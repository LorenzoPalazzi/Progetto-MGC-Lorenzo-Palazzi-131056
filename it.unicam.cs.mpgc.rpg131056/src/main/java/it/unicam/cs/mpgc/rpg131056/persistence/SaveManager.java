package it.unicam.cs.mpgc.rpg131056.persistence;

import it.unicam.cs.mpgc.rpg131056.controller.GameState;

public interface SaveManager {
    void save(GameState state, String filePath);
    GameState load(String filePath);
}
