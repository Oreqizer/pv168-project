package configurator.component;

/**
 * Created by oreqizer on 16/03/16.
 */
public final class Component {

    private final Long id;
    private final Long pid;
    private final String name;
    private final int heat;
    private final int price;
    private final int energy;

    public Component(String name, int heat, int price, int energy) {
        this.id = null;
        this.pid = null;
        this.name = name;
        this.heat = heat;
        this.price = price;
        this.energy = energy;
    }


    public Component(
            Long id, Long pid, String name, int heat, int price, int energy
    ) {
        this.id = id;
        this.pid = pid;
        this.name = name;
        this.heat = heat;
        this.price = price;
        this.energy = energy;
    }

    public Long getPid() {
        return pid;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getHeat() {
        return heat;
    }

    public int getPrice() {
        return price;
    }

    public int getEnergy() {
        return energy;
    }

    public Component setId(Long id) {
        return new Component(id, pid, name, heat, price, energy);
    }

    public Component setPid(Long pid) {
        return new Component(id, pid, name, heat, price, energy);
    }

    public Component setName(String name) {
        return new Component(id, pid, name, heat, price, energy);
    }

    public Component setHeat(int heat) {
        return new Component(id, pid, name, heat, price, energy);
    }

    public Component setPrice(int price) {
        return new Component(id, pid, name, heat, price, energy);
    }

    public Component setEnergy(int energy) {
        return new Component(id, pid, name, heat, price, energy);
    }

    @Override
    public String toString() {
        return "Component{" +
                "id=" + id +
                ", pid=" + pid +
                ", name='" + name + '\'' +
                ", heat=" + heat +
                ", price=" + price +
                ", energy=" + energy +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Component component = (Component) o;

        if (pid != component.pid) return false;
        if (heat != component.heat) return false;
        if (price != component.price) return false;
        if (energy != component.energy) return false;
        if (id != null ? !id.equals(component.id) : component.id != null) return false;
        return name != null ? name.equals(component.name) : component.name == null;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result +  (pid !=null ? id.hashCode() :0)  ;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + heat;
        result = 31 * result + price;
        result = 31 * result + energy;
        return result;
    }

}
