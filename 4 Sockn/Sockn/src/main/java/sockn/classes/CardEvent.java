package sockn.classes;

import javafx.event.Event;
import javafx.event.EventType;

public abstract class CardEvent extends Event {

    static final EventType<CardEvent> PLAYED_CARD = new EventType(ANY);

    CardEvent(EventType<? extends Event> eventType) {
        super(eventType);
    }

    public abstract void invokeHandler(CardEventHandler handler);

}