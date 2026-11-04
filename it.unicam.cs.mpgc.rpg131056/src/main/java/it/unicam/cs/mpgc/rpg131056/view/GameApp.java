package it.unicam.cs.mpgc.rpg131056.view;

import it.unicam.cs.mpgc.rpg131056.model.Modalita;
import it.unicam.cs.mpgc.rpg131056.controller.GameController;
import it.unicam.cs.mpgc.rpg131056.persistence.JsonSaveManager;
import it.unicam.cs.mpgc.rpg131056.persistence.SaveManager;
import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.io.File;

public class GameApp extends Application {
    //la GUI si affida a questi due attributi per il caricamento e il funzionamento del gioco
    private GameController controller;
    private SaveManager saveManager;
    private Modalita modalita;

    // Componenti della GUI che dovremo aggiornare durante il gioco
    private Label lblDay; //etichette per giorno e meteo
    private Label lblWeather;
    private ProgressBar barHealth; //barre di vita, fame e sete
    private ProgressBar barHunger;
    private ProgressBar barThirst;
    private TextArea txtLog; //un'area di testo multiriga dove poter scrivere a schermo ciò che succede nel gioco
    private Button btnNextTurn;
    private Button btnSave;
    private Button btnLoad;

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
        grid.add(topInfo, 0,0,6,1);

        barHealth = new ProgressBar(1.0);
        barHunger = new ProgressBar(0.0);
        barThirst = new ProgressBar(0.0);

        // Riga 0: Giorno e Meteo
//        grid.add(lblDay, 0, 0);
//        grid.add(lblWeather, 1, 0);

        // Riga 1: Barre di stato
        grid.add(new Label("Salute:"), 0, 1);
        grid.add(barHealth, 1, 1);

        grid.add(new Label("Fame:"), 2, 1);
        grid.add(barHunger, 3, 1);

        grid.add(new Label("Sete:"), 4, 1);
        grid.add(barThirst, 5, 1);

        return grid;
    }

    private HBox createActionsPane() {
        HBox box = new HBox(10); //crea la barra inferiore
        box.setAlignment(Pos.CENTER); //la posiziona centralmente

        btnNextTurn = new Button("Passa Turno"); //creo i 3 bottoni che inserisco poi nell'HBox in basso
        btnSave = new Button("Salva Partita");
        btnLoad = new Button("Carica Partita");
        
        //---------- listener ----------
        btnNextTurn.setOnAction(e -> { //lambda expression
            controller.nextTurn();
            
            var state = controller.getGameState();
            var player = state.getPlayer();
            txtLog.appendText("Turno passato. Giorno: " + state.getCurrentDay() + 
            ", Meteo: " + state.getCurrentWeather() + "\n");
            if (player.getHealth() <= 0 || player.getHunger() >= 100 || player.getThirst() >= 100) {
                modalita = Modalita.FINE_PARTITA;
                txtLog.appendText("Game Over! Sei morto, non puoi proseguire.\n");    
            } else {
                modalita = Modalita.IN_PARTITA;
            }
            updateUI();
        });

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
        /*getChildren inizialmente è vuoto perché HBox in basso non ha figli al suo interno, è vuoto
        * poi inserisco 3 figli, i 3 bottoni che ho creato, e mi vengono mostrati con lo stesso
        * ordine con cui li ho inseriti*/ 
       box.getChildren().addAll(btnNextTurn, btnSave, btnLoad);
       return box;
    }

    private void updateUI() { //fa da ponte tra la GUI e il backend del gioco
        var state = controller.getGameState();
        var player = state.getPlayer();
        
        lblDay.setText("Giorno: " + state.getCurrentDay()); //restituisce il giorno e il meteo istanziati in GameState
        lblWeather.setText("Meteo: " + state.getCurrentWeather());
        
        //le ProgressBar in JavaFX usano valori decimali tra 0.0 e 1.0, serve convertirle perché i valori di salute, fame e sete sono tra 0 e 100
        barHealth.setProgress(player.getHealth() / 100.0); //100.0 perché altrimenti la divisione di 2 interi da come resto 0, azzerando la barra
        barHunger.setProgress(player.getHunger() / 100.0);
        barThirst.setProgress(player.getThirst() / 100.0);
        
        //devo modificare qua sotto tutto lo pseudocodice della tabella di claude
        btnNextTurn.setDisable(!(modalita == Modalita.AVVIO_CON_JSON || 
            modalita == Modalita.INIZIO_PARTITA || 
            modalita == Modalita.IN_PARTITA));
        
        btnSave.setDisable(!(modalita == Modalita.IN_PARTITA));

        btnLoad.setDisable(!(modalita == Modalita.AVVIO_CON_JSON || modalita == Modalita.PARTITA_SALVATA));
}

    public static void main(String[] args) {
        launch(args);
    }
}

//saveFile.delete(); --> IMPORTANTE PER DOPO