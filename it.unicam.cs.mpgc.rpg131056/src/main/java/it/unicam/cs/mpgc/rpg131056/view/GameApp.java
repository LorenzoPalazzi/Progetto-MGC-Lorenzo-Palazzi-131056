package it.unicam.cs.mpgc.rpg131056.view;
import java.io.File;

import it.unicam.cs.mpgc.rpg131056.actions.Action;
import it.unicam.cs.mpgc.rpg131056.actions.BuildOars;
import it.unicam.cs.mpgc.rpg131056.actions.CollectWater;
import it.unicam.cs.mpgc.rpg131056.actions.CookingFish;
import it.unicam.cs.mpgc.rpg131056.actions.Drink;
import it.unicam.cs.mpgc.rpg131056.actions.Eat;
import it.unicam.cs.mpgc.rpg131056.actions.Fish;
import it.unicam.cs.mpgc.rpg131056.actions.FishingRod;
import it.unicam.cs.mpgc.rpg131056.actions.Rest;
import it.unicam.cs.mpgc.rpg131056.actions.Scavenge;
import it.unicam.cs.mpgc.rpg131056.controller.GameController;
import it.unicam.cs.mpgc.rpg131056.controller.GameState;
import it.unicam.cs.mpgc.rpg131056.persistence.JsonSaveManager;
import it.unicam.cs.mpgc.rpg131056.model.Modalita;
import it.unicam.cs.mpgc.rpg131056.persistence.SaveManager;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;

/* Avvia l'applicazione e collega tra loro le tre parti del gioco:
 * la view (interfaccia), il controller (regole) e il saveManager (salvataggio). */
public class GameApp extends Application {
    private static final String SAVE_FILE = "savegame.json";

    //la GUI si affida a questi attributi per il caricamento e il funzionamento del gioco
    private GameController controller;
    private SaveManager saveManager;
    private GameView view;
    private Modalita modalita;
    private StartMenuView startMenuView;
    private Scene scene;

    @Override
    public void start(Stage primaryStage) {
        // Inizializzazione del controller, della persistenza e dell'interfaccia
        this.controller = new GameController();
        this.saveManager = new JsonSaveManager();
        this.view = new GameView();
        this.startMenuView = new StartMenuView();

        collegaComandi();
        collegaMenuIniziale();

        this.scene = new Scene(startMenuView.getRoot(), 850, 600);
        showStartMenu();
        
        primaryStage.setTitle("Survival RPG - JavaFX");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

        private void showStartMenu() {
            startMenuView.setCanLoad(new File(SAVE_FILE).exists());
            scene.setRoot(startMenuView.getRoot());
    }

    private void collegaComandi() {
        view.onSave(this::saveGame);
        view.onNewGame(this::newGame);
        view.onExit(this::showStartMenu);
        
        view.onEat(() -> executeAction(new Eat()));
        view.onDrink(() -> executeAction(new Drink()));
        view.onRest(() -> executeAction(new Rest()));
        view.onFish(() -> executeAction(new Fish()));
        view.onScavenge(() -> executeAction(new Scavenge()));
        view.onCraftRod(() -> executeAction(new FishingRod()));
        view.onCook(() -> executeAction(new CookingFish()));
        view.onCollectWater(() -> executeAction(new CollectWater(controller.getGameState())));
        view.onBuildOars(() -> executeAction(new BuildOars()));
    }

        private void collegaMenuIniziale() {
        startMenuView.onNewGame(() -> {
            scene.setRoot(view.getRoot());
            newGame();
        });
        startMenuView.onLoadGame(() -> {
            scene.setRoot(view.getRoot());
            loadGame();
        });
        startMenuView.onExit(this::exitGame);
    }

    private void refresh() {
        view.update(controller.getGameState(), modalita);
    }

    // ---------- gestione della partita ----------

    private void saveGame() {
        try {
            saveManager.save(controller.getGameState(), SAVE_FILE);
            modalita = Modalita.PARTITA_SALVATA;
            view.log(" Partita salvata con successo.");
            refresh();
        } catch (Exception ex) {
            view.log(" Errore durante il salvataggio della partita: " + ex.getMessage());
        }
    }

    private void loadGame() {
        try {
            var loadedState = saveManager.load(SAVE_FILE);
            controller.setGameState(loadedState);
            modalita = Modalita.INIZIO_PARTITA;
            refresh();
            view.log(" Partita caricata con successo.");
        } catch (Exception ex) {
            view.log(" Errore nel caricamento: file non trovato o corrotto: " + ex.getMessage());
        }
    }

    private void newGame() {
        view.clearLog();
        var newGameState = new GameState(); 
        controller.setGameState(newGameState);
        modalita = Modalita.INIZIO_PARTITA;
        view.log("Nuova partita iniziata.");
        view.log("Obiettivo: raccogli 4 unità di Legna per costruire i remi e raggiungere l'isola!");
        refresh();
    }

    private void exitGame() {
        view.log("Uscita dal gioco.");
        Platform.exit();
    }

    // ---------- svolgimento del gioco ----------

    private void verifyEndGame() {
        var player = controller.getGameState().getPlayer();
        var state = controller.getGameState();
        boolean hasOars = state.getInventory().getItems().stream().anyMatch(item -> "Remi".equalsIgnoreCase(item.getName()));

        if (player.isDead()) {
            modalita = Modalita.FINE_PARTITA;
            view.log("Game Over! Sei morto, non puoi proseguire.");
            if (saveManager.deleteSaveFile(SAVE_FILE)) {
                view.log("La partita è stata eliminata.");
            }
        } else if (hasOars) {
            modalita = Modalita.FINE_PARTITA;
            view.log("Vittoria! Hai costruito i remi e sei approdato sull'isola!");
            if (saveManager.deleteSaveFile(SAVE_FILE))
                view.log ("La partita completata è stata rimossa.");
        } else {
            modalita = Modalita.IN_PARTITA;
        }
    }

    private void avanzaTurno() {
        var state = controller.getGameState();
        state.resetActionCount();
        controller.nextTurn();
        view.log("Turno passato. Giorno: " + state.getCurrentDay() + ", Meteo: " + state.getCurrentWeather());
        verifyEndGame();
        refresh();
    }

    private void executeAction(Action action) {
        var player = controller.getGameState().getPlayer();
        var inventory = controller.getGameState().getInventory();

        try {
            action.execute(player, inventory);
            if (action instanceof Scavenge scavengeAction) {
                view.log(scavengeAction.getUltimoEsito());
            } else {
                view.log("Azione " + action.getName() + " eseguita con successo.");
            }

            var state = controller.getGameState();
            state.incrementActionCount();

            verifyEndGame();
            if (modalita == Modalita.FINE_PARTITA) {
                refresh(); //aggiorna comunque l'interfaccia (bottoni spenti, Nuova Partita visibile)
                return; //se il giocatore è morto, non eseguire ulteriori azioni
            }
            view.log("Azioni eseguite oggi: " + state.getActionCount() + "/3");
            if (state.getActionCount() >= 3) {
                avanzaTurno();
            } else {
                refresh();
            }
        } catch (IllegalStateException | IllegalArgumentException ex) {
            view.log("Impossibile eseguire " + action.getName() + ": " + ex.getMessage());
            refresh();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}