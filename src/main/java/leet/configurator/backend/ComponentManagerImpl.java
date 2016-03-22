package leet.configurator.backend;

import leet.common.DBUtils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by oreqizer on 16/03/16.
 */
public final class ComponentManagerImpl implements ComponentManager {

    private final DataSource dataSource;

    public ComponentManagerImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Component createComponent(Component component) {
        checkDataSource();
        validate(component);

        return null;
    }

    public void updateComponent(Component component) {
        checkDataSource();
        validate(component);

    }

    public void removeComponent(Component component) {
        checkDataSource();

    }

    public Component getComponent(Long id) {
        checkDataSource();
        return null;
    }

    public List<Computer> getAllComputers() {
        checkDataSource();

        Connection conn = null;
        PreparedStatement st = null;
        try {

            conn = dataSource.getConnection();
            st = conn.prepareStatement("SELECT * FROM COMPUTERS");
            return executeQueryForMultipleComputers(st); // TODO

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtils.closeQuietly(conn, st);
        }

        return null;
    }

    private void validate(Component component) {

        if (component == null) {
            throw new IllegalArgumentException("pc should not be null");
        }

        if (component.getId() != null) {
            throw new IllegalArgumentException("id should be null");
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
