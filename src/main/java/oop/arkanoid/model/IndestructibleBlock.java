package oop.arkanoid.model;

public final class IndestructibleBlock extends Block {
    public IndestructibleBlock(double x, double y, double width, double height) {
        super(x, y, width, height);
        points=0;
    }
}
