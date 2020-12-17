package sockn.classes;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.stage.StageStyle;
import sockn.enums.PlayerPosition;
import sockn.events.CardEvent;
import sockn.events.CardEventHandler;

import java.util.ArrayList;
import java.util.Optional;
import java.util.Set;

public class Game {
    private BorderPane rootPane;
    private int playerCount;
    private Player[] players;
    private PlayerPosition[] positions;
    private String playerName;
    private ArrayList<Round> rounds = new ArrayList<>();
    private Round actualRound;

    public Game(int playerCount, String playerName) throws Exception {
        this.playerCount = playerCount;
        this.playerName = playerName;

        this.rootPane = FXMLLoader.load(getClass().getResource("../views/game.fxml"));
    }

    public Pane getRootPane() {
        return rootPane;
    }

    Player[] getPlayers() {
        return players;
    }

    public void initPlayers() {
        this.players = new Player[this.playerCount];

        players[0] = new Player(0, this.playerName, true, this.rootPane);
        for(int i = 1; i < playerCount; i++) {
            players[i] = new Player(i, Constants.COMP_NAMES[i-1], false, this.rootPane);
        }
    }

    public void initBoard() {
        this.determinePlayerPositions();
        this.drawPlayerAndBotNames();
        this.drawPutzenIcons();
        this.initEventListeners();
    }

    private void determinePlayerPositions() {
        this.positions = new PlayerPosition[playerCount];

        switch (playerCount) {
            case 2:
                this.positions[0] = PlayerPosition.BOTTOM;
                this.positions[1] = PlayerPosition.TOP;
                break;
            case 3:
                this.positions[0] = PlayerPosition.BOTTOM;
                this.positions[1] = PlayerPosition.LEFT;
                this.positions[2] = PlayerPosition.RIGHT;
                break;
            case 4:
                this.positions[0] = PlayerPosition.BOTTOM;
                this.positions[1] = PlayerPosition.LEFT;
                this.positions[2] = PlayerPosition.TOP;
                this.positions[3] = PlayerPosition.RIGHT;
                break;
        }
    }

    private void drawPlayerAndBotNames() {
        ((Label) this.rootPane.getBottom().lookup(".labelPlayerName")).setText(this.playerName);
        switch (this.playerCount) {
            case 2:
                this.rootPane.setLeft(null);
                this.rootPane.setRight(null);
                ((Label) this.rootPane.getTop().lookup(".labelPlayerName")).setText(Constants.COMP_NAMES[0]);
                break;
            case 3:
                this.rootPane.setTop(null);
                ((Label) this.rootPane.getLeft().lookup(".labelPlayerName")).setText(Constants.COMP_NAMES[0]);
                ((Label) this.rootPane.getRight().lookup(".labelPlayerName")).setText(Constants.COMP_NAMES[1]);
                break;
            case 4:
                ((Label) this.rootPane.getLeft().lookup(".labelPlayerName")).setText(Constants.COMP_NAMES[0]);
                ((Label) this.rootPane.getTop().lookup(".labelPlayerName")).setText(Constants.COMP_NAMES[1]);
                ((Label) this.rootPane.getRight().lookup(".labelPlayerName")).setText(Constants.COMP_NAMES[2]);
                break;
        }
    }

    private void drawPutzenIcons() {
        String imageName = "Putzen-" + Constants.PLAYER_LIVES + ".png";
        String url = getClass().getResource("../../images/" + imageName).toExternalForm();

        Set<Node> imageViewsPutzen = this.rootPane.lookupAll(".imageViewPutzen");
        for (Node node : imageViewsPutzen) {
            ((ImageView) node).setImage(new Image(url));
        }
    }

