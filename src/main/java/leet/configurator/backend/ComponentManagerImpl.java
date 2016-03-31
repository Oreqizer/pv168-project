package leet.configurator.backend;

import leet.common.DBException;
import leet.common.DBUtils;

import leet.common.EntityException;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

import javax.sql.DataSource;
import java.sql.*;
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
    public Component createComponent(Component component) throws EntityException, DBException {
        checkDataSource();
        validate(component);

        if (component.getId() != null) {
            throw new IllegalArgumentException("id of a new component should be null");
        }

        Connection conn = null;
        PreparedStatement st = null;
        try {

            conn = dataSource.getConnection();
            conn.setAutoCommit(false);
            st = conn.prepareStatement(
                    "INSERT INTO COMPONENTS (NAME, HEAT, ENERGY, PRICE) VALUES (?,?,?,?)",
                    Statement.RETURN_GENERATED_KEYS
            );

            st.setString(1, component.getName());
            st.setInt(2, component.getHeat());
            st.setInt(3, component.getEnergy());
            st.setInt(4, component.getPrice());

            int count = st.executeUpdate();
            DBUtils.checkUpdatesCount(count, component, true);

            Long id = DBUtils.getId(st.getGeneratedKeys());
            conn.commit();
            return component.setId(id);

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtils.doRollbackQuietly(conn);
            DBUtils.closeQuietly(conn, st);
        }

        return null;
    }

    public void updateComponent(Component component) throws EntityException, DBException {

        checkDataSource();
        validate(component);

        if (component.getId() == null) {
            throw new IllegalArgumentException("component id is null");
        }

        Connection conn = null;
        PreparedStatement st = null;
        try {

            conn = dataSource.getConnection();
            conn.setAutoCommit(false);
            st = conn.prepareStatement(
                    "UPDATE COMPONENTS SET NAME = ?, HEAT = ?, ENERGY = ?, PRICE = ? WHERE ID = ?"
            );

            st.setString(1, component.getName());
            st.setInt(2, component.getHeat());
            st.setInt(3, component.getEnergy());
            st.setInt(4, component.getPrice());
            st.setLong(5, component.getId());

            int count = st.executeUpdate();
            DBUtils.checkUpdatesCount(count, component, false);
            conn.commit();

        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            DBUtils.doRollbackQuietly(conn);
            DBUtils.closeQuietly(conn, st);
        }
    }

    public void removeComponent(Component component) throws EntityException, DBException {

        checkDataSource();

        if (component == null) {
            throw new IllegalArgumentException("component is null");
        }

        if (component.getId() == null) {
            throw new IllegalArgumentException("component id is null");
        }

        Connection conn = null;
        PreparedStatement st = null;
        try {

            conn = dataSource.getConnection();
            conn.setAutoCommit(false);
            st = conn.prepareStatement(
                    "DELETE FROM COMPONENTS WHERE ID = ?"
            );

            st.setLong(1, component.getId());

            int count = st.executeUpdate();
            DBUtils.checkUpdatesCount(count, component, false);
            conn.commit();

        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            DBUtils.doRollbackQuietly(conn);
            DBUtils.closeQuietly(conn, st);
        }

    }

    @Nullable
    public Component getComponent(Long id) {

        checkDataSource();

        if (id == null) {
            throw new IllegalArgumentException("id is null");
        }

        Connection conn = null;
        PreparedStatement st = null;
        try {

            conn = dataSource.getConnection();
            st = conn.prepareStatement(
                    "SELECT * FROM COMPONENTS WHERE ID = ?"
            );

            st.setLong(1, id);

            return executeQueryForSingleComponent(st);

        } catch (SQLException|DBException ex) {
            ex.printStackTrace();
        }  finally {
            DBUtils.closeQuietly(conn, st);
        }

        return null;

    }

    @Contract(" -> !null")
    public List<Component> getAllComponents() {
        checkDataSource();

        Connection conn = null;
        PreparedStatement st = null;
        try {

            conn = dataSource.getConnection();
            st = conn.prepareStatement("SELECT * FROM COMPONENTS");
            return executeQueryForMultipleComponents(st);

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtils.closeQuietly(conn, st);
        }

        return new ArrayList<>();
    }

    @Override
    public Component addComponentToComputer(Component component, Computer pc) throws DBException, EntityException {

        checkDataSource();
        validate(component);

        if (component.getId() == null) {
            throw new IllegalArgumentException("component id is null");
        }

        if (!component.isFree()) {
            throw new IllegalArgumentException("component is not free");
        }

        Connection conn = null;
        PreparedStatement st = null;
        try {

            conn = dataSource.getConnection();
            conn.setAutoCommit(false);
            st = conn.prepareStatement(
                    "UPDATE COMPONENTS SET PC = ? WHERE ID = ?"
            );

            st.setLong(1, pc.getId());
            st.setLong(2, component.getId());

            component= component.setFree(false);
            pc.getComponents().add(component);

            int count = st.executeUpdate();
            DBUtils.checkUpdatesCount(count, component, false);
            conn.commit();

        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            DBUtils.doRollbackQuietly(conn);
            DBUtils.closeQuietly(conn, st);
        }

        return component;

    }

    @Override
    public Component removeComponentFromComputer(Component component , Computer pc) throws DBException, EntityException {

        checkDataSource();
        validate(component);

        if (component.getId() == null) {
            throw new IllegalArgumentException("component id is null");
        }

        Connection conn = null;
        PreparedStatement st = null;
        try {

            conn = dataSource.getConnection();
            conn.setAutoCommit(false);
            st = conn.prepareStatement(
                    "UPDATE COMPONENTS SET PC = ? WHERE ID = ?"
            );

            st.setNull(1, Types.BIGINT);
            st.setLong(2, component.getId());

            pc.getComponents().remove(component);
            component = component.setFree(true);

            int count = st.executeUpdate();
            DBUtils.checkUpdatesCount(count, component, false);
            conn.commit();

        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            DBUtils.doRollbackQuietly(conn);
            DBUtils.closeQuietly(conn, st);
        }

        return component;
    }

    private static Component executeQueryForSingleComponent(PreparedStatement st) throws SQLException, DBException {

        ResultSet rs = st.executeQuery();

        if (rs.next()) {
            Component result = rowToComponent(rs);
            if (rs.next()) {
                throw new DBException(
                        "Internal integrity error: more computers with the same id found!"
                );
            }
            return result;
        }

        return null;

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
                rs.getLong("PC") == 0,
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
