package configurator.component;

import configurator.computer.Computer;
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
public final class ComponentManagerImpl implements ComponentManager {

    private final JdbcTemplate jdbc;

    public ComponentManagerImpl(DataSource dataSource) {
        this.jdbc = new JdbcTemplate(dataSource);
    }

    @Nullable
    public Component createComponent(Component component) {
        validate(component);

        if (component.getId() != null) {
            throw new IllegalArgumentException("id of a new component should be null");
        }

        SimpleJdbcInsert insertComponent = new SimpleJdbcInsert(jdbc)
                .withTableName("COMPONENTS")
                .usingGeneratedKeyColumns("ID");

        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("PC", component.getPid())
                .addValue("NAME", component.getName())
                .addValue("HEAT", component.getHeat())
                .addValue("PRICE", component.getPrice())
                .addValue("ENERGY", component.getEnergy());

        Number id = insertComponent.executeAndReturnKey(parameters);
        return component.setId(id.longValue());

    }

    public void updateComponent(Component component) {
        validate(component);
        if (component.getId() == null) {
            throw new IllegalArgumentException("component id is null");
        }
        jdbc.update(
                "UPDATE COMPONENTS set NAME=?,PC=?,HEAT=?,PRICE=?,ENERGY=? where ID=?",
                component.getName(),
                component.getPid(),
                component.getHeat(),
                component.getPrice(),
                component.getEnergy(),
                component.getId()
        );

    }

    public void removeAllComponents() {
        getAllComponents().forEach(this::removeComponent);
    }


    public void removeComponent(Component component) {
        validate(component);
        if(component.getId()==null){
            throw new IllegalArgumentException("component id is null");
        }
        jdbc.update("DELETE FROM COMPONENTS WHERE ID=?", component.getId());

    }

    public void removeComponentById(long id) {
        jdbc.update("DELETE FROM COMPONENTS WHERE ID=?", id);

    }

    @Nullable
    public Component getComponent(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("id cannot be null");
        }
        try {
            return jdbc.queryForObject(
                    "SELECT * FROM COMPONENTS WHERE id=?"
                    , componentMapper, id);
        }catch (EmptyResultDataAccessException e){
            return null;
        }
    }


    @Transactional
    @Override
    public int getNumOfComponentsInPc(long id) {
        List<Component> list = getAllComponents();
        if (list == null || list.size() == 0) return 0;

        return (list
                .stream()
                .filter(c -> c.getPid() != null && c.getPid().equals(id))
                .collect(Collectors.toList())).size();

    }

    @Transactional
    @Override
    public List<Component> getAllFreeComponents() {
        List<Component> res = jdbc.query("SELECT * FROM COMPONENTS", componentMapper);

        List<Component> ret = new ArrayList<>();
        if (res == null) return new ArrayList<>();

        ret.addAll(res.stream().filter(c -> c.getPid() == null).collect(Collectors.toList()));

        return ret;
    }

    @Transactional
    @Override
    public List<Component> getAllComponents() {
        List<Component> res = jdbc.query("SELECT * FROM COMPONENTS", componentMapper);
        if (res == null) return new ArrayList<>();
        return res;

    }


    @Override
    public Component addComponentToComputer(Component component, long pid) {
        validate(component);
        if (component.getId() == null) {
            throw new IllegalArgumentException("component id is null");
        }
        if (component.getPid() != null) {
            throw new IllegalArgumentException("component id is null");
        }
        Computer pc = jdbc.queryForObject(
                "SELECT * FROM COMPUTERS WHERE id=?"
                , computerMapper, pid);
        pc = pc.setComponents(jdbc.query("SELECT * FROM COMPONENTS WHERE pc=?", componentMapper, pid));

        if (pc.getComponents().size() < pc.getSlots()) {
            component = component.setPid(pc.getId());
            pc.getComponents().add(component);

            pc = pc.setCooling(pc.getCooling() + component.getHeat())
                    .setPrice(pc.getPrice() + component.getPrice())
                    .setEnergy(pc.getEnergy() + component.getEnergy());
            jdbc.update(
                    "UPDATE COMPUTERS SET SLOTS=?,COOLING=?,PRICE=?,ENERGY=? WHERE ID=?",
                    pc.getSlots(),
                    pc.getCooling(),
                    pc.getPrice(),
                    pc.getEnergy(),
                    pc.getId()
            );


            jdbc.update(
                    "UPDATE COMPONENTS SET PC=? WHERE ID=?",
                    component.getPid(),
                    component.getId()
            );
        }


        return component;

    }

    @Override
    public Component removeComponentFromComputer(Component component, Computer pc) {
        validate(component);
        if (pc == null) {
            throw new IllegalArgumentException("pc is null");
        }
        if (pc.getComponents() == null) {
            throw new IllegalArgumentException("pc components is null");
        }
        if (component.getId() == null) {
            throw new IllegalArgumentException("component id is null");
        }

        jdbc.update(
                "UPDATE COMPONENTS set PC=? where ID=?",
                null, component.getId()
        );
        pc.getComponents().remove(component);
        component = component.setPid(null);

        return component;

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

    @Contract("null -> fail")
    private void validate(Component component) {

        if (component == null) {
            throw new IllegalArgumentException("pc should not be null");
        }

        if (component.getName() == null || component.getName().isEmpty()) {
            throw new IllegalArgumentException("component name cannot be null or empty");
        }

        if (component.getPrice() < 0) {
            throw new IllegalArgumentException("price can't be negative");
        }

    }

    private RowMapper<Computer> computerMapper = (rs, rowNum) ->
            new Computer(
                    rs.getInt("SLOTS"),
                    rs.getInt("COOLING"),
                    rs.getInt("PRICE"),
                    rs.getInt("ENERGY")).setId(rs.getLong("ID"));

}
