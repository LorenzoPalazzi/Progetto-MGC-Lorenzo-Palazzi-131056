package it.unicam.cs.mpgc.rpg131056.actions;

import it.unicam.cs.mpgc.rpg131056.model.Food;
import it.unicam.cs.mpgc.rpg131056.model.Inventory;
import it.unicam.cs.mpgc.rpg131056.model.Player;

public class Fish implements Action {
    @Override
    public void execute(Player player, Inventory inventory) {
        if (player == null || inventory == null) {
            throw new IllegalArgumentException("Giocatore e inventario non possono essere nulli.");
        }
        if (player.getEnergy() < 15) {
            throw new IllegalStateException("Energia insufficiente per pescare! (almeno 15)");
        }
        player.reduceEnergy(15);
        Food pesce = new Food ("Pesce crudo", 0.5, 20);
        inventory.addItem(pesce);
    }

    public String getName() {
        return "Pesca";
    }
}