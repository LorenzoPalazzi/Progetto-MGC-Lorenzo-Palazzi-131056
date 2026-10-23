package it.unicam.cs.mpgc.rpg131056.controller;

import it.unicam.cs.mpgc.rpg131056.model.Inventory;
import it.unicam.cs.mpgc.rpg131056.model.Player;
import it.unicam.cs.mpgc.rpg131056.model.Weather;

//contiene lo stato attuale del gioco
public class GameState {
    private Player player;
    private Inventory inventory;
    private Weather currentWeather;
    private int currentDay;

    public GameState () {
        this.player = new Player();
        this.inventory = new Inventory();
        this.currentDay = 1;
        this.currentWeather = Weather.SUNNY;
    }

    public GameState(Player player, Inventory inventory, int currentDay, Weather currentWeather) {
        if (player == null || inventory == null || currentWeather == null) {
            throw new IllegalArgumentException("I parametri del GameState non possono essere nulli.");
        }
        if (currentDay < 1) {
            throw new IllegalArgumentException("Il giorno di gioco deve essere almeno 1.");
        }

        this.player = player;
        this.inventory = inventory;
        this.currentWeather = currentWeather;
        this.currentDay = currentDay;
    }
    public Player getPlayer() { return player; }
    public void setPlayer(Player player) { this.player = player; }
    public Inventory getInventory() { return inventory;}
    public void setInventory(Inventory inventory) { this.inventory = inventory; }
    public Weather getCurrentWeather() { return currentWeather; }
    public void setCurrentWeather(Weather currentWeather) { this.currentWeather = currentWeather; }
    public int getCurrentDay() { return currentDay; }
    public void setCurrentDay(int currentDay) { this.currentDay = currentDay; }
    public void advanceDay() { this.currentDay++; }
}