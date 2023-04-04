package oop.arkanoid.model;

public final class IndestructibleBrick extends Brick {
    public IndestructibleBrick(double x, double y, double width, double height) {
        super(x, y, width, height);
        points = 0;
    }
}
