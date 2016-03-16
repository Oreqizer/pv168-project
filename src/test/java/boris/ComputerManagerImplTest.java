package boris;

import org.junit.Before;
import org.junit.Test;

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
        assertThat("Computer has an id", pure.getId(), is(not(equalTo(null))));

        Computer result = manager.getComputer(computer.getId());

        assertThat("Computers match", result, is(equalTo(computer)));
        assertThat("Computers don't match", result, is(not(sameInstance(pure))));

    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateComputerNull() throws Exception {

        manager.createComputer(null);

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

        manager.createComputer(null);

    }

    @Test
    public void testGetComputer() throws Exception {

        Computer c1 = new Computer(3, 2000, 300);

        Computer computer1 = manager.createComputer(c1);

        assertNotNull(manager.getComputer(computer1.getId()));
        assertNull(manager.getComputer(computer1.getId() + 5));

    }
}