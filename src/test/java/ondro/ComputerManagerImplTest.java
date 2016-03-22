package java.ondro;

import ondro.Computer;
import ondro.ComputerManagerImpl;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsSame.sameInstance;
import static org.junit.Assert.*;

/**
 * Created by zeman on 16/03/16.
 */
public class ComputerManagerImplTest {

    private ComputerManagerImpl manager;

    @Before
    public void setUp() throws Exception {
        manager = new ComputerManagerImpl();
    }

    @Test
    public void testCreateComputer() throws Exception {
        Computer computer = new Computer(10,10,10);
        manager.createComputer(computer);

        Long pcId = computer.getId();

        assertThat("computer has null id", computer.getId(), is(not(equalTo(null))));
        Computer result = manager.getComputer(pcId);

        assertThat("loaded pc differs from the saved one", result, is(equalTo(computer)));

        assertThat("loaded pc is the same instance", result, is(not(sameInstance(computer))));

        assertDeepEquals(computer, result);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCreateComputerNull() throws Exception {
        manager.createComputer(null);
    }

    @Test
    public void testRemoveComputer() throws Exception {
        Computer pc = new Computer(5,2,3);
        Computer pc1 = new Computer(20,100,750);

        manager.createComputer(pc);
        manager.createComputer(pc1);


        assertNotNull(manager.getComputer(pc.getId()));
        assertNotNull(manager.getComputer(pc1.getId()));

        manager.removeComputer(pc.getId());

        assertNull(manager.getComputer(pc.getId()));
        assertNotNull(manager.getComputer(pc1.getId()));
    }
    
    @Test
    public void testGetComputer() throws Exception {
        Computer component = new Computer(50,10,500);
        Computer component1 = new Computer(20,100,750);

        manager.createComputer(component);
        manager.createComputer(component1);


        assertNotNull(manager.getComputer(component.getId()));
        assertNotNull(manager.getComputer(component1.getId()));
    }

    @Test
    public void testGetAllComputers() throws Exception {
        assertTrue(manager.getAllComputers().isEmpty());
        Computer pc = new Computer(50,10,500);
        Computer pc1 = new Computer(20,100,750);

        manager.createComputer(pc);
        manager.createComputer(pc1);

        List<Computer> list = Arrays.asList(pc, pc1);
        List<Computer> actual = manager.getAllComputers();


        Collections.sort(actual, idComparator);
        Collections.sort(list, idComparator);
        assertEquals("saved and retrieved components are not the same", list, actual);
        assertDeepEquals(list,actual);
    }
    private void assertDeepEquals(Computer expected, Computer actual) {
        assertEquals("id value is not equal", expected.getId(), actual.getId());
        assertEquals("cooling value is not equal",expected.getCooling(), actual.getCooling());
        assertEquals("slots is not equal", expected.getSlots(), actual.getSlots());
        assertEquals("price value is not equal", expected.getPrice(), actual.getPrice());
    }
    private static Comparator<Computer> idComparator = new Comparator<Computer>() {
        @Override
        public int compare(Computer o1, Computer o2) {
            return o1.getId().compareTo(o2.getId());
        }
    };
    private void assertDeepEquals(List<Computer> expectedList, List<Computer> actualList) {
        for (int i = 0; i < expectedList.size(); i++) {
            Computer expected = expectedList.get(i);
            Computer actual = actualList.get(i);
            assertDeepEquals(expected, actual);
        }
    }
}