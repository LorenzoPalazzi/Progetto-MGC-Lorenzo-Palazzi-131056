package it.unicam.cs.mpgc.rpg131056.view;

import it.unicam.cs.mpgc.rpg131056.model.Item;
import it.unicam.cs.mpgc.rpg131056.model.Modalita;
import it.unicam.cs.mpgc.rpg131056.actions.Action;
import it.unicam.cs.mpgc.rpg131056.actions.Drink;
import it.unicam.cs.mpgc.rpg131056.actions.Eat;
import it.unicam.cs.mpgc.rpg131056.actions.Fish;
import it.unicam.cs.mpgc.rpg131056.actions.Rest;
import it.unicam.cs.mpgc.rpg131056.actions.Scavenge;
import it.unicam.cs.mpgc.rpg131056.controller.GameController;
import it.unicam.cs.mpgc.rpg131056.controller.GameState;
import it.unicam.cs.mpgc.rpg131056.persistence.JsonSaveManager;
import it.unicam.cs.mpgc.rpg131056.persistence.SaveManager;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;

public class GameApp extends Application {
    //la GUI si affida a questi due attributi per il caricamento e il funzionamento del gioco
    private GameController controller;
    private SaveManager saveManager;
    private Modalita modalita;
    private ListView<String> lstInventory;

    private int actionCount = 0; //massimo 3 azioni al giorno, dopo passa turno
    // Componenti della GUI che dovremo aggiornare durante il gioco
    private Label lblDay; //etichette per giorno e meteo
    private Label lblWeather;
    private ProgressBar barHealth; //barre di vita, fame, sete ed energia
    private ProgressBar barHunger;
    private ProgressBar barThirst;
    private ProgressBar barEnergy; 
    private TextArea txtLog; //un'area di testo multiriga dove poter scrivere a schermo ciò che succede nel gioco
    //bottoni gestione partita
    private Button btnSave;
    private Button btnLoad;
    private Button btnExit;
    private Button btnNewGame;
    //bottoni per azioni giocatore
    private Button btnRest;
    private Button btnEat;
    private Button btnDrink;
    private Button btnScavenge;
    private Button btnFish;

    private void modalitaIniziale() { 
        if (new File("savegame.json").exists()) {
            modalita = Modalita.AVVIO_CON_JSON;
        } else {
            modalita = Modalita.INIZIO_PARTITA;
            } 
    }

