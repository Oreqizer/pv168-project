package configurator;

import configurator.common.DBUtils;
import configurator.component.Component;
import configurator.component.ComponentManager;
import configurator.component.ComponentManagerImpl;
import configurator.computer.Computer;
import configurator.computer.ComputerManager;
import configurator.computer.ComputerManagerImpl;
import org.apache.derby.jdbc.EmbeddedDataSource;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.List;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

/**
 * Created by oreqizer on 16/03/16.
 */
public class ComponentManagerImplTest {

    private ComponentManager manager;
    private DataSource ds;

    private static DataSource getDataSource() throws SQLException {
        EmbeddedDataSource ds = new EmbeddedDataSource();
        ds.setDatabaseName("memory:componentmgr-test");
        ds.setCreateDatabase("create");
        return ds;
    }

    @Before
    public void setUp() throws Exception {
        ds = getDataSource();
        DBUtils.executeSqlScript(ds, DBUtils.class.getResource("createTables.sql"));
        manager = new ComponentManagerImpl(ds);
    }

    @After
    public void tearDown() throws SQLException {
        DBUtils.executeSqlScript(ds, DBUtils.class.getResource("dropTables.sql"));
    }

    @Test
    public void testCreateComponent() throws Exception {

        Component pure = new Component("card", 100, 200, 100);

        assertThat("pure has null id", pure.getId(), is(equalTo(null)));

        pure = manager.createComponent(pure);

        assertThat("component has an id", pure.getId(), is(not(equalTo(null))));

        Component result = manager.getComponent(pure.getId());

        assertThat("components match", result, is(equalTo(pure)));
        assertThat("components don't match", result, is(not(sameInstance(pure))));

    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateComponentNull() throws Exception {

        manager.createComponent(null);

    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateComponentFail() throws Exception {

        Component component = new Component("card", 100, -200, 100);
        manager.createComponent(component);

    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateComponentFail2() throws Exception {

        Component component = new Component("", 100, 200, 100);
        manager.createComponent(component);

    }

    @Test
    public void testUpdateComponent() throws Exception {

        Component component = new Component("card", 100, 200, 100);
        component = manager.createComponent(component);

        component = component
                .setHeat(150)
                .setPrice(250)
                .setEnergy(150);

        manager.updateComponent(component);
        Component updated = manager.getComponent(component.getId());

        assertThat("component's name changed", updated.getName(), is(equalTo("card")));
        assertThat("component's heat changed", updated.getHeat(), is(equalTo(150)));
        assertThat("component's price changed", updated.getPrice(), is(equalTo(250)));
        assertThat("component's energy changed", updated.getEnergy(), is(equalTo(150)));

    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpdateComponentNull() throws Exception {

        Component c = new Component("card", 100, 200, 100);

        c = manager.createComponent(c);
        manager.updateComponent(null);

    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpdateComponentNoId() throws Exception {

        Component c = new Component("card", 100, 200, 100);

        //c = manager.createComponent(c);
        manager.updateComponent(c);

    }

    @Test
    public void testRemoveComponent() throws Exception {

        Component component1 = new Component("card", 100, 200, 100);
        Component component2 = new Component("pcu", 100, 300, 100);

        component1 = manager.createComponent(component1);
        component2 = manager.createComponent(component2);


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

    @Test(expected = IllegalArgumentException.class)
    public void testRemoveComponentNoId() throws Exception {

        Component c1 = new Component("card", 100, 200, 100);

        manager.createComponent(c1);
        manager.removeComponent(c1);

    }

    @Test
    public void testGetComponent() throws Exception {

        Component component = new Component("card", 100, 200, 100);
        component = manager.createComponent(component);

        assertNotNull(manager.getComponent(component.getId()));
        //    assertNull(manager.getComponent(component.getId() + 5));

    }

    @Test
    public void testGetAllComponents() throws Exception {

        Component component = new Component("card", 100, 200, 100);
        component = manager.createComponent(component);

        List<Component> list = manager.getAllComponents();

        assertThat("list is not null", list, is(not(equalTo(null))));
        assertThat("list has one component", list.size(), is(equalTo(1)));

        Component component2 = new Component("pcu", 100, 300, 100);
        component2 = manager.createComponent(component2);

        list = manager.getAllComponents();

        assertThat("list has two components", list.size(), is(equalTo(2)));

        manager.removeComponent(component);
        manager.removeComponent(component2);

        list = manager.getAllComponents();

        assertThat("list is empty", list.size(), is(equalTo(0)));

    }

    @Test
    public void testAddComponentToComputer() throws Exception {

        Component component = new Component("card", 100, 200, 100);
        component = manager.createComponent(component);

        Computer pc = new Computer(3, 2000, 300);
        ComputerManager pcmgr = new ComputerManagerImpl(getDataSource());
        pc = pcmgr.createComputer(pc);

        component = manager.addComponentToComputer(component, pc);

        Component component2 = manager.getComponent(component.getId());

        assertThat("component's pc changed", component2.getPid(), is(equalTo(pc.getId())));
        assertThat("pc has component", pc.getComponents().contains(component2), is(equalTo(true)));

    }

    @Test
    public void testRemoveComponentFromComputer() throws Exception {

        Component component = new Component("card", 100, 200, 100);
        component = manager.createComponent(component);

        Computer pc = new Computer(3, 2000, 300);

        ComputerManager pcmgr = new ComputerManagerImpl(getDataSource());
        pc = pcmgr.createComputer(pc);

        component = manager.addComponentToComputer(component,pc);

        Component c2 = manager.getComponent(component.getId());

        assertThat("component's pc is there", c2.getPid(), is(equalTo(pc.getId())));

        component = manager.removeComponentFromComputer(component, pc);

        c2 = manager.getComponent(component.getId());

        assertThat("component's pc changed", c2.getPid(), is(equalTo(null)));
        assertThat("pc has component", pc.getComponents().contains(c2), is(equalTo(false)));

    }
}