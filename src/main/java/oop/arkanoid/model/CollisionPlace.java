package oop.arkanoid.model;

enum CollisionPlace {
    TOP(false),
    LEFT(true),
    RIGHT(true),
    BOTTOM(false);

    final boolean needToChangeDirection;

    CollisionPlace(boolean needToChangeDirection) {
        this.needToChangeDirection = needToChangeDirection;
    }
}
