package boris;

import org.junit.Before;
import org.junit.Test;

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
        // TODO
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCreateComputerNull() throws Exception {
        // TODO
    }

    @Test
    public void testRemoveComputer() throws Exception {
        // TODO
    }
    
    @Test
    public void testGetComputer() throws Exception {
        // TODO
    }

    @Test
    public void testGetFreeComputers() throws Exception {
        // TODO
    }
}