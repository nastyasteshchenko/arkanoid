package oop.arkanoid.model;

import java.util.List;

class BaseLinearEquation implements LinearEquation {

    final double k;
    final double angle;
    final double b;

    BaseLinearEquation(double angle, double b) {
        this.angle = angle;
        this.k = Math.tan(angle);
        this.b = b;
    }

    @Override
    public boolean hasIntersection(CircleEquation circleEquation) {
        QuadraticEquation qEquation = new QuadraticEquation(1 + k * k, 2 * k * b, b * b - circleEquation.radius * circleEquation.radius);
        for (Double root : qEquation.roots) {
            if (!circleEquation.getY(root).isEmpty()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public double getY(double x) {
        return k * x + b;
    }

    @Override
    public LinearEquation rotate() {
        return new BaseLinearEquation(-angle, b);
    }

}

class XLinearEquation implements LinearEquation {

    private final double x;

    XLinearEquation(double x) {
        this.x = x;
    }

    @Override
    public boolean hasIntersection(CircleEquation circleEquation) {
        List<Double> ys = circleEquation.getY(x);
        return !ys.isEmpty();
    }

    @Override
    public double getY(double x) {
        return Double.NaN;
    }

    @Override
    public LinearEquation rotate() {
        throw new UnsupportedOperationException();
    }
}

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