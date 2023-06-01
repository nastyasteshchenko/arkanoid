package oop.arkanoid.model;

public enum HorizontalMotionDirection {
    LEFT,
    RIGHT;

    HorizontalMotionDirection flip() {
        return this == LEFT ? RIGHT : LEFT;
    }
}
