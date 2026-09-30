package it.unicam.cs.mpgc.rpg131056.model;

import static java.lang.Math.clamp;

public class Player {
    private int health;
    private int hunger;
    private int thirst;
    private int energy;

    public Player(int health, int hunger, int thirst, int energy) {
        if (health < 0 || health > 100) {
            throw new IllegalArgumentException("La salute deve essere compresa tra 0 e 100.");
        }
        if (hunger < 0 || hunger > 100) {
            throw new IllegalArgumentException("La fame deve essere compresa tra 0 e 100.");
        }
        if (thirst < 0 || thirst > 100) {
            throw new IllegalArgumentException("La sete deve essere compresa tra 0 e 100.");
        }
        if (energy < 0 || energy > 100) {
            throw new IllegalArgumentException("L'energia deve essere compresa tra 0 e 100.");
        }

        this.health = health;
        this.hunger = hunger;
        this.thirst = thirst;
        this.energy = energy;
    }

    public Player() {
        this(100, 0, 0, 100);
    }

    public boolean isAlive() {
        return health > 0;
    }

    public void consumeResorces(int hungerAmount, int thirstAmount, Weather weather) {
        if (hungerAmount >= 100 || thirstAmount >= 100) {
            this.health = 0;
            return;
        }
        if (this.health < 0) { this.health = 0; }
        this.hunger = clamp(this.hunger + hungerAmount, 0, 100); //clamp serve per limitare i valori tra 0-100
        double thirstMultiplier = 1.0;
        if (weather == Weather.SUNNY) {
            thirstMultiplier = 1.5;
        } else if (weather == Weather.RAINY) {
            thirstMultiplier = 0.5;
        }
        int calculatedThirst = (int) (thirstAmount * thirstMultiplier);
        this.thirst = clamp(this.thirst + calculatedThirst, 0, 100);
        updateStats();
    }

    public void updateStats() {
        if (this.hunger > 80) {
            this.health = clamp(this.health - 5, 0, 100);
        }
        if (this.thirst > 80) {
            this.health = clamp(this.health - 15, 0, 100);
        }
    }

    public void addEnergy(int amount) {
        this.energy = clamp(this.energy + amount, 0, 100);
    }

    public void reduceEnergy(int amount) {
        this.energy = clamp(this.energy - amount, 0, 100);
    }

    public void reduceHunger(int amount) {
        this.hunger = clamp(this.hunger - amount, 0, 100);
    }

    public void reduceThirst(int amount) {
        this.thirst = clamp(this.thirst - amount, 0, 100);
    }

    public int getHealth() { return health; }
    public int getHunger() { return hunger; }
    public int getThirst() { return thirst; }
    public int getEnergy() { return energy; }
}