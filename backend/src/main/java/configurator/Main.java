package configurator;

import configurator.common.DBException;
import configurator.common.DBUtils;
import org.apache.derby.jdbc.EmbeddedDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.SQLException;

/**
 * Created by oreqizer on 20/04/16.
 */
public class Main {

    final static Logger log = LoggerFactory.getLogger(Main.class);

    public static DataSource createMemoryDatabase() {
        EmbeddedDataSource ds = new EmbeddedDataSource();
        ds.setDatabaseName("memory:configurator; create=true");

        try {
            DBUtils.executeSqlScript(ds, DBUtils.class.getResource("createTables.sql"));
        } catch (SQLException ex) {
            log.error(ex.toString());
        }

        ds.setCreateDatabase("create");
        return ds;
    }

    public static void main(String[] args) throws DBException {

        log.info("Creating DBs...");

        DataSource dataSource = createMemoryDatabase();

    }

}
