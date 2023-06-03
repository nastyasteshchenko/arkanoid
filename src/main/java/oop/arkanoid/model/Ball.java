package oop.arkanoid.model;

import java.util.ArrayList;
import java.util.List;

class Ball {
    final double radius;
    Point position;
    LinearMotion motion;

    Ball(double radius, Point startPos, LinearMotion startMotion) {
        this.radius = radius;
        this.position = startPos;
        this.motion = startMotion;
    }

    Point move(double step, List<Barrier> barriers) {

        detectCollisions(barriers);

        motion = motion.changeStepIfNeeded(step);

        this.position = motion.nextPoint();
        return this.position;
    }

    private void detectCollisions(List<Barrier> barriers) {
        CircleEquation circleEquation = new CircleEquation(position, radius);
        List<Brick> collisions = new ArrayList<>();
        boolean hasChangedDirection = false;
        for (Barrier barrier : barriers) {
            CollisionPlace collision = barrier.findCollision(circleEquation);
            if (collision == null) {
                continue;
            }
            if (!hasChangedDirection) {
                if (collision.needToChangeDirection) {
                    motion = motion.flipDirection();
                }

                if (barrier instanceof Platform) {
                    double platformCenterX = barrier.position.x() + barrier.size.x() / 2;
                    motion = motion.flipDirection(position.x() - platformCenterX);
                    motion = motion.rotate(platformCenterX - position.x());
                } else {
                    motion = motion.rotate(collision);
                }
                hasChangedDirection = true;
            }

            if (barrier instanceof Brick brick) {
                brick.onHit();
                if (!brick.isAlive()) {
                    collisions.add(brick);
                }
            }

        }

        if (hasChangedDirection) {
            collisions.forEach(barriers::remove);
        }
    }
}
