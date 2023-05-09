package oop.arkanoid.model;

import java.util.ArrayList;
import java.util.List;

class Ball {

    private enum Collisions {
        HORIZONTAL,
        VERTICAL
    }

    private Collisions collision;
    final double radius;
    final Point position;
    double speed = 1.5;
    private final Motion motion;

    Ball(Point position, double radius) {
        this.radius = radius;
        this.position = position;
        motion = new Motion(position, Math.random() * 60 - 120);
    }

    Point nextPosition(List<Barrier> barriers) {
        ArrayList<Brick> collisionBricks = new ArrayList<>();
        motion.move(speed);
        boolean isChangedAngle = false;
        // TODO don't need to check all barriers every time, just need it once after changing the trajectory or direction
        for (Barrier barrier : barriers) {
            if (!hasCollision(barrier)) {
                continue;
            }

            if (!isChangedAngle) {
                motion.angle = countReboundingAngle(barrier);
                isChangedAngle = true;
            }

            if (barrier instanceof Destroyable d) {
                d.onHit();
                if (!d.isAlive()) {
                    collisionBricks.add((Brick) barrier);
                }
            }
        }

        collisionBricks.forEach(barriers::remove);
        return motion.position;
    }

    private boolean hasCollision(Barrier barrier) {
        if (isCollisionWithBottom(barrier) || isCollisionWithTop(barrier)) {
            collision = Collisions.HORIZONTAL;
            return true;
        }
        if (isCollisionWithLeftSide(barrier) || isCollisionWithRightSide(barrier)) {
            collision = Collisions.VERTICAL;
            return true;
        }
        return false;
    }

    private double countReboundingAngle(Barrier barrier) {

        if (barrier instanceof Platform) {
            return -90 - (barrier.position.x() + barrier.size.x() / 2 - position.x());
        }

        if (collision == Collisions.VERTICAL) {
            return -180 - motion.angle;
        }

        if (collision == Collisions.HORIZONTAL) {
            return -motion.angle;
        }
        return motion.angle;
    }

    private boolean isCollisionWithBottom(Barrier barrier) {
        return position.x() - radius < barrier.position.x() + barrier.size.x() && position.x() + radius > barrier.position.x()
                && Math.abs(position.y() - radius - barrier.position.y() - barrier.size.y()) <= speed / 2;
    }

    private boolean isCollisionWithTop(Barrier barrier) {
        return position.x() - radius < barrier.position.x() + barrier.size.x() && position.x() + radius > barrier.position.x()
                && Math.abs(position.y() + radius - barrier.position.y()) <= speed / 2;
    }

    private boolean isCollisionWithLeftSide(Barrier barrier) {
        return Math.abs(position.x() + radius - barrier.position.x()) <= speed / 2
                && position.y() + radius > barrier.position.y() && position.y() - radius < barrier.position.y() + barrier.size.y();
    }

    private boolean isCollisionWithRightSide(Barrier barrier) {
        return Math.abs(position.x() - radius - barrier.position.x() - barrier.size.x()) <= speed / 2
                && position.y() + radius > barrier.position.y() && position.y() - radius < barrier.position.y() + barrier.size.y();
    }
}

// TODO think about naming
class Motion {
    Point position;
    double angle;

    Motion(Point startPosition, double angle) {
        this.position = startPosition;
        this.angle = angle;
    }

    void move(double speed) {
        position.setX(position.x() + speed * Math.cos(Math.toRadians(angle)));
        position.setY(position.y() + speed * Math.sin(Math.toRadians(angle)));

    }
}
