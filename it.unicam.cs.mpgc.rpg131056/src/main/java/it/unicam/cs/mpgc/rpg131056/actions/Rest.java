package it.unicam.cs.mpgc.rpg131056.actions;

import it.unicam.cs.mpgc.rpg131056.model.Inventory;
import it.unicam.cs.mpgc.rpg131056.model.Player;

public class Rest implements Action{ 

private final int ENERGY_GAIN = 25;

    @Override
    public void execute(Player player, Inventory inventory){
        if (player == null || inventory == null) {
            throw new IllegalArgumentException("Giocatore e inventario non possono essere nulli.");
        }
        player.addEnergy(ENERGY_GAIN);
    }

    @Override
    public String getName() {
        return "Riposa";
    }
}
