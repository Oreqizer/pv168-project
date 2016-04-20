package leet.configurator.backend;

import leet.common.DBException;
import leet.common.EntityException;

import java.util.List;

/**
 * Created by oreqizer on 16/03/16.
 */
public interface ComponentManager {
    
    public Component createComponent(Component component) throws DBException, EntityException;

    public void updateComponent(Component component) throws DBException, EntityException;

    public void removeComponent(Component component) throws DBException, EntityException;

    public Component getComponent(Long id);

    public List<Component> getAllComponents();

    public Component addComponentToComputer(Component component,Computer pc) throws DBException, EntityException;

    public Component removeComponentFromComputer(Component component ,Computer pc) throws DBException, EntityException;
}
