package oop.arkanoid.model;

import java.util.ArrayList;
import java.util.List;

class Ball {
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
        motion.move(speed);
        if (motion.predictedCollisions.isEmpty()) {
            predictCollisions(barriers);
        } else {
            detectAndHandleCollisions().stream().filter(brick -> !brick.isAlive()).forEach(barriers::remove);
        }
        return motion.position;
    }

    private void predictCollisions(List<Barrier> barriers) {
        for (Barrier barrier : barriers) {
            if (barrier instanceof Platform p) {
                motion.predictedCollisions.add(p);
            } else {
                if (willBeCollision(barrier)) {
                    motion.predictedCollisions.add(barrier);
                }
            }
        }
    }

    private ArrayList<Destroyable> detectAndHandleCollisions() {
        ArrayList<Destroyable> collisionBricks = new ArrayList<>();
        boolean isChangedAngle = false;
        for (Barrier barrier : motion.predictedCollisions) {
            if (!hasCollision(barrier)) {
                continue;
            }
            if (!isChangedAngle) {
                motion.countReboundingAngle(barrier);
                isChangedAngle = true;
            }
            if (barrier instanceof Destroyable d) {
                d.onHit();
                collisionBricks.add(d);
            }
        }
        if (isChangedAngle) {
            motion.predictedCollisions.clear();
        }
        return collisionBricks;
    }

    private boolean willBeCollision(Barrier barrier) {
        return willBeCollisionWithBottom(barrier) || willBeCollisionWithTop(barrier) || willBeCollisionWithRightSide(barrier) || willBeCollisionWithLeftSide(barrier);
    }

    private boolean willBeCollisionWithBottom(Barrier barrier) {
        return motion.countX(barrier.position.y() + barrier.size.y() + radius) - radius < barrier.position.x() + barrier.size.x()
                && motion.countX(barrier.position.y() + barrier.size.y() + radius) + radius > barrier.position.x();
    }

    private boolean willBeCollisionWithTop(Barrier barrier) {
        return motion.countX(barrier.position.y() - radius) - radius < barrier.position.x() + barrier.size.x()
                && motion.countX(barrier.position.y() - radius) + radius > barrier.position.x();
    }

    private boolean willBeCollisionWithLeftSide(Barrier barrier) {
        return motion.countY(barrier.position.x() - radius) + radius > barrier.position.y()
                && motion.countY(barrier.position.x() - radius) - radius < barrier.position.y() + barrier.size.y();
    }

    private boolean willBeCollisionWithRightSide(Barrier barrier) {
        return motion.countY(barrier.position.x() + radius + barrier.size.x()) + barrier.size.x() + radius > barrier.position.y()
                && motion.countY(barrier.position.x() + radius + barrier.size.x()) + barrier.size.x() - radius < barrier.position.y() + barrier.size.y();
    }

    private boolean hasCollision(Barrier barrier) {
        if (isCollisionWithBottom(barrier) || isCollisionWithTop(barrier)) {
            motion.setHorizontalCollision();
            return true;
        }
        if (isCollisionWithLeftSide(barrier) || isCollisionWithRightSide(barrier)) {
            motion.setVerticalCollision();
            return true;
        }
        return false;
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

class Motion {

    private enum Collisions {
        HORIZONTAL,
        VERTICAL
    }

    private Collisions collision;
    final ArrayList<Barrier> predictedCollisions = new ArrayList<>();
    Point position;
    double angle;

    Motion(Point startPosition, double angle) {
        this.position = startPosition;
        this.angle = angle;
    }

    void setVerticalCollision() {
        collision = Collisions.VERTICAL;
    }

    void setHorizontalCollision() {
        collision = Collisions.HORIZONTAL;
    }

    void move(double speed) {
        position.setX(position.x() + speed * Math.cos(Math.toRadians(angle)));
        position.setY(position.y() + speed * Math.sin(Math.toRadians(angle)));

    }

    void countReboundingAngle(Barrier barrier) {
        if (barrier instanceof Platform) {
            angle = -90 - (barrier.position.x() + barrier.size.x() / 2 - position.x());
            return;
        }

        if (collision == Collisions.VERTICAL) {
            angle = -180 - angle;
            return;
        }

        if (collision == Collisions.HORIZONTAL) {
            angle = -angle;
        }
    }

    double countX(double y) {
        return (y - position.y()) / Math.tan(Math.toRadians(angle)) + position.x();
    }

    double countY(double x) {
        return (x - position.x()) * Math.tan(Math.toRadians(angle)) + position.y();
    }
}
