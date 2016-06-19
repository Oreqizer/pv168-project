package configurator.computer;

import configurator.common.DBException;
import configurator.common.EntityException;
import configurator.component.Component;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by oreqizer on 16/03/16.
 */
public final class ComputerManagerImpl implements ComputerManager {

    private final JdbcTemplate jdbc;

    public ComputerManagerImpl(DataSource dataSource) {
        this.jdbc = new JdbcTemplate(dataSource);
    }

    @Nullable
    public Computer createComputer(Computer pc) throws DBException, EntityException {
        
        validate(pc);

        if (pc.getId() != null) {
            throw new IllegalArgumentException("id of a new pc should be null");
        }

        SimpleJdbcInsert insertComponent = new SimpleJdbcInsert(jdbc)
                .withTableName("COMPUTERS")
                .usingGeneratedKeyColumns("ID");

        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("SLOTS", pc.getSlots())
                .addValue("COOLING", pc.getCooling())
                .addValue("PRICE", pc.getPrice())
                .addValue("ENERGY", pc.getEnergy());

        Number id = insertComponent.executeAndReturnKey(parameters);
        return pc.setId(id.longValue());
    }

    public void updateComputer(Computer pc) throws EntityException, DBException {
        
        validate(pc);

        if (pc.getId() == null) {
            throw new IllegalArgumentException("computer id is null");
        }
        jdbc.update(
                "UPDATE COMPUTERS set SLOTS=?,COOLING=?,PRICE=?,ENERGY=? where ID=?",
                pc.getSlots(),
                pc.getCooling(),
                pc.getPrice(),
                pc.getEnergy(),
                pc.getId()
        );
    }

    public void removeComputer(Long id) throws EntityException, DBException {
        

        if (id == null) {
            throw new IllegalArgumentException("id is null");
        }

        Computer pc = getComputer(id);
        assert pc != null;
        for (Component cp : pc.getComponents()) {
            jdbc.update("UPDATE COMPONENTS SET PC=? WHERE ID=?",
                    null, cp.getId());
        }

        jdbc.update("DELETE FROM COMPUTERS WHERE ID=?", id);
    }

    public void removeAllComputers() throws EntityException, DBException {
        for (Computer pc : getAllComputers()) removeComputer(pc.getId());
    }

    @Nullable
    public Computer getComputer(long id) {
        

        try {
            Computer pc = jdbc.queryForObject(
                    "SELECT * FROM COMPUTERS WHERE id=?"
                    , computerMapper, id);

            pc = pc.setComponents(jdbc.query("SELECT * FROM COMPONENTS WHERE pc=?", componentMapper, id));
            return pc;

        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Transactional
    @Nullable
    public List<Computer> getAllComputers() {
        List<Computer> tmp = jdbc.query("SELECT * FROM COMPUTERS", computerMapper);

        if (tmp == null) return new ArrayList<>();

        List<Computer> res = tmp.stream().map(pc -> pc.setComponents(jdbc.query("SELECT * FROM COMPONENTS WHERE PC=?", componentMapper, pc.getId()))).collect(Collectors.toList());
        return res;
    }

    private RowMapper<Computer> computerMapper = (rs, rowNum) ->
            new Computer(
                    rs.getInt("SLOTS"),
                    rs.getInt("COOLING"),
                    rs.getInt("PRICE"),
                    rs.getInt("ENERGY")).setId(rs.getLong("ID"));

    @Contract("null -> fail")
    private void validate(Computer pc) {

        if (pc == null) {
            throw new IllegalArgumentException("pc should not be null");
        }
        if (pc.getSlots() <= 0) {
            throw new IllegalArgumentException("cannot have 0 or less slots");
        }
        if (pc.getCooling() < 0) {
            throw new IllegalArgumentException("cooling can't be negative");
        }
        if (pc.getPrice() < 0) {
            throw new IllegalArgumentException("price can't be negative");
        }
    }

    private RowMapper<Component> componentMapper = (rs, rowNum) ->
            new Component(
                    rs.getLong("ID"),
                    rs.getLong("PC") == 0 ? null : rs.getLong("PC"),
                    rs.getString("NAME"),
                    rs.getInt("HEAT"),
                    rs.getInt("PRICE"),
                    rs.getInt("ENERGY")
            );

}
