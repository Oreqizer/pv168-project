package boris;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

/**
 * Created by oreqizer on 16/03/16.
 */
public class ComputerManagerImplTest {

    private ComputerManagerImpl manager;

    @Before
    public void setUp() throws Exception {
        manager = new ComputerManagerImpl();
    }

    @Test
    public void testCreateComputer() throws Exception {

        Computer pure = new Computer(3, 2000, 300);
        Computer computer = manager.createComputer(pure);

        assertThat("pure has null id", pure.getId(), is(equalTo(null)));
        assertThat("computer has an id", pure.getId(), is(not(equalTo(null))));

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
    public void testRemoveComputer() throws Exception {

        Computer c1 = new Computer(3, 2000, 300);
        Computer c2 = new Computer(5, 1500, 200);

        Computer computer1 = manager.createComputer(c1);
        Computer computer2 = manager.createComputer(c2);

        assertNotNull(manager.getComputer(computer1.getId()));
        assertNotNull(manager.getComputer(computer2.getId()));

        manager.removeComputer(computer1);

        assertNull(manager.getComputer(computer1.getId()));
        assertNotNull(manager.getComputer(computer2.getId()));

    }

    @Test(expected = IllegalArgumentException.class)
    public void testRemoveComputerNull() throws Exception {

        Computer c1 = new Computer(3, 2000, 300);

        manager.createComputer(c1);
        manager.removeComputer(null);

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

        manager.removeComputer(computer1);
        manager.removeComputer(computer2);

        list = manager.getAllComputers();

        assertThat("list is empty", list.size(), is(equalTo(0)));

    }
}