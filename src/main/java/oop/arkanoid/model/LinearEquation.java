package oop.arkanoid.model;

interface LinearEquation {

    boolean hasIntersection(CircleEquation circleEquation);

    double getY(double x);

    LinearEquation rotate(Point currPoint, VerticalMotionDirection verticalMotionDirection, HorizontalMotionDirection horizontalMotionDirection);

    static LinearEquation linearEquation(double angle, double b, Point xBorders) {
        return new BaseLinearEquation(angle, b, xBorders);
    }

    static LinearEquation xlinearMotionEquation(double x, Point yBorders) {
        return new XLinearEquation(x, yBorders);
    }
}
