package it.unicam.cs.mpgc.rpg131056.actions;

import it.unicam.cs.mpgc.rpg131056.model.Inventory;
import it.unicam.cs.mpgc.rpg131056.model.Item;
import it.unicam.cs.mpgc.rpg131056.model.Player;
import it.unicam.cs.mpgc.rpg131056.model.Food;

public class Eat implements Action {

    @Override
    public void execute(Player player, Inventory inventory){
        if (player == null || inventory == null) {
            throw new IllegalArgumentException("Giocatore e inventario non possono essere nulli.");
        }

        Food ciboTrovato = null;
        for (Item item : inventory.getItems()) {
            if (item instanceof Food) {
                ciboTrovato = (Food) item;
                break;
            }
        }
        if (ciboTrovato == null) {
            throw new IllegalStateException("Non è stato trovato nessun cibo nell'inventario");
        }
        player.reduceHunger(ciboTrovato.getHungerRestore());
        inventory.removeItem(ciboTrovato);
    }

    @Override
    public String getName() {
        return "Mangia";
    }
}