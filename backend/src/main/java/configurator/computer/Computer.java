package configurator.computer;

import configurator.component.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by oreqizer on 16/03/16.
 */
public final class Computer {

    private final Long id;
    private final List<Component> components;
    private final int slots;
    private final int cooling;
    private final int price;
    private final int energy;

    public Computer(int slots) {
        this(slots, 0, 0, 0);
    }

    public Computer(int slots, int cooling, int price, int energy) {
        this.energy = energy;
        this.id = null;
        this.components = new ArrayList<>();
        this.slots = slots;
        this.cooling = cooling;
        this.price = price;
    }

    public Computer(Long id, List<Component> components, int slots, int cooling, int price, int energy) {
        this.id = id;
        if (components == null) {
            this.components = new ArrayList<>();
        } else {
            this.components = components;
        }
        this.slots = slots;
        this.cooling = cooling;
        this.price = price;
        this.energy = energy;
    }

    public Long getId() {
        return id;
    }

    public int getEnergy() {
        return energy;
    }

    public int getSlots() {
        return slots;
    }

    public List<Component> getComponents() {
        return components;
    }

    public int getCooling() {
        return cooling;
    }

    public int getPrice() {
        return price;
    }
    
    public Computer setId(Long id) {
        return new Computer(id, components, slots, cooling, price, energy);
    }
    
    public Computer setComponents(List<Component> components) {
        return new Computer(id, components, slots, cooling, price, energy);
    }

    public Computer setEnergy(int energy) {
        return new Computer(id, components, slots, cooling, price, energy);
    }

    public Computer setSlots(int slots) {
        return new Computer(id, components, slots, cooling, price, energy);
    }
    
    public Computer setCooling(int cooling) {
        return new Computer(id, components, slots, cooling, price, energy);
    }
    
    public Computer setPrice(int price) {
        return new Computer(id, components, slots, cooling, price, energy);
    }

    @Override
    public String toString() {
        return "Computer{" +
                "id=" + id +
                ", slots=" + slots +
                ", cooling=" + cooling +
                ", price=" + price +
                ", energy=" + energy +
                ", components=" + components.size() +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Computer computer = (Computer) o;

        if (slots != computer.slots) return false;
        if (cooling != computer.cooling) return false;
        if (price != computer.price) return false;
        if (energy != computer.energy) return false;
        return id != null ? id.equals(computer.id) : computer.id == null;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + slots;
        result = 31 * result + cooling;
        result = 31 * result + price;
        result = 31 * result + energy;
        return result;
    }
}
