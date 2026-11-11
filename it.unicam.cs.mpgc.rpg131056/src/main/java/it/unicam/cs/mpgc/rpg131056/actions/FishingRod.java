package it.unicam.cs.mpgc.rpg131056.actions;
import java.util.ArrayList;
import java.util.List;

import it.unicam.cs.mpgc.rpg131056.model.Inventory;
import it.unicam.cs.mpgc.rpg131056.model.Item;
import it.unicam.cs.mpgc.rpg131056.model.Material;
import it.unicam.cs.mpgc.rpg131056.model.Player;

public class FishingRod implements Action {

    @Override
    public void execute(Player player, Inventory inventory) {
        List<Item> legnaTrovata = new ArrayList<>();
        Item reteTrovata = null;

        // Ricerca dei materiali necessari nell'inventario
        for (Item item : inventory.getItems()) {
            if (item.getName().equals("Legna") && legnaTrovata.size() < 2) {
                legnaTrovata.add(item);
            } else if (item.getName().equals("Rete") && reteTrovata == null) {
                reteTrovata = item;
            }
        }

        // Se le risorse non bastano, l'azione fallisce
        if (legnaTrovata.size() < 2 || reteTrovata == null) {
            throw new IllegalStateException("Risorse insufficienti per la canna da pesca! Servono 2 Legna e 1 Rete.");
        }

        // Consumo dei materiali e creazione della Canna
        for (Item legna : legnaTrovata) {
            inventory.removeItem(legna);
        }
        inventory.removeItem(reteTrovata);

        inventory.addItem(new Material("Canna da Pesca", 2.0));
    }

    @Override
    public String getName() { return "Crea Canna da Pesca"; }
}