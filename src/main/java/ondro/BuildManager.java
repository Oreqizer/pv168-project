package ondro;

/**
 * Created by oreqizer on 16/03/16.
 */
public interface BuildManager {

    public Build addComputer(Long id);

    public void removeComputer(Long id);

    public Build addComponent(Long id);

    public void removeComponent(Long id);

    public boolean verifyBuild();

    public int getHeat();

    public int getEnergy();

    public int getPrice();

    public int getFreeSlots();

}
