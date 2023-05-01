package oop.arkanoid;

import oop.arkanoid.model.Destroyable;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.function.Consumer;

// TODO probably there is some internal implementation of subscribers in JavaFX.
// TODO if not we can implement our own like this
public class Notifications {

    final EnumMap<EventType, List<Subscriber>> subscribers = new EnumMap<>(EventType.class);

    // TODO singleton
    public static Notifications getInstance() {
        return new Notifications();
    }

    public void publish(EventType et, Destroyable destroyable) {
        List<Subscriber> subscribers = this.subscribers.get(et);
        for (Subscriber subscriber : subscribers) {

        }
    }

    public void subscribe(EventType eventType, Object subscriber, Consumer<Destroyable> handler) {
        List<Subscriber> subscribers = this.subscribers.computeIfAbsent(EventType.DESTROY, v -> new ArrayList<>());
        subscribers.add(new Subscriber());
    }

    public void unsubscribe(EventType eventType, Object subscriber) {

    }

    static class Subscriber {

    }

    public enum EventType {
        DESTROY
    }

}