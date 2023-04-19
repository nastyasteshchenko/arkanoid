package oop.arkanoid.model2;

import java.util.List;

class Ball {
    final double radius;

    Motion motion;

    Ball(double radius) {
        this.radius = radius;
    }

    Point nextPosition(int speed, List<Barrier> barriers) {
        motion.move(speed);
        // TODO don't need to check all barriers every time, just need it once after changing the trajectory or direction
        for (Barrier barrier : barriers) {
            if (!hasCollision(barrier)) {
                continue;
            }
            // TODO change motion here, because we have a collision
            // TODO the direction and angle could change as well

            if (barrier instanceof Destroyable d) {
                d.onHit();
            }
        }
        return motion.position;
    }

    private boolean hasCollision(Barrier barrier) {
        // TODO check if we have a collision

        return true;
    }
}

// TODO think about naming
class Motion {
    Point position;
    Trajectory trajectory;
    Direction direction;


    Motion(Point startPosition) {
        this.position = startPosition;
    }

    void move(int speed) {

    }
}

// TODO think about naming
class Trajectory {
    double angle;
    double b;
}

enum Direction {
    UP,
    DOWN
}