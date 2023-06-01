package oop.arkanoid.model;

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
        // TODO detect one barrier if there are several collisions
        for (Barrier barrier : barriers) {
            CollisionPlace collision = barrier.findCollision(circleEquation);
            if (collision == null) {
                continue;
            }
            if (collision.needToChangeDirection) {
                motion = motion.flipDirection();
            }

            if (barrier instanceof Platform) {
                double platformCenterX = barrier.position.x() + barrier.size.x() / 2;
                if (position.x() <= platformCenterX) {
                    if (motion.direction == MotionDirection.RIGHT) {
                        motion = motion.flipDirection();
                    }
                } else {
                    if (motion.direction == MotionDirection.LEFT) {
                        motion = motion.flipDirection();
                    }
                }
                motion = motion.rotate(platformCenterX, position.x());
            } else {
                motion = motion.rotate();
            }
            break;
            // TODO fire hit
        }
    }
}

