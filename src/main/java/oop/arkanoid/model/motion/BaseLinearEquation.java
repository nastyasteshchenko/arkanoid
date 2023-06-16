package oop.arkanoid.model.motion;

import oop.arkanoid.model.CircleEquation;
import oop.arkanoid.model.Point;
import oop.arkanoid.model.QuadraticEquation;

import java.util.List;

public record BaseLinearEquation(double angle, double b, double k) implements LinearEquation {

    /**
     * Calculates circle and line intersection points by solving the system:
     * <p>
     * { (x-centerX)^2 + (y - centerY)^2 = R^2<br>
     * { y=kx+b
     * <p>
     * In other words, by solving the quadratic equation:
     * <p>
     * [1+k^2]x^2 + [2(k(b-centerY) - centerX)]x + [centerX^2 - R^2 + (b - centerY)^2] = 0
     *
     * @param circleEquation equation of circle
     * @return points of circle and line intersection
     */
    @Override
    public List<Double> getIntersectionPoints(CircleEquation circleEquation) {

        double centerX = circleEquation.center().x();
        double centerY = circleEquation.center().y();
        double radius = circleEquation.radius();

        double a = 1 + k * k;
        double b = 2 * (k * (this.b - centerY) - centerX);
        double c = centerX * centerX - radius * radius + Math.pow(this.b - centerY, 2);

        return new QuadraticEquation(a, b, c).roots;
    }

    @Override
    public double getY(double x) {
        return k * x + b;
    }

    @Override
    public LinearEquation rotate(double angle, Point currPoint) {
        return LinearEquation.linearEquation(angle, currPoint);
    }

}

