package leet.configurator.backend;

import leet.common.DBException;
import leet.common.EntityException;
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
import java.util.List;

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
        checkJdbc();
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
                .addValue("PRICE", pc.getPrice());
        Number id = insertComponent.executeAndReturnKey(parameters);
        return pc.setId(id.longValue());
    }

    public void updateComputer(Computer pc) throws EntityException, DBException {
        checkJdbc();
        validate(pc);

        if (pc.getId() == null) {
            throw new IllegalArgumentException("computer id is null");
        }
        jdbc.update(
                "UPDATE COMPUTERS set SLOTS=?,COOLING=?,PRICE=? where ID=?",
                pc.getSlots(),
                pc.getCooling(),
                pc.getPrice(),
                pc.getId()
        );
    }

    public void removeComputer(Computer pc) throws EntityException, DBException {
        checkJdbc();
        validate(pc);

        if (pc.getId() == null) {
            throw new IllegalArgumentException("computer id is null");
        }
        jdbc.update("DELETE FROM COMPUTERS WHERE ID=?", pc.getId());
    }

    @Nullable
    public Computer getComputer(Long id) {
        checkJdbc();
        if (id == null) {
            throw new IllegalArgumentException("id is null");
        }
        try {
            return jdbc.queryForObject(
                    "SELECT * FROM COMPUTERS WHERE id=?"
                    , computerMapper, id);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Transactional
    @Nullable
    public List<Computer> getAllComputers() {
        return jdbc.query("SELECT * FROM COMPUTERS", computerMapper);
    }

    private RowMapper<Computer> computerMapper = (rs, rowNum) ->
            new Computer(
                    rs.getInt("SLOTS"),
                    rs.getInt("COOLING"),
                    rs.getInt("PRICE")
            ).setId(rs.getLong("ID"));

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

    private void checkJdbc() {
        if (jdbc == null) {
            throw new IllegalStateException("jdbc is not set");
        }
    }

}