    private void initEventListeners() {
        this.rootPane.addEventHandler(CardEvent.PLAYED_CARD, new CardEventHandler() {
            @Override
            public void onBotPlayedCard(int playerNumber, Card playedCard) {
                addPlayedCardToRound(players[playerNumber], playedCard);
                forcePlayerToPlayCard(indexOfNextPlayer(playerNumber));
            }

            @Override
            public void onHumanPlayedCard(int playerNumber, Card playedCard) {
                players[playerNumber].removeClickListenerForCards();

                addPlayedCardToRound(players[playerNumber], playedCard);
                forcePlayerToPlayCard(indexOfNextPlayer(playerNumber));
            }

            private void addPlayedCardToRound(Player player, Card card) {
                actualRound.addPlayedCard(player, card);
            }

            private int indexOfNextPlayer(int playerNumber) {
                return (playerNumber + 1) % players.length;
            }

            private void forcePlayerToPlayCard(int playerNumber) {
                // check if all players already put card on the table
                if(actualRound.getActualPlayedCards().size() == players.length) {
                    // determine stich and draw points and stich
                    int[] playerIdxAndPoints = actualRound.determineStich();
                    int playerIdxOfStich = playerIdxAndPoints[0];
                    int points = playerIdxAndPoints[1];
                    actualRound.drawStichsAndPoints(playerIdxOfStich, points);

                    // wait 3 seconds
                    new Thread(() -> {
                        try {
                            Thread.sleep(3000);
                            Platform.runLater(
                                () -> {
                                    // check if all hand cards were played
                                    if(players[0].getHandCards().size() == 0) {
                                        roundIsFinished();
                                    } else {
                                        Player player = players[playerIdxOfStich];
                                        takeCardsFromTable(true);
                                        // if its the turn of the bot cause of a stich, force bot to play card
                                        if (!(player.getIsHuman())) {
                                            forcePlayerToPlayCard(playerIdxOfStich);
                                        } else {
                                            player.initClickListenerForCards();
                                        }
                                    }
                                }
                            );
                        } catch (InterruptedException ex) {
                        }
                    }).start();

                } else if(!players[playerNumber].getIsHuman()) {
                    // force bot to play next card
                    players[playerNumber].playCard();
                } else if(players[playerNumber].getIsHuman()) {
                    players[playerNumber].initClickListenerForCards();
                }
            }
        });
    }

    public void startNewRound() {
        this.actualRound = new Round(rounds.size() + 1, this.rootPane, this.players, this.positions);
        this.actualRound.showRoundStartedAlert();
        this.actualRound.mixCards();
        this.actualRound.distributeCardsToPlayers();
        this.actualRound.determineTrump();
        this.actualRound.prepareBoard();
        this.rounds.add(this.actualRound);
    }

    private void roundIsFinished() {
        this.takeCardsFromTable(false);
        this.announceRoundWinner();
        this.resetStichsAndPoints();

        boolean isGameOver = false;
        ArrayList<Player> gameWinnerPlayers = new ArrayList<>();
        for(Player player : this.players) {
            if(player.getScore() == 0) {
                isGameOver = true;
                gameWinnerPlayers.add(player);
            }
        }

        if(isGameOver) {
            this.gameOver(gameWinnerPlayers);
        } else {
            this.startNewRound();
        }
    }

    private void gameOver(ArrayList<Player> players) {
        StringBuilder winnerNames = new StringBuilder();
        String prefix = "";
        for(Player player : players) {
            winnerNames.append(prefix);
            prefix = " & ";
            winnerNames.append(player.getName());
        }

        // show dialog
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION );
        alert.setHeaderText("GAME OVER");
        alert.setContentText("Sieger: " + winnerNames);
        alert.initStyle(StageStyle.TRANSPARENT);

        ButtonType buttonTypeQuit = new ButtonType("Quit");
        ButtonType buttonTypeNewGame = new ButtonType("New Game");

