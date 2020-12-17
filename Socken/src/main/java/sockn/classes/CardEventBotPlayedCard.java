package sockn.classes;

import javafx.event.EventType;

public class CardEventBotPlayedCard extends CardEvent {

    private static final EventType<CardEvent> BOT_PLAYED_CARD = new EventType(PLAYED_CARD, "BotPlayedCard");
    private final int playerNumber;
    private final Card playedCard;

    CardEventBotPlayedCard(int playerNumber, Card playedCard) {
        super(BOT_PLAYED_CARD);
        this.playerNumber = playerNumber;
        this.playedCard = playedCard;
    }

    @Override
    public void invokeHandler(CardEventHandler handler) {
        handler.onBotPlayedCard(this.playerNumber, this.playedCard);
    }
}