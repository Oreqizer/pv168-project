package leet.configurator.backend;

/**
 * Created by oreqizer on 16/03/16.
 */
public class Computer {

    private final Long id;
    private final boolean free;
    private final int slots;
    private final int cooling;
    private final int price;

    public Computer(int slots, int cooling, int price) {
        this.id = null;
        this.free = true;
        this.slots = slots;
        this.cooling = cooling;
        this.price = price;
    }

    public Computer(
            Long id, boolean free, int slots, int cooling, int price
    ) {
        this.id = id;
        this.free = free;
        this.slots = slots;
        this.cooling = cooling;
        this.price = price;
    }
    
    public boolean isFree() {
        return free;
    }

    public Long getId() {
        return id;
    }

    public int getSlots() {
        return slots;
    }

    public int getCooling() {
        return cooling;
    }

    public int getPrice() {
        return price;
    }
    
    public Computer setId(Long id) {
        return new Computer(id, free, slots, cooling, price);
    }
    
    public Computer setFree(boolean free) {
        return new Computer(id, free, slots, cooling, price);
    }
    
    public Computer setSlots(int slots) {
        return new Computer(id, free, slots, cooling, price);
    }
    
    public Computer setCooling(int cooling) {
        return new Computer(id, free, slots, cooling, price);
    }
    
    public Computer setPrice(int price) {
        return new Computer(id, free, slots, cooling, price);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Computer computer = (Computer) o;

        if (free != computer.free) return false;
        if (slots != computer.slots) return false;
        if (cooling != computer.cooling) return false;
        if (price != computer.price) return false;
        return id != null ? id.equals(computer.id) : computer.id == null;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (free ? 1 : 0);
        result = 31 * result + slots;
        result = 31 * result + cooling;
        result = 31 * result + price;
        return result;
    }

}
