package oop.arkanoid.model;

public class Point {
    //сделать final, создавать новую точку
    private double x;
    private double y;

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    //TODO проверить надо или нет
    public Double x() {
        return x;
    }

    public Double y() {
        return y;
    }

    public void setX(double x) {
        this.x = x;
    }

    void setY(double y) {
        this.y = y;
    }

}