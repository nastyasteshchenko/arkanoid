package oop.arkanoid.notifications;

import oop.arkanoid.model.barrier.Brick;

import java.util.*;
import java.util.function.Consumer;

public class NotificationsManager {
    private static final NotificationsManager INSTANCE = new NotificationsManager();

    private final EnumMap<EventType, List<Subscriber>> subscribers = new EnumMap<>(EventType.class);

    private final HashMap<Object, EnumMap<EventType, Subscriber>> subscriberToObjectMapping = new HashMap<>();

    private NotificationsManager() {
    }

    public static NotificationsManager getInstance() {
        return INSTANCE;
    }

    public void publish(EventType type, Brick brick) {
        List<Subscriber> subscribers = this.subscribers.get(type);
        for (Subscriber subscriber : subscribers) {
            subscriber.handler().accept(brick);
        }
    }

    public void publish(EventType type, Double x) {
        List<Subscriber> subscribers = this.subscribers.get(type);
        for (Subscriber subscriber : subscribers) {
            subscriber.handler().accept(x);
        }
    }

    public void publish(EventType type, String name) {
        List<Subscriber> subscribers = this.subscribers.get(type);
        for (Subscriber subscriber : subscribers) {
            subscriber.handler().accept(name);
        }
    }

    public void publish(EventType type) {
        List<Subscriber> subscribers = this.subscribers.get(type);
        for (Subscriber subscriber : subscribers) {
            subscriber.handler().accept(null);
        }
    }

    public void subscribe(EventType type, Object subObj, Consumer<Object> handler) {
        EnumMap<EventType, Subscriber> mapping = this.subscriberToObjectMapping.computeIfAbsent(subObj, v -> new EnumMap<>(EventType.class));
        List<Subscriber> subscribers = this.subscribers.computeIfAbsent(type, v -> new ArrayList<>());
        Subscriber subscriber = new Subscriber(handler);
        mapping.put(type, subscriber);
        subscribers.add(0, subscriber);
    }

    public void unsubscribe(EventType type, Object subObj) {
        EnumMap<EventType, Subscriber> subscriberEnumMap = subscriberToObjectMapping.get(subObj);
        Subscriber subscriber = subscriberEnumMap.get(type);
        subscriberToObjectMapping.remove(subscriberEnumMap);
        List<Subscriber> subscribers = this.subscribers.get(type);
        subscribers.remove(subscriber);
    }
}
