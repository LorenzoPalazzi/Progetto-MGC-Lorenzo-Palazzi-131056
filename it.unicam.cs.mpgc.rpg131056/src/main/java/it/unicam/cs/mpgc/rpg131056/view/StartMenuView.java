package it.unicam.cs.mpgc.rpg131056.view;

import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

public class StartMenuView {
    private final VBox root;
    private final Button btnNewGame;
    private final Button btnLoadGame;
    private final Button btnExit;

    public StartMenuView() {
        Label title = new Label("Survival RPG");
        title.setFont(new Font(32));

        btnNewGame = new Button("Nuova Partita");
        btnLoadGame = new Button("Carica Partita");
        btnExit = new Button("Esci dal Gioco");

        btnNewGame.setPrefWidth(200);
        btnLoadGame.setPrefWidth(200);
        btnExit.setPrefWidth(200);

        root = new VBox(15, title, btnNewGame, btnLoadGame, btnExit);
        root.setAlignment(Pos.CENTER);
    }

    public Parent getRoot() { return root;}
    public void setCanLoad(boolean canLoad) { btnLoadGame.setDisable(!canLoad);}
    public void onNewGame(Runnable action) { btnNewGame.setOnAction(e -> action.run());}
    public void onLoadGame(Runnable action) { btnLoadGame.setOnAction(e -> action.run());}
    public void onExit(Runnable action) { btnExit.setOnAction(e -> action.run()); }
}
