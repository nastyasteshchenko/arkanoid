package oop.arkanoid.notifications;

public enum EventTypeWithData implements IEventType {
    DESTROY_BRICK,
    MOVE_PLATFORM,
    SAVE_SCORE
}
