package it.unicam.cs.mpgc.rpg131056.actions;

import java.util.ArrayList;
import java.util.List;

import it.unicam.cs.mpgc.rpg131056.model.Inventory;
import it.unicam.cs.mpgc.rpg131056.model.Item;
import it.unicam.cs.mpgc.rpg131056.model.Material;
import it.unicam.cs.mpgc.rpg131056.model.Player;

public class BuildOars implements Action {

    @Override
    public void execute(Player player, Inventory inventory) {
        if (player == null || inventory == null) {
            throw new IllegalArgumentException("Player e Inventory non possono essere nulli.");
        }

        List<Item> woodToRemove = new ArrayList<>();
        for (Item item : inventory.getItems()) {
            if ("Legna".equalsIgnoreCase(item.getName())) {
                woodToRemove.add(item);
                if (woodToRemove.size() == 6) {
                    break;
                }
            }
        }

        if (woodToRemove.size() < 4) {
            throw new IllegalStateException("Servono 4 unità di Legna per costruire i remi.");
        }
        for (Item wood : woodToRemove) {
            inventory.removeItem(wood);
        }
        inventory.addItem(new Material("Remi", 3.0));
    }

    @Override
    public String getName() { return "Costruisci Remi";}
}