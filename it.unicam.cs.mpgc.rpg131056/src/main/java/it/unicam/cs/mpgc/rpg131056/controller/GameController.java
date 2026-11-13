package it.unicam.cs.mpgc.rpg131056.controller;

import it.unicam.cs.mpgc.rpg131056.actions.Action;
import it.unicam.cs.mpgc.rpg131056.model.Weather;

public class GameController {
    private GameState gameState;
    private static final int DAILY_HUNGER_INCREASE = 12; // Consumo passivo basso
    private static final int DAILY_THIRST_INCREASE = 15; // Consumo passivo basso

    //per iniziare una nuova partita
    public GameController() {
        this.gameState = new GameState();
    }

    //per riprendere una partita salvata
    public GameController(GameState gameState) {
        if (gameState == null) {
            throw new IllegalArgumentException("Lo stato del gioco non può essere nullo");
        }
        this.gameState = gameState;
    }

    public void performAction(Action action) {
        if (action == null) {
            throw new IllegalArgumentException("L'azione selezionata non può essere nulla");
        }
        action.execute(gameState.getPlayer(), gameState.getInventory());
    }

    public void nextTurn() {
        var player = gameState.getPlayer();

        int hungerIncrease = DAILY_HUNGER_INCREASE;
        int thirstIncrease = DAILY_THIRST_INCREASE;

        gameState.advanceDay(); //faccio aumentare il giorno (cioè cambio di turno)
        if (gameState.getCurrentWeather() == Weather.SUNNY) {
            thirstIncrease += 10;
        } else if (gameState.getCurrentWeather() == Weather.STORMY) {
            hungerIncrease += 10;
        }

        player.increaseHunger(hungerIncrease);
        player.increaseThirst(thirstIncrease);

        Weather[] weather = Weather.values();
        //in questo modo riesco a gestire casualmente il meteo
        Weather nextWeather = weather [(int) (Math.random() * weather.length)];
        gameState.setCurrentWeather(nextWeather);
    }

    public boolean checkGameOver() {
        //uso il metodo già creato per verificare se il personaggio è ancora vivo
        return !gameState.getPlayer().isAlive();
    }

    public GameState getGameState() { return gameState; }
    
    public GameState setGameState(GameState gameState) {
        if (gameState == null) {
            throw new IllegalArgumentException("Lo stato del gioco non può essere nullo");
        }
        this.gameState = gameState;
        return this.gameState;
    }
}