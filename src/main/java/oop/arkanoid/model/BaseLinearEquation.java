package oop.arkanoid.model;

import java.util.List;

class BaseLinearEquation implements LinearEquation {

    final double k;
    final double angle;
    final double b;

    final Point xBorders;

    BaseLinearEquation(double angle, double b, Point xBorders) {
        this.angle = angle;
        this.k = Math.tan(angle);
        this.b = b;
        this.xBorders = xBorders;
    }

    @Override
    public boolean hasIntersection(CircleEquation circleEquation) {
        /*
         { (x-centerX)^2 + (y - centerY)^2 = R^2
         { y=kx+b

        [1+k^2]x^2 + [2(k(b-centerY) - centerX)]x + [centerX^2 - R^2 + (b - centerY)^2] = 0
        */
        QuadraticEquation qEquation = new QuadraticEquation(1 + k * k, 2 * (k * (b - circleEquation.center().y()) - circleEquation.center().x()),
                circleEquation.center().x() * circleEquation.center().x() - circleEquation.radius() * circleEquation.radius() + Math.pow(b - circleEquation.center().y(), 2));
        for (Double root : qEquation.roots) {
            if (!GameLevel.Builder.inSegment(xBorders.x(), xBorders.y(), root)){
              //  System.out.println("boo");
                continue;
            }
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
    public LinearEquation rotate(Point currPoint) {
        return new BaseLinearEquation(-angle, recountB(-180-angle, currPoint), xBorders);
    }

    static double recountB(double angle, Point position) {
        return position.y() - position.x() * Math.tan(angle);
    }

}

