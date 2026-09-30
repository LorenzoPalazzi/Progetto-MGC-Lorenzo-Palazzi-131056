package it.unicam.cs.mpgc.rpg131056.model;

public class Water extends AbstractItem{
    private final int thirstRestore;

    public Water(String name, double weight, int thirstRestore) {
        super(name, weight);
        if (thirstRestore <= 0) {
            throw new IllegalArgumentException("Il valore di ripristino della sete deve essere maggiore di zero");
        }
        this.thirstRestore = thirstRestore;
    }

    @Override
    public ItemType getType() {
        return ItemType.WATER;
    }

    public int getThirstRestore() {
        return thirstRestore;
    }
}
