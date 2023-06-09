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

        QuadraticEquation qEquation = new QuadraticEquation(1 + k * k, 2 * (k * (b - circleEquation.center().y()) - circleEquation.center().x()),
                circleEquation.center().x() * circleEquation.center().x() - circleEquation.radius() * circleEquation.radius() + Math.pow(b - circleEquation.center().y(), 2));
        return qEquation.roots;
    }

    @Override
    public double getY(double x) {
        return k * x + b;
    }

    @Override
    public LinearEquation rotate(Point currPoint, CollisionPlace place) {
        if (place.needToChangeDirection) {
            return new BaseLinearEquation(-180 - angle, countB(-180 - angle, currPoint));
        } else {
            return new BaseLinearEquation(-angle, countB(-angle, currPoint));
        }
    }

    @Override
    public LinearEquation rotate(Point currPoint, double diffXBetweenBallAndCenterPlatform) {
        return new BaseLinearEquation(-90 - diffXBetweenBallAndCenterPlatform, countB(-90 - diffXBetweenBallAndCenterPlatform, currPoint));
    }


    static double countB(double angle, Point position) {
        return position.y() - position.x() * Math.tan(Math.toRadians(angle));
    }
}

