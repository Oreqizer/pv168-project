package leet.common;

import javax.sql.DataSource;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by oreqizer on 22/03/16.
 */
public abstract class DBUtils {

    private static final Logger logger = Logger.getLogger(
            DBUtils.class.getName()
    );

    /**
     * Closes connection and logs possible error.
     *
     * @param conn connection to close
     * @param statements  statements to close
     */
    public static void closeQuietly(Connection conn, Statement ...statements) {
        for (Statement st : statements) {
            if (st != null) {
                try {
                    st.close();
                } catch (SQLException ex) {
                    logger.log(Level.SEVERE, "Error when closing statement", ex);
                }
            }
        }

        if (conn != null) {
            try {
                conn.setAutoCommit(true);
            } catch (SQLException ex) {
                logger.log(Level.SEVERE, "Error when switching autocommit mode back to true", ex);
            }
            try {
                conn.close();
            } catch (SQLException ex) {
                logger.log(Level.SEVERE, "Error when closing connection", ex);
            }
        }
    }

    /**
     * Rolls back transaction and logs possible error.
     *
     * @param conn connection
     */
    public static void doRollbackQuietly(Connection conn) {
        if (conn != null) {
            try {
                if (conn.getAutoCommit()) {
                    throw new IllegalStateException("Connection is in the autocommit mode!");
                }
                conn.rollback();
            } catch (SQLException ex) {
                logger.log(Level.SEVERE, "Error when doing rollback", ex);
            }
        }
    }

    /**
     * Extract key from given ResultSet.
     *
     * @param key resultSet with key
     * @return key from given result set
     * @throws SQLException when operation fails
     */
    public static Long getId(ResultSet key) throws SQLException {
        if (key.getMetaData().getColumnCount() != 1) {
            throw new IllegalArgumentException("Given ResultSet contains more columns");
        }
        if (key.next()) {
            Long result = key.getLong(1);
            if (key.next()) {
                throw new IllegalArgumentException("Given ResultSet contains more rows");
            }
            return result;
        } else {
            throw new IllegalArgumentException("Given ResultSet contain no rows");
        }
    }

    /**
     * Check if updates count is one. Otherwise appropriate exception is thrown.
     *
     * @param count updates count.
     * @param entity updated entity (for includig to error message)
     * @param insert flag if performed operation was insert
     * @throws EntityException when updates count is zero, so updated entity does not exist
     * @throws DBException when updates count is unexpected number
     */
    public static void checkUpdatesCount(
            int count,
            Object entity,
            boolean insert
    ) throws EntityException, DBException {

        if (!insert && count == 0) {
            throw new EntityException("Entity " + entity + " does not exist in the db");
        }

        if (count != 1) {
            throw new DBException("Internal integrity error: Unexpected rows count in database affected: " + count);
        }

    }

    /**
     * Executes SQL script.
     *
     * @param ds datasource
     * @param scriptUrl url of sql script to be executed
     * @throws SQLException when operation fails
     */
    public static void executeSqlScript(DataSource ds, URL scriptUrl) throws SQLException {
        Connection conn = null;
        try {

            conn = ds.getConnection();
            for (String st : readSqlStatements(scriptUrl)) {
                if (!st.trim().isEmpty()) {
                    conn.prepareStatement(st).executeUpdate();
                }
            }

        } finally {
            closeQuietly(conn);
        }
    }

    /**
     * Reads SQL statements from a file
     *
     * @param url url of the .sql file
     * @return all the commands
     */
    private static List<String> readSqlStatements(URL url) {
        try {

            if (url.toString().contains("C:")) { // toto je Ondrej
                url = new URL("file:" + url.toString().substring(6));
            }

            Path path = Paths.get(url.getPath());
            return new ArrayList<>(
                    Arrays.asList(String
                            .join("", Files.readAllLines(path))
                            .split(";"))
            );

        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

}
