package oop.arkanoid.notifications;

import oop.arkanoid.model.barrier.Brick;

import java.util.ArrayList;
import java.util.List;

public class NotificationsAboutDestroy {

    private static NotificationsAboutDestroy instance;

    final List<Subscriber> subscribers = new ArrayList<>();

    public static NotificationsAboutDestroy getInstance() {
        if (instance == null) {
            instance = new NotificationsAboutDestroy();
        }
        return instance;
    }

    public void publish(Brick brick) {
        for (Subscriber subscriber : subscribers) {
            subscriber.update(brick);
        }
    }

    public void subscribe(Subscriber subscriber) {
        subscribers.add(0, subscriber);
    }

    public void unsubscribe(Subscriber subscriber) {
        subscribers.remove(subscriber);
    }

}

