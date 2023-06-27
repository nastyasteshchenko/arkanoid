package oop.arkanoid.model;

import java.util.List;

public record CircleEquation(Point center, double radius) {

    /**
     * Calculates y coordinate according to the given x coordinate by solving a quadratic equation:
     * <p>
     * y^2 - 2by + b^2 - R^2 + (x-a)^2 = 0
     * <p>
     * (a, b) - center of circle
     *
     * @param x x coordinate
     * @return y coordinate
     */
    public List<Double> getY(double x) {

        double a = 1;
        double b = -2 * center.y();
        double c = center.y() * center.y() - radius * radius + Math.pow(x - center.x(), 2);

        return new QuadraticEquation(a, b, c).roots;
    }

}
