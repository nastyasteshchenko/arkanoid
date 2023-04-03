package oop.arkanoid.model;

public sealed class Block permits StandartBlock, IndestructibleBlock {
    private final double x;
    private final double y;
    private final double width;
    private final double height;

    protected int points;

    public Block(double x, double y, double width, double height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        points = 0;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    public int getPoints() {
        return points;
    }
}
