package boris;

import java.util.List;

/**
 * Created by oreqizer on 16/03/16.
 */
public final class Build {

    private final long id;
    private final Computer pc;
    private final List<Component> components;

    public Build(long id, Computer pc, List<Component> components) {
        this.id = id;
        this.pc = pc;
        this.components = components;
    }

    public long getId() {
        return id;
    }

    public Computer getPc() {
        return pc;
    }

    public List<Component> getComponents() {
        return components;
    }

}
