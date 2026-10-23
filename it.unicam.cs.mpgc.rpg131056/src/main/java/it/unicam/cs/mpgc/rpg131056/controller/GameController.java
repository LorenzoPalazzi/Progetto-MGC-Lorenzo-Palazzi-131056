package it.unicam.cs.mpgc.rpg131056.controller;

import it.unicam.cs.mpgc.rpg131056.actions.Action;
import it.unicam.cs.mpgc.rpg131056.model.Weather;

public class GameController {
    private GameState gameState;

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
        gameState.advanceDay(); //faccio aumentare il giorno (cioè cambio di turno)
        gameState.getPlayer().consumeResorces(10, 10, gameState.getCurrentWeather());
        Weather [] weather = Weather.values();

        //in questo modo riesco a gestire casualmente il meteo
        Weather nextWeather = weather [(int) (Math.random() * weather.length)];
        gameState.setCurrentWeather(nextWeather);
    }

    public boolean checkGameOver() {
        //uso il metodo già creato per verificare se il personaggio è ancora vivo
        return !gameState.getPlayer().isAlive();
    }

    public GameState getGameState() { return gameState; }

}
