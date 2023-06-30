package oop.arkanoid.notifications;

public enum EventTypeWithNoData implements IEventType {
    START_PLAYING_GAME,
    PAUSE,
    START_GAME,
    EXIT,
    RECORDS,
    BACK,
    ABOUT,
    RESTART_LEVEL,
    RESTART_GAME,
    DONT_SAVE_SCORE
}
