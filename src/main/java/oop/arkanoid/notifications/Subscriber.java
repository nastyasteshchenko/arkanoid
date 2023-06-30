package oop.arkanoid.notifications;

public abstract class Subscriber<T extends EventData> {
    final IEventType eventType;

    public Subscriber(IEventType eventType) {
        this.eventType = eventType;
    }

    public abstract void actionPerformed(T t);
}