package ondro;

import java.util.List;

/**
 * Created by oreqizer on 16/03/16.
 */
public interface ComponentManager {
    
    public void createComponent(Component pc);

    public void removeComponent(Long id);

    public Component getComponent(Long id);

    public List<Component> getFreeComponents();
    
}
