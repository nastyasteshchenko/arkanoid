package oop.arkanoid.model;

public final class DoubleHitBrick extends Brick{
    public DoubleHitBrick(double x, double y, double width, double height) {
        super(x, y, width, height);
        points=10;
        countOfHits=2;
    }
}
