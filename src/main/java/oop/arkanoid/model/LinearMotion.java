package oop.arkanoid.model;

import java.util.List;

class LinearMotion {

    final BaseLinearEquation linearEquation;
    final MotionDirection direction;
    final double step;

    Point currPoint;

    LinearMotion(BaseLinearEquation linearEquation, MotionDirection direction, double step, Point startPos) {
        this.linearEquation = linearEquation;
        this.direction = direction;
        this.step = step;
        currPoint = startPos;
    }

    Point nextPoint() {
        // TODO move to ctor
        // { (x-centerX)^2 + (y - centerY)^2 = R^2
        // { y=kx+b
        //[1+k^2]x^2 + [2(k(b-centerY) - centerX)]x + [centerX^2 - R^2 + (b - centerY)^2] = 0
        QuadraticEquation qEquation = new QuadraticEquation(1 + linearEquation.k * linearEquation.k, 2 * (linearEquation.k * (linearEquation.b - currPoint.y()) - currPoint.x()),
                currPoint.x() * currPoint.x() - step * step + Math.pow(linearEquation.b - currPoint.y(), 2));
//        QuadraticEquation qEquation = new QuadraticEquation(1 + linearEquation.k * linearEquation.k, -2 * currPoint.x(),
//                currPoint.x() * currPoint.x() - currPoint.y() * currPoint.y() + linearEquation.b * linearEquation.b - step * step);
        List<Double> roots = qEquation.roots;
        // TODO get only one root depending on direction
        double newX = roots.get(0);
        double newY = linearEquation.getY(newX);
        currPoint.setX(newX);
        currPoint.setY(newY);
        return new Point(newX, newY);
    }

    LinearMotion changeStepIfNeeded(double newStep) {
        if (step == newStep) {
            return this;
        }
        return new LinearMotion(linearEquation, direction, newStep, currPoint);
    }

    LinearMotion flipDirection() {
        return new LinearMotion(linearEquation, direction.flip(), step, currPoint);
    }

    LinearMotion rotate() {
        return new LinearMotion((BaseLinearEquation) linearEquation.rotate(currPoint), direction, step, currPoint);
    }
}