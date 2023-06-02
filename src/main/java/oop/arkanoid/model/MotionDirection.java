package oop.arkanoid.model;

public enum MotionDirection {
    LEFT,
    RIGHT;

    MotionDirection flip() {
        return this == LEFT ? RIGHT : LEFT;
    }

    MotionDirection flipByPlatform(double diffX) {
        return diffX <= 0 ? LEFT : RIGHT;
    }
}
