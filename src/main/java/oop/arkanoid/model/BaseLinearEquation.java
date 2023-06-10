package oop.arkanoid.model;

import oop.arkanoid.model.barriers.CollisionPlace;

import java.util.List;

public class BaseLinearEquation implements LinearEquation {

    final double k;
    final double b;
    private final double angle;

    BaseLinearEquation(double angle, double b) {
        this.angle = angle;
        this.k = Math.tan(Math.toRadians(angle));
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
    public LinearEquation rotate(Point currPoint, CollisionPlace place) {
        double angle = place.needToChangeDirection ? -180 - this.angle : -this.angle;
        return new BaseLinearEquation(angle, countB(angle, currPoint));
    }

    @Override
    public LinearEquation rotate(Point currPoint, double diffXBetweenBallAndCenterPlatform) {
        return new BaseLinearEquation(-90 - diffXBetweenBallAndCenterPlatform, countB(-90 - diffXBetweenBallAndCenterPlatform, currPoint));
    }


    static double countB(double angle, Point position) {
        return position.y() - position.x() * Math.tan(Math.toRadians(angle));
    }
}

