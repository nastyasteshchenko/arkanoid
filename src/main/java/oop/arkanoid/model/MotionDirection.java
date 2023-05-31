package oop.arkanoid.model;

enum MotionDirection {
    LEFT,
    RIGHT
    ;

    MotionDirection flip() {
        return this == LEFT ? RIGHT : LEFT;
    }
}