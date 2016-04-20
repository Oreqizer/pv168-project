package configurator;

import configurator.computer.Computer;
import configurator.computer.ComputerManagerImpl;
import configurator.common.DBUtils;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.List;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import org.apache.derby.jdbc.EmbeddedDataSource;

/**
 * Created by oreqizer on 16/03/16.
 */
public class ComputerManagerImplTest {

    private ComputerManagerImpl manager;
    private DataSource ds;

    private static DataSource getDataSource() throws SQLException {
        EmbeddedDataSource ds = new EmbeddedDataSource();
        ds.setDatabaseName("memory:computermgr-test");
        ds.setCreateDatabase("create");
        return ds;
    }

    @Before
    public void setUp() throws Exception {
        ds = getDataSource();
        DBUtils.executeSqlScript(ds, DBUtils.class.getResource("createTables.sql"));
        manager = new ComputerManagerImpl(ds);
    }

    @After
    public void tearDown() throws SQLException {
        DBUtils.executeSqlScript(ds, DBUtils.class.getResource("dropTables.sql"));
    }

    @Test
    public void testCreateComputer() throws Exception {

        Computer pure = new Computer(3, 2000, 300);
        Computer computer = manager.createComputer(pure);

        assertThat("pure has null id", pure.getId(), is(equalTo(null)));
        assertThat("computer has an id", computer.getId(), is(not(equalTo(null))));

        Computer result = manager.getComputer(computer.getId());

        assertThat("computers match", result, is(equalTo(computer)));
        assertThat("computers don't match", result, is(not(sameInstance(pure))));

    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateComputerNull() throws Exception {

        manager.createComputer(null);

    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateComputerFail() throws Exception {

        Computer computer = new Computer(-3, 2000, 300);
        manager.createComputer(computer);

    }

    @Test
    public void testUpdateComputer() throws Exception {

        Computer c = new Computer(3, 2000, 300);
        Computer computer = manager.createComputer(c);

        computer = computer
                .setSlots(4)
                .setCooling(2500)
                .setPrice(350);

        manager.updateComputer(computer);
        Computer updated = manager.getComputer(computer.getId());

        assertThat("computer's slots changed", updated.getSlots(), is(equalTo(4)));
        assertThat("computer's cooling changed", updated.getCooling(), is(equalTo(2500)));
        assertThat("computer's price changed", updated.getPrice(), is(equalTo(350)));

    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpdateComputerNull() throws Exception {

        Computer c = new Computer(3, 2000, 300);

        manager.createComputer(c);
        manager.updateComputer(null);

    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpdateComputerNoId() throws Exception {

        Computer c = new Computer(3, 2000, 300);

        manager.createComputer(c);
        manager.updateComputer(c);

    }

    @Test
    public void testRemoveComputer() throws Exception {

        Computer c1 = new Computer(3, 2000, 300);
        Computer c2 = new Computer(5, 1500, 200);

        Computer computer1 = manager.createComputer(c1);
        Computer computer2 = manager.createComputer(c2);

        assertNotNull(manager.getComputer(computer1.getId()));
        assertNotNull(manager.getComputer(computer2.getId()));

        manager.removeComputer(computer1.getId());

        assertNull(manager.getComputer(computer1.getId()));
        assertNotNull(manager.getComputer(computer2.getId()));

    }

    @Test(expected = IllegalArgumentException.class)
    public void testRemoveComputerNull() throws Exception {

        Computer c1 = new Computer(3, 2000, 300);

        manager.createComputer(c1);
        manager.removeComputer(null);

    }

    @Test(expected = IllegalArgumentException.class)
    public void testRemoveComputerNoId() throws Exception {

        Computer c1 = new Computer(3, 2000, 300);

        manager.createComputer(c1);
        manager.removeComputer(c1.getId());

    }

    @Test
    public void testGetComputer() throws Exception {

        Computer c1 = new Computer(3, 2000, 300);
        Computer computer1 = manager.createComputer(c1);

        assertNotNull(manager.getComputer(computer1.getId()));
        assertNull(manager.getComputer(computer1.getId() + 5));

    }

    @Test
    public void testGetAllComputers() throws Exception {

        Computer c1 = new Computer(3, 2000, 300);
        Computer computer1 = manager.createComputer(c1);

        List<Computer> list = manager.getAllComputers();

        assertThat("list is not null", list, is(not(equalTo(null))));
        assertThat("list has one computer", list.size(), is(equalTo(1)));

        Computer c2 = new Computer(5, 1500, 200);
        Computer computer2 = manager.createComputer(c2);

        list = manager.getAllComputers();

        assertThat("list has two computers", list.size(), is(equalTo(2)));

        manager.removeComputer(computer1.getId());
        manager.removeComputer(computer2.getId());

        list = manager.getAllComputers();

        assertThat("list is empty", list.size(), is(equalTo(0)));

    }
}