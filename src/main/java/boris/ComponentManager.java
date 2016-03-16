package boris;

import java.util.List;

/**
 * Created by oreqizer on 16/03/16.
 */
public interface ComponentManager {
    
    public void createComponent(Component pc);

    public void removeComponent(long id);

    public Component getComponent(long id);

    public List<Component> getFreeComponents();
    
}
