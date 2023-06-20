package oop.arkanoid.notifications;

import java.util.ArrayList;
import java.util.List;

public class MovePlatformNotifications {
    private static MovePlatformNotifications instance;

    final List<MovePlatformSubscriber> subscribers = new ArrayList<>();

    public static MovePlatformNotifications getInstance() {
        if (instance == null) {
            instance = new MovePlatformNotifications();
        }
        return instance;
    }

    public void publish(double x) {
        for (MovePlatformSubscriber subscriber : subscribers) {
            subscriber.update(x);
        }
    }


    public void subscribe(MovePlatformSubscriber subscriber) {
        subscribers.add(0, subscriber);
    }

    public void unsubscribe(MovePlatformSubscriber subscriber) {
        subscribers.remove(subscriber);


    }
}

