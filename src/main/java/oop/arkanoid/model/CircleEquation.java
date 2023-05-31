package oop.arkanoid.model;

import java.util.List;

class CircleEquation {
    final Point center;
    final double radius;

    CircleEquation(Point center, double radius) {
        this.center = center;
        this.radius = radius;
    }

    List<Double> getY(double x) {
        //(x-a)^2 + (y-b)^2 = R^2, where (a, b) - center of circle
        //y^2 + [- 2b]y + [b^2 - R^2 + (x-a)^2] = 0
        return new QuadraticEquation(1, -2 * center.y(), center.y() * center.y() - radius * radius + Math.pow(x - center.x(), 2)).roots;
    }
}
