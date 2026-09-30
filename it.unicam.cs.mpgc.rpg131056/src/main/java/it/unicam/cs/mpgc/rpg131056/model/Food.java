package it.unicam.cs.mpgc.rpg131056.model;

public class Food extends AbstractItem {
    private final int hungerRestore;

    public Food (String name, double weight, int hungerRestore) {
        super(name, weight);
        if (hungerRestore <= 0) {
            throw new IllegalArgumentException("Il valore di ripristino della fame deve essere maggiore di zero");
        }
        this.hungerRestore = hungerRestore;
    }

    @Override
    public ItemType getType() {
        return ItemType.FOOD;
    }

    public int getHungerRestore() {
        return hungerRestore;
    }
}

