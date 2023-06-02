package oop.arkanoid.model;

interface LinearEquation {

    boolean hasIntersection(CircleEquation circleEquation);

    double getY(double x);

    LinearEquation rotate(Point currPoint, CollisionPlace place);

    LinearEquation rotate(Point currPoint, double diffX);

    static LinearEquation linearEquation(double angle, double b, Point xBorders) {
        return new BaseLinearEquation(angle, b, xBorders);
    }

    static LinearEquation xLinearMotionEquation(double x, Point yBorders) {
        return new XLinearEquation(x, yBorders);
    }
}
