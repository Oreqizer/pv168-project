package leet.configurator.backend;

import leet.common.DBException;
import leet.common.DBUtils;
import leet.common.EntityException;

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

    public Computer createComputer(Computer pc) throws DBException, EntityException {

        checkDataSource();
        validate(pc);

        Computer updatedPc = null;

        Connection conn = null;
        PreparedStatement st = null;
        try {

            conn = dataSource.getConnection();
            conn.setAutoCommit(false);
            st = conn.prepareStatement(
                    "INSERT INTO COMPUTERS (SLOTS,COOLING,PRICE) VALUES (?,?,?)",
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

    public void updateComputer(Computer pc) {
        checkDataSource();
        validate(pc);
    }

    public void removeComputer(Computer pc) {
        checkDataSource();

    }

    public Computer getComputer(Long id) {
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
            return executeQueryForMultipleComputers(st);

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtils.closeQuietly(conn, st);
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

    private static Computer rowToComputer(ResultSet rs) throws SQLException {
        return new Computer(
                rs.getLong("ID"),
                rs.getLong("BUILDID") != 0,
                rs.getInt("SLOTS"),
                rs.getInt("COOLING"),
                rs.getInt("PRICE")
        );
    }

    private void validate(Computer pc) {

        if (pc == null) {
            throw new IllegalArgumentException("pc should not be null");
        }

        if (pc.getId() != null) {
            throw new IllegalArgumentException("id should be null");
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
