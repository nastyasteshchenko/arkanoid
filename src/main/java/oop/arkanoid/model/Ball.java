package oop.arkanoid.model;

import oop.arkanoid.model.barriers.Barrier;
import oop.arkanoid.model.barriers.Brick;
import oop.arkanoid.model.barriers.CollisionPlace;
import oop.arkanoid.model.barriers.Platform;

import java.util.ArrayList;
import java.util.List;

public class Ball {

    public final double radius;
    Point position;
    private LinearMotion motion;

    Ball(double radius, Point startPos) {
        this.radius = radius;
        this.position = startPos;

        double angle = Math.random() * 60 + 100;
        BaseLinearEquation ballLineEquation = new BaseLinearEquation(angle, BaseLinearEquation.countB(angle, position), new Point(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY));
        this.motion = new LinearMotion(ballLineEquation, MotionDirection.RIGHT, 0, position);
    }

    Point move(double step, List<Barrier> barriers) {

        detectCollisions(barriers);

        motion = motion.changeStepIfNeeded(step);

        this.position = motion.nextPoint();
        return this.position;
    }

    private void detectCollisions(List<Barrier> barriers) {
        CircleEquation circleEquation = new CircleEquation(position, radius);

        List<Brick> bricksToDelete = new ArrayList<>();
        boolean hasChangedDirection = false;

        for (Barrier barrier : barriers) {
            CollisionPlace collision = barrier.findCollision(circleEquation);
            if (collision == null) {
                continue;
            }
            if (!hasChangedDirection) {

                if (barrier instanceof Platform) {
                    double diffXBetweenBallAndCenterPlatform = barrier.position.x() + barrier.size.x() / 2 - position.x();
                    motion = motion.flipDirection(diffXBetweenBallAndCenterPlatform);
                    motion = motion.rotate(diffXBetweenBallAndCenterPlatform);
                } else {
                    if (collision.needToChangeDirection) {
                        motion = motion.flipDirection();
                    }
                    motion = motion.rotate(collision);
                }
                hasChangedDirection = true;
            }

            if (barrier instanceof Brick brick) {
                brick.onHit();
                if (!brick.isAlive()) {
                    bricksToDelete.add(brick);
                }
            }
        }

        if (hasChangedDirection) {
            bricksToDelete.forEach(barriers::remove);
        }
    }

    public Point getPosition() {
        return position;
    }
}
