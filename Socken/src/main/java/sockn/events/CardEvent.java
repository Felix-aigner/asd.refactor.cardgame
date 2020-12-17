package sockn.events;

import javafx.event.Event;
import javafx.event.EventType;

public abstract class CardEvent extends Event {

    public static final EventType<CardEvent> PLAYED_CARD = new EventType(ANY);

    CardEvent(EventType<? extends Event> eventType) {
        super(eventType);
    }

    public abstract void invokeHandler(CardEventHandler handler);

}
