package oop.arkanoid.model;

import java.util.List;

class QuadraticEquation {

    // ax^2 + bx + c = 0
    final double a;
    final double b;
    final double c;

    final List<Double> roots;

    QuadraticEquation(double a, double b, double c) {
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
