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
public final class ComputerManagerImpl implements ComputerManager {

    private final DataSource dataSource;

    public ComputerManagerImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Nullable
    public Computer createComputer(Computer pc) throws DBException, EntityException {

        checkDataSource();
        validate(pc);

        if (pc.getId() != null) {
            throw new IllegalArgumentException("id of a new pc should be null");
        }

        Computer updatedPc = null;

        Connection conn = null;
        PreparedStatement st = null;
        try {

            conn = dataSource.getConnection();
            conn.setAutoCommit(false);
            st = conn.prepareStatement(
                    "INSERT INTO COMPUTERS (SLOTS, COOLING, PRICE) VALUES (?,?,?)",
                    Statement.RETURN_GENERATED_KEYS
            );

            st.setInt(1, pc.getSlots());
            st.setInt(2, pc.getCooling());
            st.setInt(3, pc.getPrice());

            int count = st.executeUpdate();
            DBUtils.checkUpdatesCount(count, pc, true);

            Long id = DBUtils.getId(st.getGeneratedKeys());
            conn.commit();
            updatedPc = pc.setId(id);

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtils.doRollbackQuietly(conn);
            DBUtils.closeQuietly(conn, st);
        }

        return updatedPc;

    }

    public void updateComputer(Computer pc) throws EntityException, DBException {

        checkDataSource();
        validate(pc);

        if (pc.getId() == null) {
            throw new EntityException("computer id is null");
        }

        Connection conn = null;
        PreparedStatement st = null;
        try {

            conn = dataSource.getConnection();
            conn.setAutoCommit(false);
            st = conn.prepareStatement(
                    "UPDATE COMPUTERS SET SLOTS = ?, COOLING = ?, PRICE = ? WHERE ID = ?"
            );

            st.setInt(1, pc.getSlots());
            st.setInt(2, pc.getCooling());
            st.setInt(3, pc.getCooling());
            st.setLong(4, pc.getId());

            int count = st.executeUpdate();
            DBUtils.checkUpdatesCount(count, pc, false);
            conn.commit();

        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            DBUtils.doRollbackQuietly(conn);
            DBUtils.closeQuietly(conn, st);
        }

    }

    public void removeComputer(Computer pc) throws EntityException, DBException {

        checkDataSource();

        if (pc == null) {
            throw new IllegalArgumentException("computer is null");
        }

        if (pc.getId() == null) {
            throw new EntityException("computer id is null");
        }

        Connection conn = null;
        PreparedStatement st = null;
        try {

            conn = dataSource.getConnection();
            conn.setAutoCommit(false);
            st = conn.prepareStatement(
                    "DELETE FROM COMPUTERS WHERE ID = ?"
            );

            st.setLong(1, pc.getId());

            int count = st.executeUpdate();
            DBUtils.checkUpdatesCount(count, pc, false);
            conn.commit();

        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            DBUtils.doRollbackQuietly(conn);
            DBUtils.closeQuietly(conn, st);
        }

    }

    @Nullable
    public Computer getComputer(Long id) {

        checkDataSource();

        if (id == null) {
            throw new IllegalArgumentException("id is null");
        }

        Connection conn = null;
        PreparedStatement st = null;
        try {

            conn = dataSource.getConnection();
            st = conn.prepareStatement(
                    "SELECT * FROM COMPUTERS WHERE ID = ?"
            );

            st.setLong(1, id);

            return executeQueryForSingleComputer(st);

        } catch (SQLException|DBException ex) {
            ex.printStackTrace();
        }  finally {
            DBUtils.closeQuietly(conn, st);
        }

        return null;

    }

    @Nullable
    public List<Computer> getAllComputers() {

        checkDataSource();

        Connection conn = null;
        PreparedStatement st = null;
        try {

            conn = dataSource.getConnection();
            st = conn.prepareStatement("SELECT * FROM COMPUTERS");
            return executeQueryForMultipleComputers(st);

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtils.closeQuietly(conn, st);
        }

        return new ArrayList<>();

    }

    private static Computer executeQueryForSingleComputer(PreparedStatement st) throws SQLException, DBException {

        ResultSet rs = st.executeQuery();

        if (rs.next()) {
            Computer result = rowToComputer(rs);
            if (rs.next()) {
                throw new DBException(
                        "Internal integrity error: more computers with the same id found!");
            }
            return result;
        }

        return null;

    }

    private static List<Computer> executeQueryForMultipleComputers(PreparedStatement st) throws SQLException {

        ResultSet rs = st.executeQuery();

        List<Computer> result = new ArrayList<>();
        while (rs.next()) {
            result.add(rowToComputer(rs));
        }

        return result;

    }

    @Contract("_ -> !null")
    private static Computer rowToComputer(ResultSet rs) throws SQLException {
        return new Computer(
                rs.getInt("SLOTS"),
                rs.getInt("COOLING"),
                rs.getInt("PRICE")
        ).setId(rs.getLong("ID"));
    }

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

    private void checkDataSource() {
        if (dataSource == null) {
            throw new IllegalStateException("DataSource is not set");
        }
    }

}
