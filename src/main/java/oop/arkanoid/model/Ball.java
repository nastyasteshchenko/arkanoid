//package oop.arkanoid.model;
//
//public class Ball {
//
//    private boolean isVerticalCollisionWithBrick = false;
//    private boolean isHorizontalCollisionWithBrick = false;
//    private final double speed = 1.5;
//    private double angle = Math.random() * 60 - 120;
//    private final double radius;
//    private double centerX;
//    private double centerY;
//
//
//    public boolean isCollisionWithBrickLeftSide(Brick brick) {
//        return Math.abs(centerX + radius - brick.getX()) <= speed / 2
//                && centerY + radius > brick.getY() && centerY - radius < brick.getY() + brick.getHeight();
//    }
//
//    public boolean isCollisionWithBrickRightSide(Brick brick) {
//        return Math.abs(centerX - radius - brick.getX() - brick.getWidth()) <= speed / 2
//                && centerY + radius > brick.getY() && centerY - radius < brick.getY() + brick.getHeight();
//    }
//
//    public void renewCoordinates() {
//        centerX += speed * Math.cos(Math.toRadians(angle));
//        centerY += speed * Math.sin(Math.toRadians(angle));
//        isHorizontalCollisionWithBrick = false;
//        isVerticalCollisionWithBrick = false;
//    }
//
//    public double getRadius() {
//        return radius;
//    }
//
//    public double getCenterX() {
//        return centerX;
//    }
//
//    public double getCenterY() {
//        return centerY;
//    }
//
//    public double getAngle() {
//        return angle;
//    }
//
//    public void setAngle(double angle) {
//        this.angle = angle;
//    }
//
//}

package oop.arkanoid.model;

import java.util.List;

class Ball {

    private enum Collisions {
        HORIZONTAL,
        VERTICAL
    }

    private Collisions collision;
    final double radius;

    final Point position;

    Motion motion;

    double speed;

    Ball(double radius, Point position) {
        this.radius = radius;
        this.position = position;
        motion = new Motion(position, Math.random() * 60 - 120);
    }

    Point nextPosition(List<Barrier> barriers) {
        motion.move(speed);
        // TODO don't need to check all barriers every time, just need it once after changing the trajectory or direction
        for (Barrier barrier : barriers) {
            if (!hasCollision(barrier)) {
                continue;
            }

            motion.angle = countReboundingAngle(barrier);
            motion.move(speed);

            if (barrier instanceof Destroyable d) {
                d.onHit();
            }
        }
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

    public boolean isCollisionWithBottom(Barrier barrier) {
        return position.x() - radius < barrier.position.x() + barrier.size.x() && position.x() + radius > barrier.position.x()
                && Math.abs(position.y() - radius - barrier.position.y() - barrier.size.y()) <= speed / 2;
    }

    public boolean isCollisionWithTop(Barrier barrier) {
        return position.x() - radius < barrier.position.x() + barrier.size.x() && position.x() + radius > barrier.position.x()
                && Math.abs(position.y() + radius - barrier.position.y()) <= speed / 2;
    }

    public boolean isCollisionWithLeftSide(Barrier barrier) {
        return Math.abs(position.x() + radius - barrier.position.x()) <= speed / 2
                && position.y() + radius > barrier.position.y() && position.y() - radius < barrier.position.y() + barrier.size.y();
    }

    public boolean isCollisionWithRightSide(Barrier barrier) {
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
        this.angle= angle;
    }

    void move(double speed) {
        position.setX(position.x() + speed * Math.cos(Math.toRadians(angle)));
        position.setY(position.y() + speed * Math.sin(Math.toRadians(angle)));
    }
}
