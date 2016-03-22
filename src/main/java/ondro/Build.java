package ondro;

import java.util.List;

/**
 * Created by zeman on 16/03/16.
 */
public final class Build {

    private final Long id;
    private final Computer computer;
    private final List<Component> components;

    public Build(Computer computer, List<Component> components) {
        this.id = null;
        this.computer = computer;
        this.components = components;
    }

    public Build(Long id,Computer computer, List<Component> components) {
        this.id = id;
        this.computer = computer;
        this.components = components;
    }

    public Long getId() {
        return id;
    }

    public Computer getComputer() {
        return computer;
    }

    public List<Component> getComponents() {
        return components;
    }

    public Build setId(Long id){
        return new Build(id, computer,components);
    }

    public Build setComputer(Computer computer){
        return new Build(computer,components);
    }

    public Build setComponents(List<Component> components){
        return new Build(computer,components);
    }

}
