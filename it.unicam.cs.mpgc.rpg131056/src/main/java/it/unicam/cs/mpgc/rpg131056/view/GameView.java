package it.unicam.cs.mpgc.rpg131056.view;

import java.util.LinkedHashMap;
import java.util.Map;

import it.unicam.cs.mpgc.rpg131056.controller.GameState;
import it.unicam.cs.mpgc.rpg131056.model.Food;
import it.unicam.cs.mpgc.rpg131056.model.Item;
import it.unicam.cs.mpgc.rpg131056.model.Modalita;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class GameView {
    private final BorderPane root = new BorderPane(); //divide lo schermo in 5 zone, N, S, E, O, Centro
 
    // Componenti della GUI che dovremo aggiornare durante il gioco
    private Label lblDay; //etichette per giorno e meteo
    private Label lblWeather;
    private ProgressBar barHealth; //barre di vita, fame, sete ed energia
    private ProgressBar barHunger;
    private ProgressBar barThirst;
    private ProgressBar barEnergy;
    private TextArea txtLog; //un'area di testo multiriga dove poter scrivere a schermo ciò che succede nel gioco
    private ListView<String> lstInventory;
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
    private Button btnCraftRod;
    private Button btnCook;
 
    public GameView() {
        root.setPadding(new Insets(15));
 
        // 1. ZONA SUPERIORE: Status del giocatore
        root.setTop(createHeaderPane()); //inserisce in alto la barra con le statistiche del giocatore
 
        // 2. ZONA CENTRALE: Registro eventi / Log
        txtLog = new TextArea("Benvenuto in Survival RPG!\nFai la tua prima mossa.\n");
        txtLog.setEditable(false); //non permette all'utente di scrivere dentro la zona centrale
        txtLog.setWrapText(true); //permette di andare a capo al testo se è troppo lungo su una sola riga
        root.setCenter(txtLog);
        BorderPane.setMargin(txtLog, new Insets(10, 0, 10, 0)); //E e O sono settati a 0, N e S a 10
 
        // 3. ZONA INFERIORE: Pulsanti di azione
        root.setBottom(createActionsPane());
 
        // 4. ZONA DESTRA: Inventario
        VBox inventoryPane = createInventoryPane();
        root.setRight(inventoryPane);
        BorderPane.setMargin(inventoryPane, new Insets(10, 0, 10, 10)); // sopra, destra, sotto, sinistra
    }
 
    public BorderPane getRoot() { return root; }
 
    //scrive una riga nel registro eventi
    public void log(String line) { txtLog.appendText(line + "\n"); }
 
    // ---------- creazione dei componenti ----------
 
    private GridPane createHeaderPane() {
        GridPane grid = new GridPane(); //creo un nuovo GridPane chiamato grid
        grid.setHgap(10); //Configura la spaziatura orizzontale e verticale tra le celle
        grid.setVgap(10);
        grid.setAlignment(Pos.CENTER);
 
        lblDay = new Label("Giorno: 1");
        lblWeather = new Label("Meteo: SUNNY");
 
        //crea lo spazio sopra con distanza 10 pxl tra l'etichetta del giorno e quella del meteo
        HBox topInfo = new HBox(10, lblDay, lblWeather);
 
        //centra le etichette (topInfo) dentro l'HBox
        topInfo.setAlignment(Pos.CENTER);
 
        //dice al gridpane di posizionare topInfo (cioè l'HBox) al centro
        GridPane.setHalignment(topInfo, HPos.CENTER);
 
        //posiziona l'HBox nella griglia con questi valori:
        // 0 = parte dalla colonna sinistra
        // 0 = parte dalla riga 0 in alto
        // 8 = si estende su 8 colonne (4 barre, ognuna con etichetta e barra)
        // 1 = occupa 1 riga in altezza
        grid.add(topInfo, 0, 0, 8, 1);
 
        barHealth = new ProgressBar(1.0);
        barHunger = new ProgressBar(0.0);
        barThirst = new ProgressBar(0.0);
        barEnergy = new ProgressBar(1.0);
 
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
 
    private VBox createActionsPane() {
        //contenitore "globale"
        VBox container = new VBox(10); //crea un contenitore verticale con distanza 10 pxl tra le due righe di bottoni
        container.setAlignment(Pos.CENTER);
 
        //riga dei bottoni di azioni del gioco
        HBox actionButtons = new HBox(10);
        actionButtons.setAlignment(Pos.CENTER);
 
        btnRest = new Button("Riposa");
        btnEat = new Button("Mangia");
        btnDrink = new Button("Bevi");
        btnScavenge = new Button("Recupera Risorse");
        btnFish = new Button("Pesca");
        btnCook = new Button("Cuoci pesce");
        btnCraftRod = new Button("Crea canna da pesca");
 
        actionButtons.getChildren().addAll(btnRest, btnEat, btnDrink, btnScavenge, btnFish, btnCook, btnCraftRod);
 
        //riga dei bottoni di gestione della partita
        HBox box = new HBox(10);
        box.setAlignment(Pos.CENTER);
 
        btnSave = new Button("Salva Partita");
        btnLoad = new Button("Carica Partita");
        btnExit = new Button("Esci");
        btnNewGame = new Button("Nuova Partita");
 
        box.getChildren().addAll(btnSave, btnLoad, btnExit, btnNewGame);
 
        container.getChildren().addAll(actionButtons, box);
        return container;
    }
 
    // ---------- aggiornamento dei componenti ----------
 
    //fa da ponte tra la GUI e lo stato del gioco: riceve lo stato e la modalità e ridisegna tutto
    public void update(GameState state, Modalita modalita) {
        var player = state.getPlayer();
        var inventory = state.getInventory();
        boolean exitVisible = modalita == Modalita.PARTITA_SALVATA || modalita == Modalita.FINE_PARTITA;
        boolean newGameVisible = modalita == Modalita.FINE_PARTITA;
        boolean inGame = modalita == Modalita.AVVIO_CON_JSON || modalita == Modalita.INIZIO_PARTITA || modalita == Modalita.IN_PARTITA;
        boolean tooTired = player.getEnergy() < 20;
 
        lblDay.setText("Giorno: " + state.getCurrentDay());
        lblWeather.setText("Meteo: " + state.getCurrentWeather());
 
        //le ProgressBar in JavaFX usano valori decimali tra 0.0 e 1.0, serve convertirle perché i valori di salute, fame, sete ed energia sono tra 0 e 100
        barHealth.setProgress(player.getHealth() / 100.0); //100.0 perché altrimenti la divisione tra 2 interi restituisce 0, azzerando la barra
        barHunger.setProgress(player.getHunger() / 100.0);
        barThirst.setProgress(player.getThirst() / 100.0);
        barEnergy.setProgress(player.getEnergy() / 100.0);
 
        boolean haveRod = false;
        boolean haveRowFish = false;
        boolean hasFood = false;
        boolean hasWater = false;
        int countWood = 0;
        int countFishingNet = 0;

        for (Item item : inventory.getItems()) {
            if (item instanceof Food) {
                hasFood = true;
            }
            if ("Acqua".equalsIgnoreCase(item.getName())) {
                hasWater = true;
            }
        }
 
        for (Item item : inventory.getItems()) {
            switch (item.getName()) {
                case "Canna da Pesca" -> haveRod = true;
                case "Pesce crudo" -> haveRowFish = true;
                case "Legna" -> countWood++;
                case "Rete" -> countFishingNet++;
            }
        }
 
        //attivazione e disattivazione dei bottoni in base alle fasi di gioco
        btnSave.setDisable(!(modalita == Modalita.IN_PARTITA));
        btnLoad.setDisable(!(modalita == Modalita.AVVIO_CON_JSON || modalita == Modalita.PARTITA_SALVATA));
        btnExit.setVisible(exitVisible);
        btnExit.setManaged(exitVisible);
        btnNewGame.setVisible(newGameVisible);
        btnNewGame.setManaged(newGameVisible);
        btnRest.setDisable(!inGame);
        btnEat.setDisable(!inGame || !hasFood);
        btnDrink.setDisable(!inGame || !hasWater);
        btnScavenge.setDisable(!inGame);
        btnFish.setDisable(!inGame || tooTired || !haveRod);
 
        boolean createRod = inGame && !haveRod;
        btnCraftRod.setVisible(createRod);
        btnCraftRod.setManaged(createRod);
        btnCraftRod.setDisable(countWood < 2 || countFishingNet < 1);
 
        // Cuoci Pesce: visibile solo se possiedi almeno 1 Legna e 1 Pesce Crudo
        boolean canCookFish = inGame && (countWood >= 1) && haveRowFish;
        btnCook.setVisible(canCookFish);
        btnCook.setManaged(canCookFish);
 
        updateInventory(state);
    }
 
    /* Aggiorna il riquadro dell'inventario a destra dello schermo, in due fasi.
     * 1) Conta gli oggetti: crea una mappa nome -> quantità (LinkedHashMap, per mantenere
     * l'ordine di inserimento) e scorre l'inventario. merge() inserisce il nome con valore 1
     * se non c'è ancora, altrimenti somma 1 alla quantità già presente.
     * 2) Riempie la lista: la svuota con clear(), altrimenti a ogni aggiornamento le righe si accumulerebbero (pesce x2, pesce x2 ecc…),
     * poi scorre la mappa con entrySet() e aggiunge una riga per ogni voce nel formato "Pesce crudo x2".
     * Viene richiamato alla fine di update(), quindi si aggiorna dopo ogni azione, Carica e Nuova Partita. */
    private void updateInventory(GameState state) {
        Map<String, Integer> counts = new LinkedHashMap<>();
        for (Item item : state.getInventory().getItems()) {
            counts.merge(item.getName(), 1, Integer::sum);
        }
        lstInventory.getItems().clear();
        for (Map.Entry<String, Integer> entry : counts.entrySet()) {
            lstInventory.getItems().add(entry.getKey() + " x" + entry.getValue());
        }
    }
 
    // ---------- collegamento dei comandi (i listener vengono decisi da chi usa la view) ----------
 
    public void onSave(Runnable action) { btnSave.setOnAction(e -> action.run()); }
    public void onLoad(Runnable action) { btnLoad.setOnAction(e -> action.run()); }
    public void onExit(Runnable action) { btnExit.setOnAction(e -> action.run()); }
    public void onNewGame(Runnable action) { btnNewGame.setOnAction(e -> action.run()); }
 
    public void onRest(Runnable action) { btnRest.setOnAction(e -> action.run()); }
    public void onEat(Runnable action) { btnEat.setOnAction(e -> action.run()); }
    public void onDrink(Runnable action) { btnDrink.setOnAction(e -> action.run()); }
    public void onScavenge(Runnable action) { btnScavenge.setOnAction(e -> action.run()); }
    public void onFish(Runnable action) { btnFish.setOnAction(e -> action.run()); }
    public void onCraftRod(Runnable action) { btnCraftRod.setOnAction(e -> action.run()); }
    public void onCook(Runnable action) { btnCook.setOnAction(e -> action.run()); }
}
