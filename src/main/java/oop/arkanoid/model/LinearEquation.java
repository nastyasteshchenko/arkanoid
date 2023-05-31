package oop.arkanoid.model;

interface LinearEquation {

    boolean hasIntersection(CircleEquation circleEquation);

    double getY(double x);

    LinearEquation rotate();

    static LinearEquation linearEquation(double angle, double b) {
        return new BaseLinearEquation(angle, b);
    }

    static LinearEquation xlinearMotionEquation(double x) {
        return new XLinearEquation(x);
    }
}
