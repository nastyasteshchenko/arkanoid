package oop.arkanoid;

import oop.arkanoid.model.Destroyable;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.function.Consumer;

// TODO probably there is some internal implementation of subscribers in JavaFX.
// TODO if not we can implement our own like this
//TODO почитать про наблюдателя
public class Notifications {

    private static Notifications instance;

    final EnumMap<EventType, List<Subscriber>> subscribers = new EnumMap<>(EventType.class);

    public static Notifications getInstance() {
        if (instance == null) {
            instance = new Notifications();
        }
        return instance;
    }

    public void publish(EventType et, Destroyable destroyable) {
        List<Subscriber> subscribers = this.subscribers.get(et);
        for (Subscriber subscriber : subscribers) {
            subscriber.handler.accept(destroyable);
        }
    }

    public void subscribe(EventType eventType, Consumer<Destroyable> handler) {
        List<Subscriber> subscribers = this.subscribers.computeIfAbsent(EventType.DESTROY, v -> new ArrayList<>());
        subscribers.add(new Subscriber(handler));
    }

    public void unsubscribe(EventType eventType, Object subscriber) {

    }

    static class Subscriber {


        private final Consumer<Destroyable> handler;

        public Subscriber(Consumer<Destroyable> handler) {

            this.handler = handler;
        }
    }

    public enum EventType {
        DESTROY
    }

}