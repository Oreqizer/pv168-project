package leet.configurator.backend;

import javax.activation.DataSource;
import java.util.List;

/**
 * Created by oreqizer on 16/03/16.
 */
public final class ComponentManagerImpl implements ComponentManager {

    private DataSource dataSource;

    public ComponentManagerImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Component createComponent(Component component) {
        validate(component);

        return null;
    }

    public void updateComponent(Component component) {
        validate(component);

    }

    public void removeComponent(Component component) {

    }

    public Component getComponent(Long id) {
        return null;
    }

    public List<Component> getAllComponents() {
        return null;
    }

    private void validate(Component component) {

        if (component == null) {
            throw new IllegalArgumentException("pc should not be null");
        }

        if (component.getId() != null) {
            throw new IllegalArgumentException("id should be null");
        }

        if (component.getName().equals("")) {
            throw new IllegalArgumentException("name cannot be empty");
        }

        if (component.getPrice() < 0) {
            throw new IllegalArgumentException("price can't be negative");
        }

    }

}
