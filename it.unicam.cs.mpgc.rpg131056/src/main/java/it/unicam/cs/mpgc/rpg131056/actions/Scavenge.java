package it.unicam.cs.mpgc.rpg131056.actions;

import it.unicam.cs.mpgc.rpg131056.model.Inventory;
import it.unicam.cs.mpgc.rpg131056.model.Material;
import it.unicam.cs.mpgc.rpg131056.model.Player;
import it.unicam.cs.mpgc.rpg131056.model.Water;

import java.util.Random;

public class Scavenge implements Action {

    private final int ENERGY_COST = 20;
    private static final int WATER_RESTORE = 20;
    private final Random random = new Random();
    private String ultimoEsito = "";

    @Override
    public void execute(Player player, Inventory inventory){
        if (player == null || inventory == null) {
            throw new IllegalArgumentException("Giocatore e inventario non possono essere nulli.");
        }
        if (player.getEnergy() < ENERGY_COST) {
            throw new IllegalStateException("Energia insufficiente per raccogliere risorse! (almeno " + ENERGY_COST + " energia richiesta)");
        }
        player.reduceEnergy(ENERGY_COST);
        player.increaseHunger(2);
        player.increaseThirst(3);


        int esito = random.nextInt(4); //genera un numero casuale tra 0 e 2 (inclusi)
        switch (esito) {
            case 0 -> {
                Water water = new Water("Acqua", 0.5, WATER_RESTORE);
                inventory.addItem(water);
                ultimoEsito = "Hai trovato dell'acqua! (Ripristina " + WATER_RESTORE + " sete)";
            }
            case 1 -> {
                Material wood = new Material("Legna", 1.0);
                inventory.addItem(wood);
                ultimoEsito = "Hai trovato della legna! (Peso: 1.0)";
            }
            case 2 -> {
                Material fishingNet = new Material("Rete", 0.2);
                inventory.addItem(fishingNet);
                ultimoEsito = "Hai trovato della rete!";
            }
            default -> { ultimoEsito = "Non hai trovato nulla."; }
        }
    }

    public String getUltimoEsito() { return ultimoEsito; }

    @Override
    public String getName() { return "Raccogli risorse"; }
}
