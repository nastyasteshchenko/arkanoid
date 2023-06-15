package oop.arkanoid.model.barrier;

public enum CollisionPlace {

    BOTTOM(false),
    TOP(false),
    LEFT(true),
    RIGHT(true);

    public final boolean needToChangeDirection;

    CollisionPlace(boolean needToChangeDirection) {
        this.needToChangeDirection = needToChangeDirection;
    }
}
