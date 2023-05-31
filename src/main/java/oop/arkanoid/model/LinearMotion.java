package oop.arkanoid.model;

import java.util.List;

class LinearMotion {

    final BaseLinearEquation linearEquation;
    final MotionDirection direction;
    final double step;

    Point currPoint;

    LinearMotion(BaseLinearEquation linearEquation, MotionDirection direction, double step) {
        this.linearEquation = linearEquation;
        this.direction = direction;
        this.step = step;
    }

    Point nextPoint() {
        // TODO move to ctor
        // { (x1 - x0) ^ 2 + (y1 - yo) ^ 2 = step ^ 2
        // { y1 = kx1 + b
        // need to find x1
        // (1 + k^2)x1^2 - 2x0x1 + (x0^2 - y0^2 + b^2 -d^2) = 0
        QuadraticEquation qEquation = new QuadraticEquation(1 + linearEquation.k * linearEquation.k, -2 * currPoint.x(),
                currPoint.x() * currPoint.x() - currPoint.y() * currPoint.y() + linearEquation.b * linearEquation.b - step * step);
        List<Double> roots = qEquation.roots;
        // TODO get only one root depending on direction
        double newX = roots.get(0);
        double newY = linearEquation.getY(newX);
        return new Point(newX, newY);
    }

    LinearMotion changeStepIfNeeded(double newStep) {
        if (step == newStep) {
            return this;
        }
        return new LinearMotion(linearEquation, direction, newStep);
    }

    LinearMotion flipDirection() {
        return new LinearMotion(linearEquation, direction.flip(), step);
    }

    LinearMotion rotate() {
        return new LinearMotion((BaseLinearEquation) linearEquation.rotate(), direction, step);
    }
}