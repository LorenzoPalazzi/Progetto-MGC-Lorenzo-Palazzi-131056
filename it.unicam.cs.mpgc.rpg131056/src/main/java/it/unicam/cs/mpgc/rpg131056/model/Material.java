package it.unicam.cs.mpgc.rpg131056.model;

public class Material extends AbstractItem {
    public Material(String name, double weight) {
        super(name, weight);
    }

    @Override
    public ItemType getType() {
        return ItemType.MATERIAL;
    }
}
