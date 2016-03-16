package ondro;

/**
 * Created by oreqizer on 16/03/16.
 */
public class Computer {

    private final Long id;
    private final int slots;
    private final int cooling;
    private final int price;

    public Computer(int slots, int cooling, int price) {
        this.id = null;
        this.slots = slots;
        this.cooling = cooling;
        this.price = price;
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

}
