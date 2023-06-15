package oop.arkanoid;

import oop.arkanoid.model.Destroyable;

import java.util.ArrayList;
import java.util.List;

public class DestroyingNotifications {

    private static DestroyingNotifications instance;

    final List<Subscriber> subscribers = new ArrayList<>();

    public static DestroyingNotifications getInstance() {
        if (instance == null) {
            instance = new DestroyingNotifications();
        }
        return instance;
    }

    public void publish(Destroyable destroyable) {
        for (Subscriber subscriber : subscribers) {
            subscriber.update(destroyable);
        }
    }

    public void subscribe(Subscriber subscriber) {
        subscribers.add(0, subscriber);
    }

    public void unsubscribe(Subscriber subscriber) {
        subscribers.remove(subscriber);
    }

}

