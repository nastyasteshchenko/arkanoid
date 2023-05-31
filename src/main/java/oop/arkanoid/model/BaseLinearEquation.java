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
        //(x-centerX)^2 + (y - centerY)^2 = R^2  and  y=kx+b
        //[1+k^2]x^2 + [2(k(b-centerY) - centerX)]x + [centerX^2 - R^2 + (b - centerY)^2] = 0
        QuadraticEquation qEquation = new QuadraticEquation(1 + k * k, 2 * (k * (b - circleEquation.center.y()) - circleEquation.center.x()),
                circleEquation.center.x() * circleEquation.center.x() - circleEquation.radius * circleEquation.radius + Math.pow(b - circleEquation.center.y(), 2));
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

