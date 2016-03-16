package ondro;

import java.util.List;

/**
 * Created by oreqizer on 16/03/16.
 */
public interface ComponentManager {
    
    public Component createComponent(Component pc);

    public void removeComponent(Component pc);

    public Component getComponent(Long id);

    public List<Component> getFreeComponents();
    
}
