package it.unicam.cs.mpgc.rpg131056.actions;

import it.unicam.cs.mpgc.rpg131056.model.Food;
import it.unicam.cs.mpgc.rpg131056.model.Inventory;
import it.unicam.cs.mpgc.rpg131056.model.Item;
import it.unicam.cs.mpgc.rpg131056.model.Player;

public class CookingFish implements Action{
    @Override
    public void execute(Player player, Inventory inventory) {
        if (player == null || inventory == null) {
            throw new IllegalArgumentException("Giocatore e inventario non possono essere nulli.");
        }

        Item legna = null;
        Item pesceCrudo = null;

        for (Item item : inventory.getItems()) {
            if ("Legna".equals(item.getName()) && legna == null) {
                legna = item;
            } else if ("Pesce crudo".equals(item.getName()) && pesceCrudo == null) {
                pesceCrudo = item;
            }
        }
        
        if (legna == null || pesceCrudo == null) {
            throw new IllegalStateException("Per cucinare servono 1 Legna e 1 Pesce crudo.");
        }

        inventory.removeItem(legna);
        inventory.removeItem(pesceCrudo);

        inventory.addItem(new Food("Pesce cotto", 0.5, 40));
    }

    @Override
    public String getName() { return "Cuoci Pesce"; }
}