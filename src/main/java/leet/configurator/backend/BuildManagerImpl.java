package leet.configurator.backend;

import javax.activation.DataSource;
import java.util.List;

/**
 * Created by oreqizer on 16/03/16.
 */
public final class BuildManagerImpl implements BuildManager {

    private final DataSource dataSource;

    public BuildManagerImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Build createBuild(Build build) {
        checkDataSource();
        return null;
    }

    public void updateBuild(Build build) {
        checkDataSource();

    }

    public void removeBuild(Build build) {
        checkDataSource();

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
        checkDataSource();
        return null;
    }

    private void checkDataSource() {
        if (dataSource == null) {
            throw new IllegalStateException("DataSource is not set");
        }
    }

}
