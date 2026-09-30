package it.unicam.cs.mpgc.rpg131056.model;

public abstract class AbstractItem implements Item {
    private final String name;
    private final double weight;

    public AbstractItem(String name, double weight) {
        if(name == null || name.isBlank()) {
            throw new IllegalArgumentException("Il nome dell'oggetti non può essere nullo o vuoto");
        }
        if (weight < 0) {
            throw new IllegalArgumentException("Il peso dell'oggetto non può essere negativo");
        }
        this.name = name;
        this.weight = weight;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public double getWeight() {
        return weight;
    }
}
