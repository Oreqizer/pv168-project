package boris.configurator.backend;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Build build = (Build) o;

        if (id != null ? !id.equals(build.id) : build.id != null) return false;
        if (pc != null ? !pc.equals(build.pc) : build.pc != null) return false;
        return components != null ? components.equals(build.components) : build.components == null;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (pc != null ? pc.hashCode() : 0);
        result = 31 * result + (components != null ? components.hashCode() : 0);
        return result;
    }

}
