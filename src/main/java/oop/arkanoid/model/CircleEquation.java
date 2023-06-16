package oop.arkanoid.model;

import java.util.List;

public record CircleEquation(Point center, double radius) {

    /**
     * @param x coordina
     * @return
     */
    public List<Double> getY(double x) {
    /*
       (x-a)^2 + (y-b)^2 = R^2, where (a, b) - center of circle
       y^2 + [- 2b]y + [b^2 - R^2 + (x-a)^2] = 0
    */
        double a = 1;
        double b = -2 * center.y();
        double c = center.y() * center.y() - radius * radius + Math.pow(x - center.x(), 2);

        return new QuadraticEquation(a, b, c).roots;
    }

}
