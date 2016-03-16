package boris;

import java.util.List;

/**
 * Created by oreqizer on 16/03/16.
 */
public interface ComputerManager {

    public void createComputer(Computer pc);

    public void removeComputer(long id);

    public Computer getComputer(long id);

    public List<Computer> getFreeComputers();

}
