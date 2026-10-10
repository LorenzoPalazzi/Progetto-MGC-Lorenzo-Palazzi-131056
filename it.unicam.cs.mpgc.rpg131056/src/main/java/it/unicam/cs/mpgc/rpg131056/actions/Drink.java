package it.unicam.cs.mpgc.rpg131056.actions;

import it.unicam.cs.mpgc.rpg131056.model.Inventory;
import it.unicam.cs.mpgc.rpg131056.model.Item;
import it.unicam.cs.mpgc.rpg131056.model.Player;
import it.unicam.cs.mpgc.rpg131056.model.Water;

public class Drink implements Action {

    @Override
    public void execute(Player player, Inventory inventory){
        if (player == null || inventory == null) {
            throw new IllegalArgumentException("Giocatore e inventario non possono essere nulli.");
        }

        Water acquaTrovata = null;
        for (Item item : inventory.getItems()) {
            if (item instanceof Water) {
                acquaTrovata = (Water) item;
                break;
            }
        }
        if (acquaTrovata == null) {
            throw new IllegalStateException("Non è stato trovato nessun oggetto acqua nell'inventario");
        }
        player.reduceThirst(acquaTrovata.getThirstRestore());
        inventory.removeItem(acquaTrovata);
    }

    public String getName() {
        return "Bevi";
    }
}
