package leet.configurator.backend;

import leet.common.DBUtils;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by oreqizer on 16/03/16.
 */
public final class ComponentManagerImpl implements ComponentManager {

    private final DataSource dataSource;

    public ComponentManagerImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Nullable
    public Component createComponent(Component component) {
        checkDataSource();
        validate(component);

        if (component.getId() != null) {
            throw new IllegalArgumentException("id of a new component should be null");
        }

        return null;
    }

    public void updateComponent(Component component) {
        checkDataSource();
        validate(component);

    }

    public void removeComponent(Component component) {
        checkDataSource();

    }

    @Nullable
    public Component getComponent(Long id) {
        checkDataSource();
        return null;
    }

    @Contract(" -> !null")
    public List<Component> getAllComponents() {
        return new ArrayList<>();
    }

    private static List<Component> executeQueryForMultipleComponents(PreparedStatement st) throws SQLException {
        ResultSet rs = st.executeQuery();
        List<Component> result = new ArrayList<>();
        while (rs.next()) {
            result.add(rowToComponent(rs));
        }
        return result;
    }

    @Contract("_ -> !null")
    private static Component rowToComponent(ResultSet rs) throws SQLException {
        return new Component(
                rs.getLong("ID"),
                rs.getLong("PCNAME") != 0,
                rs.getString("NAME"),
                rs.getInt("HEAT"),
                rs.getInt("PRICE"),
                rs.getInt("ENERGY")
        );
    }

    @Contract("null -> fail")
    private void validate(Component component) {

        if (component == null) {
            throw new IllegalArgumentException("pc should not be null");
        }

        if (component.getName().equals("")) {
            throw new IllegalArgumentException("name cannot be empty");
        }

        if (component.getPrice() < 0) {
            throw new IllegalArgumentException("price can't be negative");
        }

    }

    private void checkDataSource() {
        if (dataSource == null) {
            throw new IllegalStateException("DataSource is not set");
        }
    }

}
