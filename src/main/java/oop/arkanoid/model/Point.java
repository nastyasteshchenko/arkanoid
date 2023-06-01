package oop.arkanoid.model;

public class Point {
    private Double x;
    private Double y;

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Double x() {
        return x;
    }

    public Double y() {
        return y;
    }

    void setX(double x) {
        this.x = x;
    }

    void setY(double y) {
        this.y = y;
    }

}