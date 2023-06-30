package oop.arkanoid.notifications;

import oop.arkanoid.model.barrier.Brick;
import oop.arkanoid.notifications.wrapper.DoubleWrapper;
import oop.arkanoid.notifications.wrapper.StringWrapper;

import java.util.*;

public class NotificationsManager {

    private static final NotificationsManager INSTANCE = new NotificationsManager();
    final Map<IEventType, List<Subscriber<EventData>>> subscribers = new HashMap<>();

    private NotificationsManager() {
    }

    public static NotificationsManager getInstance() {
        return INSTANCE;
    }

    public void publishBrickDestroyed(Brick brick) {
        publish(EventTypeWithData.DESTROY_BRICK, brick);
    }

    public void publishSaveScores(String author) {
        publish(EventTypeWithData.SAVE_SCORE, new StringWrapper(author));
    }

    public void publishMovePlatform(double x) {
        publish(EventTypeWithData.MOVE_PLATFORM, new DoubleWrapper(x));
    }

    public void publish(EventTypeWithNoData eventType) {
        publish(eventType, null);
    }

    public void subscribe(Subscriber<? extends EventData> subscriber) {
        List<Subscriber<EventData>> subscribers = this.subscribers.computeIfAbsent(subscriber.eventType, v -> new ArrayList<>());
        //noinspection unchecked
        subscribers.add((Subscriber<EventData>) subscriber);
    }

    public void unsubscribe(Subscriber<? extends EventData> subscriber) {
        List<Subscriber<EventData>> subscribers = this.subscribers.get(subscriber.eventType);
        subscribers.remove(subscriber);
    }

    private <T extends EventData> void publish(IEventType eventType, T eventInfo) {
        for (Subscriber<? super EventData> s : subscribers.getOrDefault(eventType, Collections.emptyList())) {
            s.actionPerformed(eventInfo);
        }
    }

}

