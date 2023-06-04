package oop.arkanoid.model;

enum CollisionPlace {

    BOTTOM(false),
    TOP(false),
    LEFT(true),
    RIGHT(true);

    final boolean needToChangeDirection;

    CollisionPlace(boolean needToChangeDirection) {
        this.needToChangeDirection = needToChangeDirection;
    }
}
