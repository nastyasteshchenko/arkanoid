package oop.arkanoid.model;

import oop.arkanoid.model.barrier.Barrier;
import oop.arkanoid.model.barrier.Brick;
import oop.arkanoid.model.barrier.CollisionPlace;
import oop.arkanoid.model.barrier.Platform;
import oop.arkanoid.model.motion.BaseLinearEquation;
import oop.arkanoid.model.motion.LinearEquation;
import oop.arkanoid.model.motion.LinearMotion;
import oop.arkanoid.model.motion.MotionDirection;

import java.util.ArrayList;
import java.util.List;

public class Ball {

    public final double radius;
    private LinearMotion motion;

    Ball(double radius, Point startPos) {
        this.radius = radius;
        BaseLinearEquation ballLineEquation = LinearEquation.linearEquation(-60, startPos);
        this.motion = new LinearMotion(ballLineEquation, MotionDirection.RIGHT, 1.5, startPos);
    }

    public Point position() {
        return motion.currPosition();
    }

    Point move(List<Barrier> barriers) {

        detectCollisions(barriers);

        return motion.nextPoint();
    }

    private void detectCollisions(List<Barrier> barriers) {
        CircleEquation circleEquation = new CircleEquation(position(), radius);

        List<Brick> bricksToDelete = new ArrayList<>();

        for (Barrier barrier : barriers) {
            CollisionPlace collision = barrier.findCollision(circleEquation);
            if (collision == null) {
                continue;
            }

            if (bricksToDelete.isEmpty()) {
                handleCollision(barrier, collision);
            }

            if (barrier instanceof Brick brick) {
                brick.onHit();
                if (brick.isDead()) {
                    bricksToDelete.add(brick);
                }
            }
        }

        bricksToDelete.forEach(barriers::remove);
    }

    private void handleCollision(Barrier barrier, CollisionPlace collision) {
        if (barrier instanceof Platform) {
            double diffXBetweenBallAndCenterPlatform = barrier.position().x() + barrier.size.x() / 2 - position().x();
            flipDirectionByPlatform(diffXBetweenBallAndCenterPlatform);
            motion = motion.rotate(-90 - diffXBetweenBallAndCenterPlatform);

        } else {
            if (collision.needToChangeDirection) {
                motion = motion.flipDirection();
            }
            double angle = collision.needToChangeDirection ? -180 - motion.getMotionAngle() : -motion.getMotionAngle();
            motion = motion.rotate(angle);
        }
    }

    private void flipDirectionByPlatform(double diffXBetweenBallAndCenterPlatform) {
        if (motion.direction == MotionDirection.RIGHT && diffXBetweenBallAndCenterPlatform > 0
                || motion.direction == MotionDirection.LEFT && diffXBetweenBallAndCenterPlatform <= 0) {
            motion = motion.flipDirection();
        }
    }

}
