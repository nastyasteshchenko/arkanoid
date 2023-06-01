package oop.arkanoid.model;

enum VerticalMotionDirection {

    UP,
    DOWN;

    VerticalMotionDirection flip() {
        return this == UP ? DOWN : UP;
    }
}