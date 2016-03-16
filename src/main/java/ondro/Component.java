package ondro;

/**
 * Created by zeman on 16/03/16.
 */
public class Component {

    private final Long id;
    private final String name;
    private final int heat;
    private final int price;
    private final int energy;

    public Component(String name, int heat, int price, int energy) {
        this.id = null;
        this.name = name;
        this.heat = heat;
        this.price = price;
        this.energy = energy;
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

}
