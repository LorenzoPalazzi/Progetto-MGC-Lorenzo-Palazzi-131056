package it.unicam.cs.mpgc.rpg131056.view;

import java.util.LinkedHashMap;
import java.util.Map;

import it.unicam.cs.mpgc.rpg131056.controller.GameState;
import it.unicam.cs.mpgc.rpg131056.model.Food;
import it.unicam.cs.mpgc.rpg131056.model.Item;
import it.unicam.cs.mpgc.rpg131056.model.Modalita;
import it.unicam.cs.mpgc.rpg131056.model.Weather;
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
    private final BorderPane root = new BorderPane(); 
    private Label lblDay; 
    private Label lblWeather;
    private ProgressBar barHealth; 
    private ProgressBar barHunger;
    private ProgressBar barThirst;
    private ProgressBar barEnergy;
    private TextArea txtLog; 
    private ListView<String> lstInventory;

    private Button btnSave;
    private Button btnExit;
    private Button btnNewGame;

    private Button btnRest;
    private Button btnEat;
    private Button btnDrink;
    private Button btnScavenge;
    private Button btnFish;
    private Button btnCraftRod;
    private Button btnCook;
    private Button btnCollectWater;
    private Button btnBuildOars;
 
    public GameView() {
        root.setPadding(new Insets(15));
 
        root.setTop(createHeaderPane()); 
 

        txtLog = new TextArea("Benvenuto in Survival RPG!\nFai la tua prima mossa.\n");
        txtLog.setEditable(false);
        txtLog.setWrapText(true); 
        root.setCenter(txtLog);
        BorderPane.setMargin(txtLog, new Insets(10, 0, 10, 0));
 
        root.setBottom(createActionsPane());
 
        VBox inventoryPane = createInventoryPane();
        root.setRight(inventoryPane);
        BorderPane.setMargin(inventoryPane, new Insets(10, 0, 10, 10)); 
    }
 
    public BorderPane getRoot() { return root; }

    public void clearLog() { txtLog.clear(); }
 
    //scrive una riga nel registro eventi
    public void log(String line) { txtLog.appendText(line + "\n"); }
 
    // ---------- creazione dei componenti ----------
 
    private GridPane createHeaderPane() {
        GridPane grid = new GridPane(); 
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setAlignment(Pos.CENTER);
 
        lblDay = new Label("Giorno: 1");
        lblWeather = new Label("Meteo: SUNNY");
 
        HBox topInfo = new HBox(10, lblDay, lblWeather);
 
        topInfo.setAlignment(Pos.CENTER);
 
        GridPane.setHalignment(topInfo, HPos.CENTER);
 
        grid.add(topInfo, 0, 0, 8, 1);
 
        barHealth = new ProgressBar(1.0);
        barHunger = new ProgressBar(0.0);
        barThirst = new ProgressBar(0.0);
        barEnergy = new ProgressBar(1.0);
 
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
        VBox container = new VBox(10); 
        container.setAlignment(Pos.CENTER);
 
        HBox actionButtons = new HBox(10);
        actionButtons.setAlignment(Pos.CENTER);
 
        btnRest = new Button("Riposa");
        btnEat = new Button("Mangia");
        btnDrink = new Button("Bevi");
        btnScavenge = new Button("Recupera Risorse");
        btnFish = new Button("Pesca");
        btnCook = new Button("Cuoci pesce");
        btnCraftRod = new Button("Crea canna da pesca");
        btnCollectWater = new Button("Raccogli acqua");
        btnBuildOars = new Button("Costruisci Remi");
 
        actionButtons.getChildren().addAll(
            btnRest, btnEat, btnDrink, btnScavenge, btnFish, 
            btnCook, btnCraftRod, btnCollectWater, btnBuildOars
        );
 
        HBox box = new HBox(10);
        box.setAlignment(Pos.CENTER);
 
        btnSave = new Button("Salva Partita");
        btnExit = new Button("Esci");
        btnNewGame = new Button("Nuova Partita");
 
        box.getChildren().addAll(btnSave, btnExit, btnNewGame);
 
        container.getChildren().addAll(actionButtons, box);
        return container;
    }
 
    // ---------- aggiornamento dei componenti ----------

    public void update(GameState state, Modalita modalita) {
        var player = state.getPlayer();
        var inventory = state.getInventory();
        boolean exitVisible = modalita == Modalita.PARTITA_SALVATA || modalita == Modalita.FINE_PARTITA;
        boolean newGameVisible = modalita == Modalita.FINE_PARTITA;
        boolean inGame = modalita == Modalita.AVVIO_CON_JSON || modalita == Modalita.INIZIO_PARTITA || modalita == Modalita.IN_PARTITA;
        boolean tooTired = player.getEnergy() < 20;
        int requiredWaterEnergy = (state.getCurrentWeather() == Weather.RAINY || state.getCurrentWeather() == Weather.STORMY) ? 5 : 15;
        boolean canCollectWater = state.getPlayer().getEnergy() >= requiredWaterEnergy;

        lblDay.setText("Giorno: " + state.getCurrentDay());
        lblWeather.setText("Meteo: " + state.getCurrentWeather());

        barHealth.setProgress(player.getHealth() / 100.0); 
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

        btnSave.setDisable(!(modalita == Modalita.IN_PARTITA));

        btnExit.setVisible(exitVisible);
        btnExit.setManaged(exitVisible);
        btnNewGame.setVisible(newGameVisible);
        btnNewGame.setManaged(newGameVisible);
        btnRest.setDisable(!inGame);
        btnEat.setDisable(!inGame || !hasFood);
        btnDrink.setDisable(!inGame || !hasWater);
        btnScavenge.setDisable(!inGame || player.getEnergy() < 20);
        btnFish.setDisable(!inGame || tooTired || !haveRod);

        boolean createRod = inGame && !haveRod && countWood >= 2 && countFishingNet >= 1;
        btnCraftRod.setVisible(createRod);
        btnCraftRod.setManaged(createRod);
        btnCraftRod.setDisable(!createRod);
        
        btnCollectWater.setDisable(!inGame || !canCollectWater);
 
        boolean canCookFish = inGame && (countWood >= 1) && haveRowFish;
        btnCook.setVisible(canCookFish);
        btnCook.setManaged(canCookFish);

        boolean canCraftOars = inGame && countWood >= 4;
        btnBuildOars.setVisible(canCraftOars);
        btnBuildOars.setManaged(canCraftOars);
        btnBuildOars.setDisable(!canCraftOars);
 
        updateInventory(state);

        hungerThirstColorBar(barHunger, player.getHunger());
        hungerThirstColorBar(barThirst, player.getThirst());
        energyHealthColorBar(barEnergy, player.getEnergy());
        energyHealthColorBar(barHealth, player.getHealth());
    }
 
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
    public void onExit(Runnable action) { btnExit.setOnAction(e -> action.run()); }
    public void onNewGame(Runnable action) { btnNewGame.setOnAction(e -> action.run()); }
 
    public void onRest(Runnable action) { btnRest.setOnAction(e -> action.run()); }
    public void onEat(Runnable action) { btnEat.setOnAction(e -> action.run()); }
    public void onDrink(Runnable action) { btnDrink.setOnAction(e -> action.run()); }
    public void onScavenge(Runnable action) { btnScavenge.setOnAction(e -> action.run()); }
    public void onFish(Runnable action) { btnFish.setOnAction(e -> action.run()); }
    public void onCraftRod(Runnable action) { btnCraftRod.setOnAction(e -> action.run()); }
    public void onCook(Runnable action) { btnCook.setOnAction(e -> action.run()); }
    public void onCollectWater (Runnable action) { btnCollectWater.setOnAction(e -> action.run());}
    public void onBuildOars(Runnable action) { btnBuildOars.setOnAction(e -> action.run()); }

    private void hungerThirstColorBar(ProgressBar bar, double value) {
        if (value > 80) {
            bar.setStyle("-fx-accent: #e74c3c;"); 
        } else if (value > 60) {
            bar.setStyle("-fx-accent: #f39c12;"); 
        } else {
            bar.setStyle(""); 
        }
    }
    
    private void energyHealthColorBar(ProgressBar bar, double value) {
        if (value <= 20) {
            bar.setStyle("-fx-accent: #e74c3c;");  
        } else if (value <= 40) {
            bar.setStyle("-fx-accent: #f39c12;"); 
        } else {
            bar.setStyle(""); 
        }
    }
}
