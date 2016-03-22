package leet.configurator.backend;

import javax.activation.DataSource;
import java.util.List;

/**
 * Created by oreqizer on 16/03/16.
 */
public final class BuildManagerImpl implements BuildManager {

    private DataSource dataSource;

    public BuildManagerImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Build createBuild(Build build) {
        return null;
    }

    public void updateBuild(Build build) {

    }

    public void removeBuild(Build build) {

    }

    public boolean verifyBuild(Build build) {
        return false;
    }

    public int getHeat(Build build) {
        return 0;
    }

    public int getEnergy(Build build) {
        return 0;
    }

    public int getPrice(Build build) {
        return 0;
    }

    public int getFreeSlots(Build build) {
        return 0;
    }

    public List<Build> getAllBuilds() {
        return null;
    }
}
