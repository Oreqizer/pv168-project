package boris;

/**
 * Created by oreqizer on 16/03/16.
 */
public interface BuildManager {

    public void addComputer(Long id);

    public void removeComputer(Long id);

    public void addComponent(Long id);

    public void removeComponent(Long id);

    public void verifyBuild();

    public void getHeat();

    public void getEnergy();

    public void getPrice();

    public void getFreeSlots();

}
