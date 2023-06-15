package oop.arkanoid;

import oop.arkanoid.model.Destroyable;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.function.Consumer;

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
        List<Subscriber> subscribers = this.subscribers.computeIfAbsent(eventType, v -> new ArrayList<>());
        subscribers.add(0, new Subscriber(handler));
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