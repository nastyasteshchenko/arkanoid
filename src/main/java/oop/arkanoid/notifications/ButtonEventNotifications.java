package oop.arkanoid.notifications;

import java.util.ArrayList;
import java.util.List;

public class ButtonEventNotifications {
    private static ButtonEventNotifications instance;

    final List<ButtonSubscriber> subscribers = new ArrayList<>();

    public static ButtonEventNotifications getInstance() {
        if (instance == null) {
            instance = new ButtonEventNotifications();
        }
        return instance;
    }

    public void publish(ButtonEventType type) {
        for (ButtonSubscriber subscriber : subscribers) {
            subscriber.update(type);
        }
    }


    public void subscribe(ButtonSubscriber subscriber) {
        subscribers.add(0, subscriber);
    }

    public void unsubscribe(ButtonSubscriber subscriber) {
        subscribers.remove(subscriber);


    }
}
