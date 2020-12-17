package sockn.events;

import javafx.event.EventHandler;
import sockn.classes.Card;

public abstract class CardEventHandler implements EventHandler<CardEvent> {

    public abstract void onBotPlayedCard(int playerNumber, Card playedCard);

    public abstract void onHumanPlayedCard(int playerNumber, Card playedCard);

    @Override
    public void handle(CardEvent event) {
        event.invokeHandler(this);
    }
}
