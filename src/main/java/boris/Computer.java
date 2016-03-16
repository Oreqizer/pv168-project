package boris;

/**
 * Created by oreqizer on 16/03/16.
 */
public class Computer {

    private final long id;
    private final int slots;
    private final int cooling;
    private final int price;

    public Computer(long id, int slots, int cooling, int price) {
        this.id = id;
        this.slots = slots;
        this.cooling = cooling;
        this.price = price;
    }

    public long getId() {
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
