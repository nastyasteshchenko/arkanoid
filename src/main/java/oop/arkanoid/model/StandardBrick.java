package oop.arkanoid.model;

public final class StandardBrick extends Brick {
    public StandardBrick(double x, double y, double width, double height) {
        super(x, y, width, height);
        points = 5;
        countOfHits = 1;
    }
}
