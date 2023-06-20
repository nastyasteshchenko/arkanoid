package oop.arkanoid.notifications;

import oop.arkanoid.model.barrier.Brick;

import java.util.*;
import java.util.function.Consumer;

public class Notifications {
    private static Notifications instance;

    private final EnumMap<EventType, List<Subscriber>> subscribers = new EnumMap<>(EventType.class);

    private final HashMap<Object, EnumMap<EventType, Subscriber>> subscriberToObjectMapping = new HashMap<>();

    public static Notifications getInstance() {
        if (instance == null) {
            instance = new Notifications();
        }
        return instance;
    }

    public void publish(EventType type, Brick brick) {
        List<Subscriber> subscribers = this.subscribers.get(type);
        for (Subscriber subscriber : subscribers) {
            subscriber.handler().accept(brick);
        }
    }

    public void publish(EventType type, double x) {
        List<Subscriber> subscribers = this.subscribers.get(type);
        for (Subscriber subscriber : subscribers) {
            subscriber.handler().accept(x);
        }
    }

    public void publish(EventType type) {
        List<Subscriber> subscribers = this.subscribers.get(type);
        for (Subscriber subscriber : subscribers) {
            subscriber.handler().accept(null);
        }
    }

    public void subscribe(EventType type, Object subscriberObj, Consumer<Object> handler) {
        EnumMap<EventType, Subscriber> mapping = this.subscriberToObjectMapping.computeIfAbsent(subscriberObj, v -> new EnumMap<>(EventType.class));
        List<Subscriber> subscribers = this.subscribers.computeIfAbsent(type, v -> new ArrayList<>());
        Subscriber subscriber = new Subscriber(handler);
        mapping.put(type, subscriber);
        subscribers.add(0, subscriber);
    }

    public void unsubscribe(EventType type, Object subscriberObj) {
        List<Subscriber> subscribers = this.subscribers.computeIfAbsent(type, V -> new ArrayList<>());
        Subscriber subscriber = subscriberToObjectMapping.get(subscriberObj).get(type);
        subscribers.remove(subscriber);
    }
}

