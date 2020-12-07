package sockn.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.text.Text;

public class MenuController {
    // The reference will be injected by the FXML loader
    @FXML private int playerCount = 4;

    @FXML private Text actiontarget;
    @FXML private Label buttonTwoPlayers;
    @FXML private Label buttonThreePlayers;
    @FXML private Label buttonFourPlayers;

    @FXML
    public void initialize() {
        setPlayerCount(playerCount);
        buttonTwoPlayers.setOnMouseClicked(e -> setPlayerCount(2));
        buttonThreePlayers.setOnMouseClicked(e -> setPlayerCount(3));
        buttonFourPlayers.setOnMouseClicked(e -> setPlayerCount(4));
    }

    @FXML
    private void setPlayerCount(int playerCount) {
        this.playerCount = playerCount;
        actiontarget.setText("Players: " + playerCount);
    }

    @FXML
    public int getPlayerCount() {
        return playerCount;
    }
}
