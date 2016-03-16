package ondro;

import java.util.List;

/**
 * Created by oreqizer on 16/03/16.
 */
public class ComponentManagerImpl implements ComponentManager {

    public Component createComponent(Component pc) {
        if (pc == null) {
            throw new IllegalArgumentException("pc should not be null");
        }

        if (pc.getId() != null) {
            throw new IllegalArgumentException("id should be null");
        }

        if (pc.getName().equals("")) {
            throw new IllegalArgumentException("name cannot be empty");
        }

        if (pc.getPrice() < 0) {
            throw new IllegalArgumentException("price can't be negative");
        }

        return null;
    }

    public void removeComponent(Component pc) {

    }

    public Component getComponent(Long id) {
        return null;
    }

    public List<Component> getFreeComponents() {
        return null;
    }

}
