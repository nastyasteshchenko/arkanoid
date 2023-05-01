//package oop.arkanoid.model;
//
//public class Platform {
//    private double x;
//    private final double y;
//    private final double width;
//    private final double height;
//
//    public Platform(double x, double y, double width, double height) {
//        this.x = x;
//        this.y = y;
//        this.width = width;
//        this.height = height;
//    }
//
//    public void setX(double x) {
//        this.x = x;
//    }
//
//    public double getX() {
//        return x;
//    }
//
//    public double getY() {
//        return y;
//    }
//
//    public double getWidth() {
//        return width;
//    }
//
//    public double getHeight() {
//        return height;
//    }
//}

package oop.arkanoid.model;

class Platform extends Barrier {

    Platform(Point position, Point size) {
        super(position, size);
    }
}