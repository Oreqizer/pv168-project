package configurator.computer;

import configurator.common.DBException;
import configurator.common.EntityException;

import java.util.List;

/**
 * Created by oreqizer on 16/03/16.
 */
public interface ComputerManager {

    public Computer createComputer(Computer pc) throws DBException, EntityException;

    public void updateComputer(Computer pc) throws DBException, EntityException;

    public void removeComputer(Long id) throws DBException, EntityException;

    public Computer getComputer(Long id);

    public List<Computer> getAllComputers();

}