    @Override
    public void start(Stage primaryStage) {
        // Inizializziamo il controller e la persistenza
        this.controller = new GameController();
        this.saveManager = new JsonSaveManager();
        modalitaIniziale();
        
        BorderPane root = new BorderPane(); //divide lo schermo in 5 zone, N, S, E, O, Centro
        root.setPadding(new Insets(15));

        // 1. ZONA SUPERIORE: Status del giocatore
        root.setTop(createHeaderPane()); //inserisce in alto la barra con le statistiche del giocatore

        // 2. ZONA CENTRALE: Registro eventi / Log
        txtLog = new TextArea("Benvenuto in Survival RPG!\nFai la tua prima mossa.\n");
        txtLog.setEditable(false); //non permette all'utente di scrivere dentro la zona centrale
        txtLog.setWrapText(true); //permette di andare a capo al testo se è troppo lungo su una sola riga
        root.setCenter(txtLog);
        BorderPane.setMargin(txtLog, new Insets(10, 0, 10, 0)); //E e O sono settati a 0, N e S a 10

        /* 3. ZONA INFERIORE: Pulsanti di azione
        * inserisce nel bordo inferiore ciò che è dichiarato dentro al metodo createActionsPane*/
        root.setBottom(createActionsPane());

        VBox inventoryPane = createInventoryPane();
        root.setRight(inventoryPane);
        BorderPane.setMargin(inventoryPane, new Insets(10, 0, 10, 10)); // sopra, destra, sotto, sinistra

        // Inizializziamo i valori grafici con lo stato attuale del gioco
        updateUI();

        Scene scene = new Scene(root, 850, 600);
        primaryStage.setTitle("Survival RPG - JavaFX");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private GridPane createHeaderPane() {
        GridPane grid = new GridPane(); //creo un nuovo GridPane chiamato grid
        grid.setHgap(10); //Configura la spaziatura orizzontale e verticale tra le celle
        grid.setVgap(10); //Configura la spaziatura orizzontale e verticale tra le celle
        grid.setAlignment(Pos.CENTER);

        lblDay = new Label("Giorno: 1");
        lblWeather = new Label("Meteo: SUNNY");

        //crea lo spazio sopra con distanza 10 pxl tra l'etichetta del giorno e quella del meteo
        HBox topInfo = new HBox(10, lblDay, lblWeather);

        //centra le etichette (topInfo) dentro l'HBox
        topInfo.setAlignment(Pos.CENTER);

        //dice al gridpane di posizionare topInfo (cioè l'HBox) al centro
        GridPane.setHalignment(topInfo, HPos.CENTER);

        //posizione l'HBox nella griglia con questi valori:
        // 0 = parte dalla colonna sinistra
        // 0 = parte dalla riga 0 in alto
        // 6 = si estende su 6 colonne
        // 1 = occupa 1 riga in altezza
        grid.add(topInfo, 0,0,8,1);

        barHealth = new ProgressBar(1.0);
        barHunger = new ProgressBar(0.0);
        barThirst = new ProgressBar(0.0);
        barEnergy = new ProgressBar(1.0);

        //Riga 0: Giorno e Meteo
        //grid.add(lblDay, 0, 0);
        //grid.add(lblWeather, 1, 0);

        // Riga 1: Barre di stato
        grid.add(new Label("Salute:"), 0, 1);
        grid.add(barHealth, 1, 1);

        grid.add(new Label("Fame:"), 2, 1);
        grid.add(barHunger, 3, 1);

        grid.add(new Label("Sete:"), 4, 1);
        grid.add(barThirst, 5, 1);

        grid.add(new Label("Energia:"), 6, 1);
        grid.add(barEnergy, 7, 1);

        return grid;
    }

    private VBox createInventoryPane() {
        Label lblTitle = new Label("Inventario");
        lstInventory = new ListView<>();
        lstInventory.setPrefWidth(200);

        VBox box = new VBox(10, lblTitle, lstInventory);
        box.setAlignment(Pos.TOP_CENTER);
        return box;
    }
    private void updateInventory() {
        Map<String, Integer> counts = new LinkedHashMap<>();

        //scorri l'inventario con gli oggetti e se il nome non c’è ancora, inseriscilo con 1; se c’è già, somma 1 al valore che ha
        for (Item item : controller.getGameState().getInventory().getItems()) {
        counts.merge(item.getName(), 1, Integer::sum);
    }

    lstInventory.getItems().clear();
    for (Map.Entry<String, Integer> entry : counts.entrySet()) {
        lstInventory.getItems().add(entry.getKey() + " x" + entry.getValue()); //
    }
}

    private VBox createActionsPane() {
        //contenitore "globale"
        VBox container = new VBox(10); //crea un contenitore verticale con distanza 10 pxl tra i bottoni
        container.setAlignment(Pos.CENTER); //posiziona centralmente i bottoni
        
        //contenitore bottoni di azioni del gioco
        HBox actionButtons = new HBox(10); //crea un contenitore orizzontale con distanza 10 pxl tra i bottoni
        actionButtons.setAlignment(Pos.CENTER); //posiziona centralmente i bottoni

        //comandi azionin di gioco
        btnRest = new Button("Riposa");
        btnEat = new Button("Mangia");
        btnDrink = new Button("Bevi");
        btnScavenge = new Button("Recupera Risorse");
        btnFish = new Button("Pesca");

        btnEat.setOnAction(e -> executeAction(new Eat()));
        btnDrink.setOnAction(e -> executeAction(new Drink()));
        btnRest.setOnAction(e -> executeAction(new Rest()));
        btnFish.setOnAction(e -> executeAction(new Fish()));
        btnScavenge.setOnAction(e -> executeAction(new Scavenge()));

        actionButtons.getChildren().addAll(btnRest, btnEat, btnDrink, btnScavenge, btnFish);

        //zona bottoni comandi di gioco
        HBox box = new HBox(10); //crea la barra inferiore
        box.setAlignment(Pos.CENTER); //la posiziona centralmente

        btnSave = new Button("Salva Partita");
        btnLoad = new Button("Carica Partita");
        btnExit = new Button("Esci");
        btnNewGame = new Button("Nuova Partita");
        
        //---------- listener ----------
        btnSave.setOnAction(e -> {
            try {
                saveManager.save(controller.getGameState(), "savegame.json");
                modalita = Modalita.PARTITA_SALVATA;
                txtLog.appendText(" Partita salvata con successo.\n");
                updateUI();
            } catch (Exception ex) {
                txtLog.appendText(" Errore durante il salvataggio della partita: " + ex.getMessage() + "\n");
            }
        }); 

        btnLoad.setOnAction(e -> {
            try {
                var loadedState = saveManager.load("savegame.json");
                controller.setGameState(loadedState);
                modalita = Modalita.INIZIO_PARTITA;
                updateUI();
                txtLog.appendText(" Partita caricata con successo.\n");
            } catch (Exception ex) {
                txtLog.appendText(" Errore nel caricamento: file non trovato o corrotto: " + ex.getMessage() + "\n");
           }
    });

        btnNewGame.setOnAction (e -> {
                var newGameState = new GameState(); //crea un nuovo GameState con valori iniziali
                controller.setGameState(newGameState);
                modalita = Modalita.INIZIO_PARTITA;
                txtLog.appendText("Nuova partita iniziata.\n");
                updateUI();
        });
        
        btnExit.setOnAction(e -> {
            txtLog.appendText("Uscita dal gioco.\n");
            Platform.exit();
        });

        /*getChildren inizialmente è vuoto perché HBox in basso non ha figli al suo interno, è vuoto
        * poi inserisco 3 figli, i 3 bottoni che ho creato, e mi vengono mostrati con lo stesso
        * ordine con cui li ho inseriti*/ 
       box.getChildren().addAll(btnSave, btnLoad, btnExit, btnNewGame);
       
       container.getChildren().addAll(actionButtons, box); //inserisce l'HBox con i bottoni in basso dentro al VBox principale
       return container;
    }

    private void verifyEndGame() {
        var player = controller.getGameState().getPlayer();
        if (player.isDead()) {
            modalita = Modalita.FINE_PARTITA;
            txtLog.appendText("Game Over! Sei morto, non puoi proseguire.\n");
            if (saveManager.deleteSaveFile("savegame.json")) {
                txtLog.appendText("La partita è stata eliminata.\n");
            }
        } else {
            modalita = Modalita.IN_PARTITA;
        }
    }

    private void avanzaTurno() {
        actionCount = 0; //resetta il contatore azioni al passaggio turno
        controller.nextTurn();
        var state = controller.getGameState();
        txtLog.appendText("Turno passato. Giorno: " + state.getCurrentDay() + ", Meteo: " + state.getCurrentWeather() + "\n");
        verifyEndGame();
        updateUI();
    }

    private void executeAction(Action action) {
        var player = controller.getGameState().getPlayer();
        var inventory = controller.getGameState().getInventory();
        try {
            action.execute(player, inventory);
            if (action instanceof Scavenge scavengeAction) {
                txtLog.appendText(scavengeAction.getUltimoEsito() + "\n");
            } else {
                txtLog.appendText("Azione " + action.getName() + " eseguita con successo.\n");
            }
            verifyEndGame();
            if (modalita == Modalita.FINE_PARTITA) {
                return; //se il giocatore è morto, non eseguire ulteriori azioni
            }
            actionCount++;
            txtLog.appendText("Azioni eseguite oggi: " + actionCount + "/3\n");
            if (actionCount >= 3) {
                avanzaTurno();
            } else {
                updateUI();
            }
        } catch (IllegalStateException | IllegalArgumentException ex) {
            txtLog.appendText("Impossibile eseguire " + action.getName() + ": " + ex.getMessage() + "\n");
        }
        updateUI();
    }


    private void updateUI() { //fa da ponte tra la GUI e il backend del gioco
        var state = controller.getGameState();
        var player = state.getPlayer();
        var exitVisible = modalita == Modalita.PARTITA_SALVATA || modalita == Modalita.FINE_PARTITA;
        var newGameVisible = modalita == Modalita.FINE_PARTITA;
        boolean inGame = modalita == Modalita.AVVIO_CON_JSON || modalita == Modalita.INIZIO_PARTITA || modalita == Modalita.IN_PARTITA;
        lblDay.setText("Giorno: " + state.getCurrentDay()); //restituisce il giorno e il meteo istanziati in GameState
        lblWeather.setText("Meteo: " + state.getCurrentWeather());
        
        //le ProgressBar in JavaFX usano valori decimali tra 0.0 e 1.0, serve convertirle perché i valori di salute, fame e sete sono tra 0 e 100
        barHealth.setProgress(player.getHealth() / 100.0); //100.0 perché altrimenti la divisione di 2 interi da come resto 0, azzerando la barra
        barHunger.setProgress(player.getHunger() / 100.0);
        barThirst.setProgress(player.getThirst() / 100.0);
        barEnergy.setProgress(player.getEnergy() / 100.0);
        
        //attivazione e disattivazione dei bottoni in base alla fasi di gioco
        btnSave.setDisable(!(modalita == Modalita.IN_PARTITA));
        btnLoad.setDisable(!(modalita == Modalita.AVVIO_CON_JSON || modalita == Modalita.PARTITA_SALVATA));
        btnExit.setVisible(exitVisible);
        btnExit.setManaged(exitVisible);
        btnNewGame.setVisible(newGameVisible);
        btnNewGame.setManaged(newGameVisible);
        btnRest.setDisable(!(inGame));
        btnEat.setDisable(!(inGame));
        btnDrink.setDisable(!(inGame));
        btnScavenge.setDisable(!(inGame));
        btnFish.setDisable(!(inGame));

        updateInventory();
}

    public static void main(String[] args) {
        launch(args);
    }
}

