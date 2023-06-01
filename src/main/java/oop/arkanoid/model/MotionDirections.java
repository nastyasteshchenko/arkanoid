package oop.arkanoid.model;

enum VerticalMotionDirection {
    UP,
    DOWN;

    VerticalMotionDirection flip() {
        return this == UP ? DOWN : UP;
    }
}

enum HorizontalMotionDirection {
    LEFT,
    RIGHT;

    HorizontalMotionDirection flip() {
        return this == LEFT ? RIGHT : LEFT;
    }
}