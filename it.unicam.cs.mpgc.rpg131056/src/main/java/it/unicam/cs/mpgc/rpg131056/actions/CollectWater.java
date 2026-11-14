package it.unicam.cs.mpgc.rpg131056.actions;

import it.unicam.cs.mpgc.rpg131056.controller.GameState;
import it.unicam.cs.mpgc.rpg131056.model.Inventory;
import it.unicam.cs.mpgc.rpg131056.model.Player;
import it.unicam.cs.mpgc.rpg131056.model.Water;
import it.unicam.cs.mpgc.rpg131056.model.Weather;

public class CollectWater implements Action {
    private final GameState gameState;

    public CollectWater(GameState gameState) {
        if (gameState == null) {
            throw new IllegalArgumentException("Il GameState non può essere nullo.");
        }
        this.gameState = gameState;
    }

    @Override
    public void execute(Player player, Inventory inventory) {
        if (player == null || inventory == null) {
            throw new IllegalArgumentException("Player e Inventory non possono essere nulli.");
        }
        Weather weather = gameState.getCurrentWeather();
        if (weather == Weather.RAINY || weather == Weather.STORMY) {
            player.reduceEnergy(5);
            inventory.addItem(new Water("Acqua", 0.5, 25));
            inventory.addItem(new Water("Acqua", 0.5, 25));
        } else {
            // Giorno di sole (SUNNY): resa minore e più fatica, ma acqua garantita
            player.reduceEnergy(15);
            inventory.addItem(new Water("Acqua", 0.5, 25));
        }
    }

    @Override
    public String getName() { return "Raccogli Acqua"; }
}