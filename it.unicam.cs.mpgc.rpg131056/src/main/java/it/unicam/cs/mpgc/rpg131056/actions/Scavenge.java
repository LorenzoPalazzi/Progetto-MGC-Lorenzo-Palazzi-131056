package it.unicam.cs.mpgc.rpg131056.actions;

import it.unicam.cs.mpgc.rpg131056.model.Inventory;
import it.unicam.cs.mpgc.rpg131056.model.Material;
import it.unicam.cs.mpgc.rpg131056.model.Player;

public class Scavenge implements Action {

    private final int ENERGY_COST = 20;

    @Override
    public void execute(Player player, Inventory inventory){
        if (player == null || inventory == null) {
            throw new IllegalArgumentException("Giocatore e inventario non possono essere nulli.");
        }
        if (player.getEnergy() < ENERGY_COST) {
            throw new IllegalStateException("Energia insufficiente per raccogliere risorse! (almeno " + ENERGY_COST + " energia richiesta)");
        }
        player.reduceEnergy(ENERGY_COST);
        Material legna = new Material("Legna da ardere", 1.2);
        inventory.addItem(legna);
    }

    @Override
    public String getName() {
        return "Raccogli risorse";
    }
}
