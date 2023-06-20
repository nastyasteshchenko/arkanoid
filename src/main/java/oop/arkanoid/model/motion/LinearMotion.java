package oop.arkanoid.model.motion;

import oop.arkanoid.model.Point;
import oop.arkanoid.model.QuadraticEquation;

public class LinearMotion {

    public final MotionDirection direction;
    //getter
    public Point currPoint;
    private final double step;
    private final BaseLinearEquation linearEquation;

    public LinearMotion(BaseLinearEquation linearEquation, MotionDirection direction, double step, Point startPos) {
        this.linearEquation = linearEquation;
        this.direction = direction;
        this.step = step;
        currPoint = startPos;
    }

    /**
     * Calculate next point of motion for ball by solving the system:
     * <p>
     * { (x-centerX)^2 + (y - centerY)^2 = R^2<br>
     * { y=kx+b
     * <p>
     * In other words, by solving the quadratic equation:
     * <p>
     * [1+k^2]x^2 + [2(k(b-centerY) - centerX)]x + [centerX^2 - R^2 + (b - centerY)^2] = 0
     *
     * @return next point for ball motion
     */

    public Point nextPoint() {

        double a = 1 + linearEquation.k() * linearEquation.k();
        double b = 2 * (linearEquation.k() * (linearEquation.b() - currPoint.y()) - currPoint.x());
        double c = currPoint.x() * currPoint.x() - step * step + Math.pow(linearEquation.b() - currPoint.y(), 2);

        QuadraticEquation qEquation = new QuadraticEquation(a, b, c);

        double firstRoot = qEquation.roots.get(0);
        double secondRoot = qEquation.roots.get(1);

        double newX;

        if (direction == MotionDirection.LEFT) {
            newX = firstRoot <= currPoint.x() ? firstRoot : secondRoot;
        } else {
            newX = firstRoot >= currPoint.x() ? firstRoot : secondRoot;
        }

        double newY = linearEquation.getY(newX);

        currPoint = new Point(newX, newY);

        return currPoint;
    }

    /**
     * Flips direction of ball.
     *
     * @return new linear motion with new direction
     */
    public LinearMotion flipDirection() {
        return new LinearMotion(linearEquation, direction.flip(), step, currPoint);
    }

    /**
     * Rotates ball motion linear equation.
     *
     * @param angle - angle of rotate
     * @return new linear motion with new linear equation according to new angle
     */
    public LinearMotion rotate(double angle) {
        return new LinearMotion((BaseLinearEquation) linearEquation.rotate(angle, currPoint), direction, step, currPoint);
    }

    public double getMotionAngle() {
        return linearEquation.angle();
    }
}