package oop.arkanoid.model;

import java.util.List;

public class QuadraticEquation {

    final double a;
    final double b;
    final double c;

    public final List<Double> roots;

    /**
     * Computes solutions to the equation of the form:<br/>
     * ax^2 + bx + c = 0
     *
     * @param a the quadratic coefficient
     * @param b the linear coefficient
     * @param c the constant coefficient
     */

    public QuadraticEquation(double a, double b, double c) {

        this.a = a;
        this.b = b;
        this.c = c;

        double d = b * b - 4 * a * c;

        if (d < 0) {
            roots = List.of();
        } else if (d == 0) {
            roots = List.of(-b / (2 * a));
        } else {
            double sqrtD = Math.sqrt(d);
            roots = List.of((-b - sqrtD) / (2 * a), (-b + sqrtD) / (2 * a));
        }
    }
}
