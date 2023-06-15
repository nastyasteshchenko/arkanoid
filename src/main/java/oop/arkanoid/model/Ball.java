package oop.arkanoid.model;

import oop.arkanoid.model.barrier.Barrier;
import oop.arkanoid.model.barrier.Brick;
import oop.arkanoid.model.barrier.CollisionPlace;
import oop.arkanoid.model.barrier.Platform;

import java.util.ArrayList;
import java.util.List;

public class Ball {

    public final double radius;
    private LinearMotion motion;

    Ball(double radius, Point startPos) {
        this.radius = radius;
        double angle = Math.random() * 60 + 100;
        BaseLinearEquation ballLineEquation = new BaseLinearEquation(angle, BaseLinearEquation.countB(angle, startPos));
        this.motion = new LinearMotion(ballLineEquation, MotionDirection.RIGHT, 0, startPos);
    }

    public Point getPosition() {
        return motion.currPoint;
    }

    Point move(double step, List<Barrier> barriers) {

        detectCollisions(barriers);

        motion = motion.changeStepIfNeeded(step);

        return motion.nextPoint();
    }

    private void detectCollisions(List<Barrier> barriers) {
        CircleEquation circleEquation = new CircleEquation(motion.currPoint, radius);

        List<Brick> bricksToDelete = new ArrayList<>();
        boolean hasChangedDirection = false;

        for (Barrier barrier : barriers) {
            CollisionPlace collision = barrier.findCollision(circleEquation);
            if (collision == null) {
                continue;
            }

            if (!hasChangedDirection) {
                if (barrier instanceof Platform) {
                    double diffXBetweenBallAndCenterPlatform = barrier.position.x() + barrier.size.x() / 2 - motion.currPoint.x();
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

}
