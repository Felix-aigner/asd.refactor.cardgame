package sockn.events;

import javafx.event.EventType;
import sockn.gamelogic.Card;

public class CardEventHumanPlayedCard extends CardEvent {

    private static final EventType<CardEvent> HUMAN_PLAYED_CARD = new EventType(PLAYED_CARD, "HumanPlayedCard");
    private final int playerNumber;
    private final Card playedCard;

    public CardEventHumanPlayedCard(int playerNumber, Card playedCard) {
        super(HUMAN_PLAYED_CARD);
        this.playerNumber = playerNumber;
        this.playedCard = playedCard;
    }

    @Override
    public void invokeHandler(CardEventHandler handler) {
        handler.onHumanPlayedCard(this.playerNumber, this.playedCard);
    }
}
