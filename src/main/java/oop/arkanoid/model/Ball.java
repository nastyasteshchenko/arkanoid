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
                motion = motion.flipHorizontalDirection();
            } else {
                motion = motion.flipVerticalDirection();
            }
            motion = motion.rotate();
            break;
            // TODO fire hit
        }
    }
}

