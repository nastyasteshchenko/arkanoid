package oop.arkanoid.notifications;

import oop.arkanoid.model.barrier.Brick;

import java.util.ArrayList;
import java.util.List;

public class DestroyNotifications {
    private static DestroyNotifications instance;

    final List<DestroySubscriber> subscribers = new ArrayList<>();

    public static DestroyNotifications getInstance() {
        if (instance == null) {
            instance = new DestroyNotifications();
        }
        return instance;
    }

    public void publish(Brick brick) {
        for (DestroySubscriber subscriber : subscribers) {
            subscriber.update(brick);
        }
    }


    public void subscribe(DestroySubscriber subscriber) {
        subscribers.add(0, subscriber);
    }

    public void unsubscribe(DestroySubscriber subscriber) {
        subscribers.remove(subscriber);


    }


}
