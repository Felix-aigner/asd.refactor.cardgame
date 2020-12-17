package sockn;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import sockn.classes.Game;
import sockn.controllers.MenuController;

import java.io.IOException;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.show();
        loadGameMenu(primaryStage);
    }

    private void loadGameMenu(Stage stage) throws IOException {
        FXMLLoader fxmlloader = new FXMLLoader(getClass().getResource("views/menu.fxml"));
        GridPane root = fxmlloader.load();
        MenuController menucontroller = fxmlloader.getController();

        stage.setTitle("Socken");
        stage.setScene(new Scene(root, 500, 340));
        stage.centerOnScreen();

        Button startBtn = (Button) root.lookup("#buttonStart");
        startBtn.setOnAction(e -> {
            try {
                String playerName = ((TextField) root.lookup("#textFieldPlayerName")).getText();

                Game game = new Game(menucontroller.getPlayerCount(), playerName);
                game.initPlayers();
                game.initBoard();

                Pane rootPane = game.getRootPane();

                stage.setScene(new Scene(rootPane));
                stage.centerOnScreen();

                Button newGame = (Button) rootPane.lookup("#buttonNewGame");
                newGame.setOnAction(event -> {
                    try {
                        loadGameMenu(stage);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                });

                game.startNewRound();
            } catch (Exception exc) {
                exc.printStackTrace();
            }
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}
