package ondro;

import java.util.List;

/**
 * Created by zeman on 16/03/16.
 */
public class ComponentManagerImpl implements ComponentManager {

    public Component createComponent(Component component) {
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

        return null;
    }

    public void removeComponent(Component component) {

    }

    public Component getComponent(Long id) {
        return null;
    }

    public List<Component> getAllComponents() {
        return null;
    }

}
