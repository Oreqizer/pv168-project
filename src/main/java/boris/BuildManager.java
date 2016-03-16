package boris;

/**
 * Created by oreqizer on 16/03/16.
 */
public interface BuildManager {

    /**
     * creates a new build
     *
     * @param build build to create
     * @return new build with id filled
     */
    public Build createBuild(Build build);

    /**
     * updates an existing build
     *
     * @param build build to update
     */
    public void updateBuild(Build build);

    /**
     * removes a build
     *
     * @param build build to remove
     */
    public void removeBuild(Build build);

    /**
     * verifies if a build is valid
     *
     * @param build build to verify
     * @return validity of the build
     */
    public boolean verifyBuild(Build build);

    /**
     * computes total heat of the build
     *
     * @param build build to compute
     * @return total heat
     */
    public int getHeat(Build build);

    /**
     * computes total energy of the build
     *
     * @param build build to compute
     * @return total energy
     */
    public int getEnergy(Build build);

    /**
     * computes total price of the build
     *
     * @param build build to compute
     * @return total price
     */
    public int getPrice(Build build);

    /**
     * returns number of free slots
     *
     * @param build build to check
     * @return free slots
     */
    public int getFreeSlots(Build build);

}
