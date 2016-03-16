package ondro;

/**
 * Created by zeman on 16/03/16.
 */
public class BuildManagerImpl implements BuildManager {


    @Override
    public Build addBuild(Computer computer) {
        return null;
    }

    @Override
    public void removeBuild(Build buildId) {

    }

    @Override
    public Build addComponent(Component component, Build build) {
        return null;
    }

    @Override
    public void removeComponent(Long id, Build buildId) {

    }

    @Override
    public boolean verifyBuild(Build buildId) {
        return false;
    }

    @Override
    public int getHeat(Build buildId) {
        return 0;
    }

    @Override
    public int getEnergy(Build buildId) {
        return 0;
    }

    @Override
    public int getPrice(Build buildId) {
        return 0;
    }

    @Override
    public int getFreeSlots(Build buildId) {
        return 0;
    }
}
