package sockn.events;

import javafx.event.EventType;
import sockn.classes.Card;

public class CardEventBotPlayedCard extends CardEvent {

    private static final EventType<CardEvent> BOT_PLAYED_CARD = new EventType(PLAYED_CARD, "BotPlayedCard");
    private final int playerNumber;
    private final Card playedCard;

    public CardEventBotPlayedCard(int playerNumber, Card playedCard) {
        super(BOT_PLAYED_CARD);
        this.playerNumber = playerNumber;
        this.playedCard = playedCard;
    }

    @Override
    public void invokeHandler(CardEventHandler handler) {
        handler.onBotPlayedCard(this.playerNumber, this.playedCard);
    }
}
