package sockn.classes;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.StageStyle;
import sockn.enums.CardColor;
import sockn.enums.PlayerPosition;
import sockn.utils.CardSymbolUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Round {
    private int roundNumber;
    private BorderPane board;
    private Player[] players;
    private PlayerPosition[] positions;
    private int[] points;
    private CardStack cardStack;
    private HashMap<Player, Card> actualPlayedCards;
    private Player playerOfFirstPlayedCard;
    private CardColor trump;

    public Round(int roundNumber, BorderPane board, Player[] players, PlayerPosition[] positions) {
        this.roundNumber = roundNumber;
        this.board = board;
        this.players = players;
        this.positions = positions;

        this.points = new int[players.length];
        this.actualPlayedCards = new HashMap<Player, Card>();
    }

    int[] getPoints() {
        return this.points;
    }

    CardColor getTrump() {
        return this.trump;
    }

    HashMap<Player, Card> getActualPlayedCards() {
        return this.actualPlayedCards;
    }

    void showRoundStartedAlert() {
        // reset trump
        Label trumpLabel = (Label) this.board.lookup("#trump");
        trumpLabel.setText("-");

        // show dialog
        Alert alert = new Alert(Alert.AlertType.INFORMATION );
        alert.setHeaderText("RUNDE " + this.roundNumber);
        alert.setContentText("Karten werden gemischt...");
        alert.initStyle(StageStyle.TRANSPARENT);
        alert.showAndWait();
    }

    void mixCards() {
        this.cardStack = new CardStack();
    }

    void distributeCardsToPlayers() {
        for(int i = 0; i < Constants.CARD_HAND_SIZE; i++) {
            for(Player player : players) {
                try {
                    player.receiveCard(cardStack.getCard());
                } catch (SocknException e) {
                    e.printStackTrace();
                }
            }
        }

        // init click listener for human player
        this.players[0].initClickListenerForCards();
    }

    void determineTrump() {
        switch (players.length) {
            case 2:
            case 3:
                try {
                    this.trump = this.cardStack.showLatestCard().getColor();
                } catch (SocknException e) {
                    e.printStackTrace();
                }
                break;
            case 4:
                Player lastPlayer = this.players[this.players.length - 1];
                ArrayList<Card> handCards = lastPlayer.getHandCards();
                this.trump = handCards.get(handCards.size() - 1).getColor();
                break;
        }

        Label trumpLabel = (Label) this.board.lookup("#trump");
        trumpLabel.setText(this.trump.toString());
    }

    void prepareBoard() {
        for(int i = 0; i < this.players.length; i++) {
            drawHandCards(this.positions[i], this.players[i]);
        }
    }

    private void drawHandCards(PlayerPosition position, Player player) {
        Group group = new Group();
        Pane pane;

        if(position == PlayerPosition.LEFT || position == PlayerPosition.RIGHT) {
            pane = new StackPane();
        } else {
            pane = new HBox();
        }

        ArrayList<Card> playerCards = player.getHandCards();

        if(player.getIsHuman()) {
            for (Card playerCard : playerCards) {
                pane.getChildren().add(playerCard.getImageView());
            }
        } else {
            int index = 0;
            for (Card playerCard : playerCards) {
                ImageView imgView = playerCard.getImageView();

                if(Constants.HIDDEN_CARDS) {
                    String url = getClass().getResource("../../images/Back.gif").toExternalForm();
                    imgView.setImage(new Image(url));
                }

                pane.getChildren().add(imgView);

                if(position == PlayerPosition.LEFT) {
                    StackPane.setAlignment(imgView, Pos.TOP_LEFT);
                    StackPane.setMargin(imgView, new Insets((18 * index), 0, 0, (5 * index)));
                } else if (position == PlayerPosition.RIGHT) {
                    StackPane.setAlignment(imgView, Pos.TOP_RIGHT);
                    StackPane.setMargin(imgView, new Insets((18 * index), (5 * index), 0, 0));
                }

                index++;
            }
        }

        group.getChildren().add(pane);

        StackPane stackPane = new StackPane();

        if(position == PlayerPosition.BOTTOM) {
            stackPane = (StackPane) this.board.getBottom().lookup("#bottomStackPane");
        } else if(position == PlayerPosition.TOP) {
            stackPane = (StackPane) this.board.getTop().lookup("#topStackPane");
        } else if(position == PlayerPosition.LEFT) {
            stackPane = (StackPane) this.board.getLeft().lookup("#leftStackPane");
        } else if (position == PlayerPosition.RIGHT) {
            stackPane = (StackPane) this.board.getRight().lookup("#rightStackPane");
        }

        stackPane.getChildren().add(group);
    }

    void addPlayedCard(Player player, Card card) {
        if(this.actualPlayedCards.size() == 0) {
            this.playerOfFirstPlayedCard = player;
        }
        this.actualPlayedCards.put(player, card);
    }

    int[] determineStich() {
        int points = 0;
        Card firstPlayedCard = this.actualPlayedCards.get(this.playerOfFirstPlayedCard);
        int highestCardValue = 0;
        int playerIndex = 0;

        for(Map.Entry<Player, Card> entry : this.actualPlayedCards.entrySet()) {
            Player player = entry.getKey();
            Card card = entry.getValue();

            boolean isTrump = card.getColor() == this.trump;
            points += CardSymbolUtil.getPoints(card.getSymbol(), isTrump);

            int cardValue = CardSymbolUtil.getValue(card.getSymbol(), isTrump);
            if(isTrump) {
                cardValue += 100;
            }
            if(cardValue > highestCardValue && (card.getColor() == firstPlayedCard.getColor() || isTrump)) {
                highestCardValue = cardValue;
                playerIndex = player.getNumber();
            }
        }

        // reset
        this.actualPlayedCards = new HashMap<Player, Card>();

        return new int[]{ playerIndex, points };
    }

    void drawStichsAndPoints(int playerIndex, int points) {
        Label labelStichs = null;
        Label labelPoints = null;

        switch (this.positions[playerIndex]) {
            case BOTTOM:
                labelStichs = (Label) this.board.lookup("#labelBottomPlayerStichs");
                labelPoints = (Label) this.board.lookup("#labelBottomPlayerPoints");
                break;
            case LEFT:
                labelStichs = (Label) this.board.lookup("#labelLeftPlayerStichs");
                labelPoints = (Label) this.board.lookup("#labelLeftPlayerPoints");
                break;
            case TOP:
                labelStichs = (Label) this.board.lookup("#labelTopPlayerStichs");
                labelPoints = (Label) this.board.lookup("#labelTopPlayerPoints");
                break;
            case RIGHT:
                labelStichs = (Label) this.board.lookup("#labelRightPlayerStichs");
                labelPoints = (Label) this.board.lookup("#labelRightPlayerPoints");
                break;
        }

        if(labelStichs != null && labelPoints != null) {
            int newStichs = (Integer.parseInt(labelStichs.getText()) + 1);
            labelStichs.setText(Integer.toString(newStichs));

            int newPoints = (Integer.parseInt(labelPoints.getText()) + points);
            labelPoints.setText(Integer.toString(newPoints));

            this.points[playerIndex] = newPoints;
        }
    }
}
