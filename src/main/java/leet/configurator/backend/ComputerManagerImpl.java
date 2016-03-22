package leet.configurator.backend;

import java.util.List;

/**
 * Created by oreqizer on 16/03/16.
 */
public class ComputerManagerImpl implements ComputerManager {

    public Computer createComputer(Computer pc) {
        if (pc == null) {
            throw new IllegalArgumentException("pc should not be null");
        }

        if (pc.getId() != null) {
            throw new IllegalArgumentException("id should be null");
        }

        if (pc.getSlots() <= 0) {
            throw new IllegalArgumentException("cannot have 0 or less slots");
        }

        if (pc.getCooling() < 0) {
            throw new IllegalArgumentException("cooling can't be negative");
        }

        if (pc.getPrice() < 0) {
            throw new IllegalArgumentException("price can't be negative");
        }
        
        return null;
    }

    public void updateComputer(Computer pc) {

    }

    public void removeComputer(Computer pc) {

    }

    public Computer getComputer(Long id) {
        return null;
    }

    public List<Computer> getAllComputers() {
        return null;
    }

}
