package it.unicam.cs.mpgc.rpg131056.actions;

import it.unicam.cs.mpgc.rpg131056.model.Inventory;
import it.unicam.cs.mpgc.rpg131056.model.Material;
import it.unicam.cs.mpgc.rpg131056.model.Player;

public class Scavenge implements Action {

    @Override
    public void execute(Player player, Inventory inventory){
        if (player == null || inventory == null) {
            throw new IllegalArgumentException("Giocatore e inventario non possono essere nulli.");
        }
        if (player.getEnergy() < 20) {
            throw new IllegalStateException("Energia insufficiente per pescare! (almeno 20)");
        }
        player.reduceEnergy(20);
        Material legna = new Material("Legna da ardere", 1.2);
        inventory.addItem(legna);
    }

    public String getName() {
        return "Raccogli risorse";
    }
}
