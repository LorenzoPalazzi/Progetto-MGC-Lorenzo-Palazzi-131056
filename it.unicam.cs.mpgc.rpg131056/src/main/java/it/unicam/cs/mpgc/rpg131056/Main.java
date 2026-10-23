package it.unicam.cs.mpgc.rpg131056;

import it.unicam.cs.mpgc.rpg131056.controller.GameController;
import it.unicam.cs.mpgc.rpg131056.controller.GameState;
import it.unicam.cs.mpgc.rpg131056.persistence.JsonSaveManager;
import it.unicam.cs.mpgc.rpg131056.persistence.SaveManager;

public class Main {
        public static void main(String[] args) {
            System.out.println("==========================================");
            System.out.println("   AVVIO TEST MOTORE RPG E PERSISTENZA   ");
            System.out.println("==========================================");

            // 1. Inizializzazione nuova partita
            GameController controller = new GameController();
            System.out.println("\n[1] Nuova partita creata:");
            stampaStatoGiocatore(controller);

            // 2. Avanzamento di un turno (test nextTurn, consumo risorse e meteo)
            System.out.println("\n[2] Avanzamento al turno successivo...");
            // Simulo 10 turni senza mangiare né bere
            for (int i = 0; i < 10; i++) {
                controller.nextTurn();
            }
            stampaStatoGiocatore(controller);

            // 3. Test del salvataggio in JSON
            System.out.println("\n[3] Salvataggio partita su file...");
            SaveManager saveManager = new JsonSaveManager();
            String fileSalvataggio = "test_savegame.json";
            saveManager.save(controller.getGameState(), fileSalvataggio);

            // 4. Test del caricamento da JSON
            System.out.println("\n[4] Caricamento nuova istanza di gioco dal file salvato...");
            GameState statoCaricato = saveManager.load(fileSalvataggio);

            if (statoCaricato != null) {
                GameController controllerCaricato = new GameController(statoCaricato);
                System.out.println("\n[5] Stato del gioco ripristinato con successo:");
                stampaStatoGiocatore(controllerCaricato);
                System.out.println("\n=== TEST COMPLETATO CON SUCCESSO! ===");
            } else {
                System.err.println("\n!!! ERRORE NEL CARICAMENTO DEL SALVATAGGIO !!!");
            }
        }

    private static void stampaStatoGiocatore(GameController controller) {
        GameState state = controller.getGameState();
        System.out.println(" - Giorno attuale: " + state.getCurrentDay());
        System.out.println(" - Meteo attuale:  " + state.getCurrentWeather());
        System.out.println(" - Fame Player:    " + state.getPlayer().getHunger()); // <-- AGGIUNGI QUESTO
        System.out.println(" - Sete Player:    " + state.getPlayer().getThirst()); // <-- AGGIUNGI QUESTO
        System.out.println(" - Salute Player:  " + state.getPlayer().getHealth());
        System.out.println(" - Giocatore Vivo: " + state.getPlayer().isAlive());
    }
}