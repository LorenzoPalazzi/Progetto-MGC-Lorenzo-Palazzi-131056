package it.unicam.cs.mpgc.rpg131056.actions;

import it.unicam.cs.mpgc.rpg131056.model.Food;
import it.unicam.cs.mpgc.rpg131056.model.Inventory;
import it.unicam.cs.mpgc.rpg131056.model.Item;
import it.unicam.cs.mpgc.rpg131056.model.Player;

public class Fish implements Action {
    private final int ENERGY_COST = 15;

    @Override
    public void execute(Player player, Inventory inventory) {
        if (player == null || inventory == null) {
            throw new IllegalArgumentException("Giocatore e inventario non possono essere nulli.");
        }
        boolean haCanna = false;
        for (Item item : inventory.getItems()) { 
            if ("Canna da Pesca".equals(item.getName())){
                haCanna = true;
                break;
            }
        }
        if (!haCanna) {
            throw new IllegalStateException("Nessuna canna da pesca trovata. Costruiscila con 2 legna e 1 rete!");
        }
        if (player.getEnergy() < ENERGY_COST) {
            throw new IllegalStateException("Energia insufficiente per pescare! (almeno " + ENERGY_COST + " energia richiesta)");
        }
        player.reduceEnergy(ENERGY_COST);
        Food pesce = new Food ("Pesce crudo", 0.5, 20);
        inventory.addItem(pesce);
    }
    
    @Override
    public String getName() { return "Pesca"; }
}