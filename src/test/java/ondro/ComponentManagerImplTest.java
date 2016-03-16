package java.ondro;

import ondro.Component;
import ondro.ComponentManagerImpl;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

/**
 * Created by zeman on 16/03/16.
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

    @Test
    public void testRemoveComponent() throws Exception {
        // TODO
    }

    @Test
    public void testGetComponent() throws Exception {
        // TODO
    }

    @Test
    public void testGetFreeComponents() throws Exception {
        // TODO
    }
}