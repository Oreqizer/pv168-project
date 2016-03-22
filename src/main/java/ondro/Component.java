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

    public Component setId(Long id){
        return new Component(name,heat,price,energy);
    }

    public Component setName(String name){
        return new Component(name,this.heat,this.price,this.energy);
    }

    public Component setHeat(int heat){
        return new Component(name,heat,this.price,this.energy);
    }

    public Component setPrice(int price){
        return new Component(name,this.heat,price,this.energy);
    }

    public Component setEnergy(int energy){
        return new Component(name,this.heat,this.price,energy);
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

    public Component(Long id,String name, int heat, int price, int energy) {
        this.id = id;
        this.name = name;
        this.heat = heat;
        this.price = price;
        this.energy = energy;

    }
}
