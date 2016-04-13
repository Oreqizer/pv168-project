package leet.configurator.backend;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.util.List;

import static java.sql.JDBCType.NULL;

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
        checkJdbc();
        validate(component);

        if (component.getId() != null) {
            throw new IllegalArgumentException("id of a new component should be null");
        }

        SimpleJdbcInsert insertComponent = new SimpleJdbcInsert(jdbc)
                .withTableName("COMPONENTS")
                .usingGeneratedKeyColumns("ID");

        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("PC", component.getPid()) // TODO set null
                .addValue("NAME", component.getName())
                .addValue("HEAT", component.getHeat())
                .addValue("PRICE", component.getPrice())
                .addValue("ENERGY", component.getEnergy());

        Number id = insertComponent.executeAndReturnKey(parameters);
        return component.setId(id.longValue());

    }

    public void updateComponent(Component component) {
        checkJdbc();
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

    private RowMapper<Component> componentMapper = (rs, rowNum) ->
            new Component(
                    rs.getLong("ID"),
                    rs.getLong("PC") == 0 ? null : rs.getLong("PC"),
                    rs.getString("NAME"),
                    rs.getInt("HEAT"),
                    rs.getInt("PRICE"),
                    rs.getInt("ENERGY")
            );

    public void removeComponent(Component component) {
        checkJdbc();
        validate(component);
        jdbc.update("DELETE FROM COMPONENTS WHERE ID=?", component.getId());

    }

    @Nullable
    public Component getComponent(Long id) {
        checkJdbc();
        if (id == null) {
            throw new IllegalArgumentException("id cannot be null");
        }
        return jdbc.queryForObject(
                "SELECT * FROM COMPONENTS WHERE ID=?",
                componentMapper,
                id
        );

    }

    @Transactional
    @Override
    public List<Component> getAllComponents() {

        return jdbc.query("SELECT * FROM COMPONENTS", componentMapper);

    }

    @Override
    public Component addComponentToComputer(Component component, Computer pc) {
        checkJdbc();
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
        if (component.getPid() != null) {
            throw new IllegalArgumentException("component id is null");
        }

        jdbc.update(
                "UPDATE COMPONENTS set PC=? where ID=?",
                component.getPid(),
                component.getId()
        );

        pc.getComponents().add(component);
        return component.setPid(pc.getId());

    }

    @Override
    public Component removeComponentFromComputer(Component component) {
        checkJdbc();
        validate(component);
        if (component.getId() == null) {
            throw new IllegalArgumentException("component id is null");
        }

        jdbc.update(
                "UPDATE COMPONENTS set PC=? where ID=?",
                NULL, component.getId()
        );

        return component.setPid(null);

    }

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

    private void checkJdbc() {
        if (jdbc == null) {
            throw new IllegalStateException("jdbc is not set");
        }
    }
}
