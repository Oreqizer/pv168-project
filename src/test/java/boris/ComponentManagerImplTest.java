package boris;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

/**
 * Created by oreqizer on 16/03/16.
 */
public class ComponentManagerImplTest {

    private ComponentManagerImpl manager;

    @Before
    public void setUp() throws Exception {
        manager = new ComponentManagerImpl();
    }

    @Test
    public void testCreateComponent() throws Exception {
        
        Component pure = new Component("card", 100, 200, 100);
        Component component = manager.createComponent(pure);

        assertThat("pure has null id", pure.getId(), is(equalTo(null)));
        assertThat("component has an id", pure.getId(), is(not(equalTo(null))));

        Component result = manager.getComponent(component.getId());

        assertThat("components match", result, is(equalTo(component)));
        assertThat("components don't match", result, is(not(sameInstance(pure))));
        
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateComponentNull() throws Exception {
        
        manager.createComponent(null);
        
    }
    @Test(expected = IllegalArgumentException.class)
    public void testCreateComponentFail() throws Exception {

        Component component = new Component("card", 100, 200, -100);
        manager.createComponent(component);

    }

    @Test
    public void testRemoveComponent() throws Exception {
        
        Component c1 = new Component("card", 100, 200, 100);
        Component c2 = new Component("pcu", 100, 300, 100);

        Component component1 = manager.createComponent(c1);
        Component component2 = manager.createComponent(c2);

        assertNotNull(manager.getComponent(component1.getId()));
        assertNotNull(manager.getComponent(component2.getId()));

        manager.removeComponent(component1);

        assertNull(manager.getComponent(component1.getId()));
        assertNotNull(manager.getComponent(component2.getId()));
        
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRemoveComponentNull() throws Exception {

        Component c1 = new Component("card", 100, 200, 100);

        manager.createComponent(c1);
        manager.removeComponent(null);

    }

    @Test
    public void testGetComponent() throws Exception {

        Component c1 = new Component("card", 100, 200, 100);
        Component component1 = manager.createComponent(c1);

        assertNotNull(manager.getComponent(component1.getId()));
        assertNull(manager.getComponent(component1.getId() + 5));

    }

    @Test
    public void testGetAllComponents() throws Exception {

        Component c1 = new Component("card", 100, 200, 100);
        Component component1 = manager.createComponent(c1);

        List<Component> list = manager.getAllComponents();

        assertThat("list is not null", list, is(not(equalTo(null))));
        assertThat("list has one component", list.size(), is(equalTo(1)));

        Component c2 = new Component("pcu", 100, 300, 100);
        Component component2 = manager.createComponent(c2);

        list = manager.getAllComponents();

        assertThat("list has two components", list.size(), is(equalTo(2)));
        
        manager.removeComponent(component1);
        manager.removeComponent(component2);

        list = manager.getAllComponents();

        assertThat("list is empty", list.size(), is(equalTo(0)));

    }

}