        alert.getButtonTypes().setAll(buttonTypeQuit, buttonTypeNewGame);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == buttonTypeQuit){
            Platform.exit();
            System.exit(0);
        } else if (result.get() == buttonTypeNewGame) {
            Button newGame = (Button) rootPane.lookup("#buttonNewGame");
            newGame.fire();
        }
    }

    private void announceRoundWinner() {
        // determine round winner
        int[] points = this.actualRound.getPoints();
        int[] winnerPlayerIndexes = this.determineWinnerPlayerIndexes(points);

        StringBuilder winnerNames = new StringBuilder();
        String prefix = "";
        for(int index : winnerPlayerIndexes) {
            winnerNames.append(prefix);
            prefix = " & ";
            winnerNames.append(this.players[index].getName()).append(" (").append(points[index]).append(")");

            // reduce score of player
            this.players[index].reduceScore();
        }

        this.putzen(winnerPlayerIndexes);

        // show dialog
        Alert alert = new Alert(Alert.AlertType.INFORMATION );
        alert.setHeaderText("Rundensieger");
        alert.setContentText("Putzen: " + winnerNames);
        alert.initStyle(StageStyle.TRANSPARENT);
        alert.showAndWait();
    }

    private void putzen(int[] winnerPlayerIndexes) {
        for(int index : winnerPlayerIndexes) {
            ImageView imageViewPutzen = null;

            switch (this.positions[index]) {
                case BOTTOM:
                    imageViewPutzen = (ImageView) this.rootPane.getBottom().lookup(".imageViewPutzen");
                    break;
                case LEFT:
                    imageViewPutzen = (ImageView) this.rootPane.getLeft().lookup(".imageViewPutzen");
                    break;
                case TOP:
                    imageViewPutzen = (ImageView) this.rootPane.getTop().lookup(".imageViewPutzen");
                    break;
                case RIGHT:
                    imageViewPutzen = (ImageView) this.rootPane.getRight().lookup(".imageViewPutzen");
                    break;
            }

            if(imageViewPutzen != null) {
                int playerScore = this.players[index].getScore();
                String url = getClass().getResource("../../images/Putzen-" + playerScore + ".png").toExternalForm();
                imageViewPutzen.setImage(new Image(url));
            }
        }
    }

    private void takeCardsFromTable(boolean saveLastStich) {
        HBox cardContainer = (HBox) this.rootPane.lookup("#panePlayedCards");
        HBox cardsBeforeContainer = (HBox) this.rootPane.lookup("#panePlayedCardsBefore");

        HBox playerNamesContainer = (HBox) this.rootPane.lookup("#containerPlayerNamesOfCards");
        HBox playerNamesBeforeContainer = (HBox) this.rootPane.lookup("#containerPlayerNamesOfCardsBefore");

        Label labelLastStich = (Label) this.rootPane.lookup("#lastStich");

        cardsBeforeContainer.getChildren().clear();
        playerNamesBeforeContainer.getChildren().clear();
        labelLastStich.setText("");

        if(saveLastStich) {
            cardsBeforeContainer.getChildren().addAll(cardContainer.getChildren());
            for (Node node: cardsBeforeContainer.getChildren()) {
                ImageView imgView = ((ImageView) node);
                imgView.setFitWidth(50.0);
                imgView.setPreserveRatio(true);
            }

            playerNamesBeforeContainer.getChildren().addAll(playerNamesContainer.getChildren());
            for (Node node: playerNamesBeforeContainer.getChildren()) {
                Label label = ((Label) node);
                label.setMinWidth(50.0);
                label.setMaxWidth(50.0);
                label.setFont(new Font(10));
            }

            labelLastStich.setText("Vorheriger Stich");
            labelLastStich.setFont(new Font(10));
        }

        cardContainer.getChildren().clear();
        playerNamesContainer.getChildren().clear();
    }

    private void resetStichsAndPoints() {
        for(PlayerPosition position : this.positions) {
            Label labelStichs = null;
            Label labelPoints = null;

            switch (position) {
                case BOTTOM:
                    labelStichs = (Label) this.rootPane.lookup("#labelBottomPlayerStichs");
                    labelPoints = (Label) this.rootPane.lookup("#labelBottomPlayerPoints");
                    break;
                case LEFT:
                    labelStichs = (Label) this.rootPane.lookup("#labelLeftPlayerStichs");
                    labelPoints = (Label) this.rootPane.lookup("#labelLeftPlayerPoints");
                    break;
                case TOP:
                    labelStichs = (Label) this.rootPane.lookup("#labelTopPlayerStichs");
                    labelPoints = (Label) this.rootPane.lookup("#labelTopPlayerPoints");
                    break;
                case RIGHT:
                    labelStichs = (Label) this.rootPane.lookup("#labelRightPlayerStichs");
                    labelPoints = (Label) this.rootPane.lookup("#labelRightPlayerPoints");
                    break;
            }

            if (labelStichs != null && labelPoints != null) {
                labelStichs.setText("0");
                labelPoints.setText("0");
            }
        }
    }

    private int[] determineWinnerPlayerIndexes(int[] points) {
        int i, first, second, firstIndex, secondIndex;
        int arrSize = points.length;

        // two players
        if(arrSize == 2)
        {
            if(points[0] > points [1]) {
                return new int[]{0};
            } else if(points[0] < points [1]) {
                return new int[]{1};
            } else {
                return new int[]{0, 1};
            }
        }

        first = second = Integer.MIN_VALUE;
        firstIndex = secondIndex = -1;

        for (i = 0; i < arrSize ; i++)
        {
            if (points[i] > first) {
                second = first;
                secondIndex = firstIndex;
                first = points[i];
                firstIndex = i;
            }
            else if (points[i] > second && points[i] != first) {
                second = points[i];
                secondIndex = i;
            }
        }

        return new int[]{firstIndex, secondIndex};
    }
}
