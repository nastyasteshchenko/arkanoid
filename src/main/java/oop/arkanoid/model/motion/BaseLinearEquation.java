package oop.arkanoid.model.motion;

import oop.arkanoid.model.CircleEquation;
import oop.arkanoid.model.Point;
import oop.arkanoid.model.QuadraticEquation;
import oop.arkanoid.model.barrier.CollisionPlace;

import java.util.List;

import static oop.arkanoid.model.ModelUtils.tan;

public class BaseLinearEquation implements LinearEquation {

    final double k;
    final double b;
    final double angle;

    public BaseLinearEquation(double angle, double b) {
        this.angle = angle;
        this.k = tan(angle);
        this.b = b;
    }

    @Override
    public List<Double> getIntersectionPoints(CircleEquation circleEquation) {

    /*
        { (x-centerX)^2 + (y - centerY)^2 = R^2
        { y=kx+b

       [1+k^2]x^2 + [2(k(b-centerY) - centerX)]x + [centerX^2 - R^2 + (b - centerY)^2] = 0
    */

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

