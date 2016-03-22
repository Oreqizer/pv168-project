package java.ondro;

import ondro.Component;
import ondro.ComponentManagerImpl;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

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
    public void createComponentNull() throws Exception {
        manager.createComponent(null);
    }

    @Test
    public void testRemoveComponent() throws Exception {
        Component component = new Component("asdf",50,10,500);
        Component component1 = new Component("1337",20,100,750);

        manager.createComponent(component);
        manager.createComponent(component1);


        assertNotNull(manager.getComponent(component.getId()));
        assertNotNull(manager.getComponent(component1.getId()));

        manager.removeComponent(component);

        assertNull(manager.getComponent(component.getId()));
        assertNotNull(manager.getComponent(component1.getId()));

    }
    @Test
    public void createComponentWithWrongValues() {
        Component component = new Component("ddsd",5,3,6);

        component.setId(1L);
        try {
            manager.createComponent(component);
            fail("should refuse assigned id");
        } catch (IllegalArgumentException ex) {
            //OK
        }

        component = new Component("ddsd",5,-1,6);
        try {
            manager.createComponent(component);
            fail("negative price not detected");
        } catch (IllegalArgumentException ex) {
            //OK
        }

    }


    @Test
    public void testGetComponent() throws Exception {

        Component component = new Component("asdf",50,10,500);
        Component component1 = new Component("1337",20,100,750);

        manager.createComponent(component);
        manager.createComponent(component1);


        assertNotNull(manager.getComponent(component.getId()));
        assertNotNull(manager.getComponent(component1.getId()));

    }

    @Test
    public void testDeleteComponentWithWrongAttributes(){
        Component component = new Component("dsds",20,3,65);

        try {
            manager.removeComponent(null);
            fail();
        } catch (IllegalArgumentException ex) {

        }

        try {
            component.setId(null);
            manager.removeComponent(component);
            fail();
        } catch (IllegalArgumentException ex) {

        }

        try {
            component.setId(1L);
            manager.removeComponent(component);
            fail();
        } catch (IllegalArgumentException ex) {

        }

    }

    @Test
    public void testGetAllComponents() throws Exception {
        assertTrue(manager.getAllComponents().isEmpty());
        Component component = new Component("asdf",50,10,500);
        Component component1 = new Component("1337",20,100,750);

        manager.createComponent(component);
        manager.createComponent(component1);

        List<Component> list = Arrays.asList(component,component1);
        List<Component> actual = manager.getAllComponents();


        Collections.sort(actual, idComparator);
        Collections.sort(list, idComparator);
        assertEquals("saved and retrieved components are not the same", list, actual);
        assertDeepEquals(list,actual);

    }
    private void assertDeepEquals(List<Component> expectedList, List<Component> actualList) {
        for (int i = 0; i < expectedList.size(); i++) {
            Component expected = expectedList.get(i);
            Component actual = actualList.get(i);
            assertDeepEquals(expected, actual);
        }
    }

    private void assertDeepEquals(Component expected, Component actual) {
        assertEquals("id value is not equal", expected.getId(), actual.getId());
        assertEquals("energy value is not equal",expected.getEnergy(), actual.getEnergy());
        assertEquals("name is not equal", expected.getName(), actual.getName());
        assertEquals("heat value is not equal", expected.getHeat(), actual.getHeat());
        assertEquals("price value is not equal", expected.getPrice(), actual.getPrice());
    }


    private static Comparator<Component> idComparator = new Comparator<Component>() {
        @Override
        public int compare(Component o1, Component o2) {
            return o1.getId().compareTo(o2.getId());
        }
    };
}