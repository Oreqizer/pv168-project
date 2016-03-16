package ondro;

import java.util.List;

/**
 * Created by zeman on 16/03/16.
 */
public interface ComponentManager {
    
    public Component createComponent(Component component);

    public void removeComponent(Component component);

    public Component getComponent(Long id);

    public List<Component> getFreeComponents();
    
}
