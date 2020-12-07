package sockn.classes;

import javafx.event.EventHandler;

public abstract class CardEventHandler implements EventHandler<CardEvent> {

    abstract void onBotPlayedCard(int playerNumber, Card playedCard);
    abstract void onHumanPlayedCard(int playerNumber, Card playedCard);

    @Override
    public void handle(CardEvent event) {
        event.invokeHandler(this);
    }
}