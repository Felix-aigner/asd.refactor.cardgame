package sockn.gamelogic;

import javafx.event.Event;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import sockn.events.CardEventBotPlayedCard;
import sockn.events.CardEventHumanPlayedCard;
import sockn.resources.config.Config;

import java.util.ArrayList;

public class Player {
    public static final String PANE_PLAYED_CARDS = "#panePlayedCards";
    private int number;
    private String name;
    private boolean isHuman;
    private BorderPane board;
    private ArrayList<Card> handCards;
    private int score = Config.PLAYER_LIVES;

    public Player(int number, String name, boolean isHuman, BorderPane board) {
        this.number = number;
        this.name = name;
        this.isHuman = isHuman;
        this.board = board;
        this.handCards = new ArrayList<>();
    }

    ArrayList<Card> getHandCards() {
        return this.handCards;
    }

    boolean getIsHuman() {
        return this.isHuman;
    }

    String getName() {
        return this.name;
    }

    int getNumber() {
        return this.number;
    }

    int getScore() {
        return this.score;
    }

    void reduceScore() {
        this.score -= 1;
    }

    void receiveCard(Card card) {
        this.handCards.add(card);
    }

    void initClickListenerForCards() {
        for(Card card : this.handCards) {
            card.getImageView().setOnMouseClicked(e -> {
                this.playCard(e);
                this.fireCardEvent(card);
            });
        }
    }

    void removeClickListenerForCards() {
        for(Card card : this.handCards) {
            card.getImageView().setOnMouseClicked(null);
        }
    }

    void playCard() {
        int indexOfCardToPlay = (int)(Math.random() * this.handCards.size());

        Card card = this.handCards.get(indexOfCardToPlay);

        card.setImage();

        this.putCardOnBoard(card.getImageView());

        this.handCards.remove(indexOfCardToPlay);

        this.fireCardEvent(card);
    }

    private void playCard(Event e) {
        ImageView imgView = (ImageView) e.getTarget();

        int index = -1;
        for(int i = 0; i < this.handCards.size(); i++) {
            if(this.handCards.get(i).getImageView() == imgView) {
                index = i;
                break;
            }
        }
        this.handCards.remove(index);

        imgView.setOnMouseClicked(null);

        this.putCardOnBoard(imgView);
    }

    private void putCardOnBoard(ImageView imgView) {
        Pane parent = (Pane) imgView.getParent();

        HBox cardContainer = (HBox) this.board.lookup(PANE_PLAYED_CARDS);

        cardContainer.getChildren().add(imgView);

        HBox playerNamesContainer = (HBox) this.board.lookup("#containerPlayerNamesOfCards");
        Label labelPayerName = new Label();
        labelPayerName.setText(this.name);
        labelPayerName.setMinWidth(73.0);
        labelPayerName.setMaxWidth(73.0);
        playerNamesContainer.getChildren().add(labelPayerName);

        parent.getChildren().remove(imgView);
    }

    private void resetPlayedCardsOnBoard() {
        HBox cardContainer = (HBox) this.board.lookup(PANE_PLAYED_CARDS);
        cardContainer.getChildren().clear();
    }

    private void fireCardEvent(Card playedCard) {
        if(this.isHuman) {
            this.board.fireEvent(new CardEventHumanPlayedCard(this.number, playedCard));
        } else {
            this.board.fireEvent(new CardEventBotPlayedCard(this.number, playedCard));
        }
    }
}
