package leet.configurator.backend;

import javax.activation.DataSource;
import java.util.List;

/**
 * Created by oreqizer on 16/03/16.
 */
public final class ComputerManagerImpl implements ComputerManager {

    private final DataSource dataSource;

    public ComputerManagerImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Computer createComputer(Computer pc) {
        validate(pc);

        return null;
    }

    public void updateComputer(Computer pc) {
        validate(pc);
    }

    public void removeComputer(Computer pc) {

    }

    public Computer getComputer(Long id) {
        return null;
    }

    public List<Computer> getAllComputers() {
        return null;
    }

    private void validate(Computer pc) {

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

    }

    private void checkDataSource() {
        if (dataSource == null) {
            throw new IllegalStateException("DataSource is not set");
        }
    }

}
