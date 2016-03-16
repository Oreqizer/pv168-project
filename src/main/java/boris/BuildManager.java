package boris;

/**
 * Created by oreqizer on 16/03/16.
 */
public interface BuildManager {

    public void addComputer(long id);

    public void removeComputer(long id);

    public void addComponent(long id);

    public void removeComponent(long id);

    public void verifyBuild();

    public void getHeat();

    public void getEnergy();

    public void getPrice();

    public void getFreeSlots();

}
