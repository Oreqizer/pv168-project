package boris;

/**
 * Created by oreqizer on 16/03/16.
 */
public class Component {

    private final long id;
    private final String name;
    private final int heat;
    private final int price;
    private final int energy;

    public Component(long id, String name, int heat, int price, int energy) {
        this.id = id;
        this.name = name;
        this.heat = heat;
        this.price = price;
        this.energy = energy;
    }

    public long getId() {
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

}
