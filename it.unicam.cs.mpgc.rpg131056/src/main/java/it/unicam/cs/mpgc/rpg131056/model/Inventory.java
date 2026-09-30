package it.unicam.cs.mpgc.rpg131056.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

public class Inventory {
    private final List<Item> items;

    public Inventory() {
        this.items = new ArrayList<> ();
    }

    public void addItem(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("L'elemento da inserire è nullo");
        }
        items.add(item);
    }
    public void removeItem(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("L'elemento da rimuovere è nullo");
        }
        items.remove(item);
    }
    public boolean hasItem(String name) {
        if (name == null || name.isBlank()) {
            return false;
        }
        for (Item item : items) {
            if(item.getName().equalsIgnoreCase(name)){
                return true;
            }
        }
        return false;
    }
    public List<Item> getItems() {
        return new ArrayList<>(items);
    }
}
