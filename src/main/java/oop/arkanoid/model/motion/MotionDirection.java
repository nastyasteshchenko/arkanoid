package oop.arkanoid.model.motion;

public enum MotionDirection {
    LEFT,
    RIGHT;

    MotionDirection flip() {
        return this == LEFT ? RIGHT : LEFT;
    }

}
