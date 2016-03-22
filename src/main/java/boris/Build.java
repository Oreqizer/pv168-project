package boris;

import java.util.List;

/**
 * Created by oreqizer on 16/03/16.
 */
public final class Build {

    private final Long id;
    private final Computer pc;
    private final List<Component> components;

    public Build(Computer pc, List<Component> components) {
        this.id = null;
        this.pc = pc;
        this.components = components;
    }

    public Build(Long id, Computer pc, List<Component> components) {
        this.id = id;
        this.pc = pc;
        this.components = components;
    }

    public Long getId() {
        return id;
    }

    public Computer getPc() {
        return pc;
    }

    public List<Component> getComponents() {
        return components;
    }

    public Build setId(Long id) {
        return new Build(id, pc, components);
    }

    public Build setComputer(Computer pc) {
        return new Build(id, pc, components);
    }

    public Build setComponents(List<Component> components) {
        return new Build(id, pc, components);
    }

}
