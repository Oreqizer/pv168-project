package boris.configurator.backend;

/**
 * Created by oreqizer on 16/03/16.
 */
public class Component {

    private final Long id;
    private final boolean free;
    private final String name;
    private final int heat;
    private final int price;
    private final int energy;

    public Component(String name, int heat, int price, int energy) {
        this.id = null;
        this.free = true;
        this.name = name;
        this.heat = heat;
        this.price = price;
        this.energy = energy;
    }

    public Component(
            Long id, boolean free, String name, int heat, int price, int energy
    ) {
        this.id = id;
        this.free = free;
        this.name = name;
        this.heat = heat;
        this.price = price;
        this.energy = energy;
    }

    public boolean isFree() {
        return free;
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
        return new Component(id, free, name, heat, price, energy);
    }

    public Component setFree(boolean free) {
        return new Component(id, free, name, heat, price, energy);
    }

    public Component setName(String name) {
        return new Component(id, free, name, heat, price, energy);
    }

    public Component setHeat(int heat) {
        return new Component(id, free, name, heat, price, energy);
    }

    public Component setPrice(int price) {
        return new Component(id, free, name, heat, price, energy);
    }

    public Component setEnergy(int energy) {
        return new Component(id, free, name, heat, price, energy);
    }

}
