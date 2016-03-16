package ondro;

/**
 * Created by zeman on 16/03/16.
 */
public interface BuildManager {

    public Build addBuild(Computer computer);

    public void removeBuild(Build buildId);

    public Build addComponent(Component component,Build build);

    public void removeComponent(Long id,Build buildId);

    public boolean verifyBuild(Build buildId);

    public int getHeat(Build buildId);

    public int getEnergy(Build buildId);

    public int getPrice(Build buildId);

    public int getFreeSlots(Build buildId);

}
