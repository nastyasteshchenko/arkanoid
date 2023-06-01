package oop.arkanoid.model;

public enum VerticalMotionDirection {
    UP,
    DOWN;

    VerticalMotionDirection flip() {
        return this == UP ? DOWN : UP;
    }
}

