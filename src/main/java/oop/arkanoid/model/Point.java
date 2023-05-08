package oop.arkanoid.model;

public class Point implements Comparable<Point> {
    private double x;
    private double y;

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double x() {
        return x;
    }

    public double y() {
        return y;
    }

    void setX(double x) {
        this.x = x;
    }

    void setY(double y) {
        this.y = y;
    }

    @Override
    public int compareTo(Point o) {
        int yRes = Double.compare(y, o.y);
        if (yRes == -1) {
            return -1;
        }
        if (yRes == 1) {
            return 1;
        }
        return 0;
    }
}