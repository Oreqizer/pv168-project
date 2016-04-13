package leet.configurator.backend;

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

        SimpleJdbcInsert insertComponent = new SimpleJdbcInsert(jdbc)
                .withTableName("COMPONENTS")
                .usingGeneratedKeyColumns("ID");

        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("NAME", component.getName())
                .addValue("HEAT", component.getHeat())
                .addValue("PRICE", component.getPrice())
                .addValue("ENERGY", component.getEnergy())
                .addValue("PC",component.getPid());

        Number id = insertComponent.executeAndReturnKey(parameters);
        return component.setId(id.longValue());

    }

    public void updateComponent(Component component) {

        jdbc.update(
                "UPDATE COMPONENTS set NAME=?,HEAT=?,PRICE=?,ENERGY=? where ID=?",
                component.getName(),
                component.getHeat(),
                component.getPrice(),
                component.getEnergy(),
                component.getId()
        );

    }

    private RowMapper<Component> componentMapper = (rs, rowNum) ->
        new Component(
                rs.getLong("ID"),
                rs.getLong("PC"),
                rs.getString("NAME"),
                rs.getInt("HEAT"),
                rs.getInt("PRICE"),
                rs.getInt("ENERGY")
        );

    public void removeComponent(Component component) {

        jdbc.update("DELETE FROM COMPONENTS WHERE ID=?", component.getId());

    }

    @Nullable
    public Component getComponent(Long id) {

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

        if (component.getId() == null) {
            throw new IllegalArgumentException("component id is null");
        }
        if(component.getPid()!=null){
            throw new IllegalArgumentException("component id is null");
        }

        jdbc.update(
                "UPDATE COMPONENTS set PC=? where ID=?",
                component.getPid(),
                component.getId()
        );

        return component.setPid(pc.getId());

    }

    @Override
    public Component removeComponentFromComputer(Component component ) {

        if (component.getId() == null) {
            throw new IllegalArgumentException("component id is null");
        }

        jdbc.update(
                "UPDATE COMPONENTS set PC=? where ID=?",
                NULL,component.getId()
        );

        return component.setPid(null);

    }

}
