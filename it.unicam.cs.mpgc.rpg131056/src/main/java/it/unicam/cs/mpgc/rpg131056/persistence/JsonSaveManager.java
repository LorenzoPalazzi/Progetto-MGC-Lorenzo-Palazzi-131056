package it.unicam.cs.mpgc.rpg131056.persistence;

import it.unicam.cs.mpgc.rpg131056.controller.GameState;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class JsonSaveManager implements SaveManager {
    private final Gson gson;

    public JsonSaveManager () {
        this.gson = new GsonBuilder().setPrettyPrinting().create();
    }

    @Override
    public void save(GameState state, String filePath) {
        if (state == null || filePath == null) {
            throw new IllegalArgumentException("GameState e filePath non possono essere null.");
        }
        try (FileWriter writer = new FileWriter(filePath)) {
            gson.toJson(state, writer);
            System.out.println("Partita salvata con successo in: " + filePath);
        } catch (IOException e) {
            System.err.println("Errore durante il salvataggio: " + e.getMessage());
        }
    }

    @Override
    public GameState load(String filePath) {
        if (filePath == null) {
            throw new IllegalArgumentException("Il filePath non può essere null.");
        }

        try (FileReader reader = new FileReader(filePath)) {
            GameState loadedState = gson.fromJson(reader, GameState.class);
            System.out.println("Partita caricata con successo da: " + filePath);
            return loadedState;
        } catch (IOException e) {
            System.err.println("Errore nel caricamento del file: " + e.getMessage());
            return null;
        }
    }
}
