package oop.arkanoid.model;

public final class StandardBlock extends Block {
    public StandardBlock(double x, double y, double width, double height) {
        super(x, y, width, height);
        points = 5;
    }
}
