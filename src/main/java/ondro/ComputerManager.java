package ondro;

import java.util.List;

/**
 * Created by oreqizer on 16/03/16.
 */
public interface ComputerManager {

    public Computer createComputer(Computer pc);

    public void removeComputer(Long id);

    public Computer getComputer(Long id);

    public List<Computer> getFreeComputers();

}
