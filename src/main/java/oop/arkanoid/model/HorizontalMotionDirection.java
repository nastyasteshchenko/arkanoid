package oop.arkanoid.model;

enum HorizontalMotionDirection {
    LEFT,
    RIGHT;

    HorizontalMotionDirection flip() {
        return this == LEFT ? RIGHT : LEFT;
    }
}