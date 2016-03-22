package ondro;

/**
 * Created by zeman on 16/03/16.
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
    public Computer(Long id,int slots, int cooling, int price) {
        this.id = id;
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

    public Computer setId(Long id){
        return new Computer(id,slots,cooling,price);
    }

    public Computer setSlots(int slots){
        return new Computer(id,slots,cooling,price);
    }

    public Computer setCooling(int cooling){
        return new Computer(id,slots,cooling,price);
    }

    public Computer setPrice(int price){
        return new Computer(id,slots,cooling,price);
    }

}
