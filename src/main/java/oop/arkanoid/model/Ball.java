package oop.arkanoid.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

enum CollisionResult {
    NEED_TO_CHANGE_ANGLE,
    NO_ANGLE_CHANGE
}

class Ball {
    final double radius;
    MotionTrajectory motionTrajectory;

    Map<Barrier, CollisionResult> visibleCollisions = new HashMap<>();

    Ball(Point position, double radius) {
        this.radius = radius;
        motionTrajectory = new MotionTrajectory(new Point(1, -1), position, new Point(0, 100000), new Point(0, 100000));
    }

    Point move(List<Barrier> barriers) {
        if (visibleCollisions.isEmpty()) {
            detectVisibleCollisions(barriers);
        }
        checkCollisions(barriers);
        return motionTrajectory.nextPoint();
    }

    private void detectVisibleCollisions(List<Barrier> barriers) {
        for (Barrier barrier : barriers) {
            CollisionResult collisionResult = barrier.hasVisibleCollisions(motionTrajectory.trajectory);
            if (barrier instanceof Platform) {
                collisionResult = CollisionResult.NEED_TO_CHANGE_ANGLE;
            }
            if (collisionResult != null) {
                visibleCollisions.put(barrier, collisionResult);
            }
        }
    }

    private void checkCollisions(List<Barrier> barriers) {
        AtomicBoolean hasColl = new AtomicBoolean(false);
        visibleCollisions.forEach((barrier, collisionResult) -> {
            if (collisionResult == CollisionResult.NEED_TO_CHANGE_ANGLE) {
                if (isCollisionWithBottom(barrier) || isCollisionWithTop(barrier)) {
                    motionTrajectory.trajectory.dy = -motionTrajectory.trajectory.dy;
                    if (barrier instanceof Destroyable d) {
                        d.onHit();
                        if (!d.isAlive()) {
                            barriers.remove(d);
                        }
                    } else if (barrier instanceof Platform p) {
                        changeAngleByPlatform(p);
                    }
                    motionTrajectory.trajectory.recountB(motionTrajectory.position);
                    hasColl.set(true);
                }
            } else if (isCollisionWithLeftSide(barrier) || isCollisionWithRightSide(barrier)) {
                if (barrier instanceof Destroyable d) {
                    d.onHit();
                    if (!d.isAlive()) {
                        barriers.remove(d);
                    }
                }
                motionTrajectory.trajectory.dx = -motionTrajectory.trajectory.dx;
                motionTrajectory.trajectory.recountB(motionTrajectory.position);
                hasColl.set(true);
            }
        });
        if (hasColl.get()) {
            visibleCollisions.clear();
        }
    }

    private void changeAngleByPlatform(Platform platform) {
        double platformCenterX = platform.position.x() + platform.size.x() / motionTrajectory.speed / 2;
        double halfPlatformXSize = platform.size.x()/2;
        motionTrajectory.changeAngle((motionTrajectory.position.x() - platformCenterX) / halfPlatformXSize);
    }

    private boolean isCollisionWithBottom(Barrier barrier) {
        return motionTrajectory.position.x() - radius < barrier.position.x() + barrier.size.x() && motionTrajectory.position.x() + radius > barrier.position.x()
                && Math.abs(motionTrajectory.position.y() - radius - barrier.position.y() - barrier.size.y()) <= motionTrajectory.speed / 2;
    }

    private boolean isCollisionWithTop(Barrier barrier) {
        return motionTrajectory.position.x() - radius < barrier.position.x() + barrier.size.x() && motionTrajectory.position.x() + radius > barrier.position.x()
                && Math.abs(motionTrajectory.position.y() + radius - barrier.position.y()) <= motionTrajectory.speed / 2;
    }

    private boolean isCollisionWithLeftSide(Barrier barrier) {
        return Math.abs(motionTrajectory.position.x() + radius - barrier.position.x()) <= motionTrajectory.speed / 2
                && motionTrajectory.position.y() + radius > barrier.position.y() && motionTrajectory.position.y() - radius < barrier.position.y() + barrier.size.y();
    }

    private boolean isCollisionWithRightSide(Barrier barrier) {
        return Math.abs(motionTrajectory.position.x() - radius - barrier.position.x() - barrier.size.x()) <= 1
                && motionTrajectory.position.y() + radius > barrier.position.y() && motionTrajectory.position.y() - radius < barrier.position.y() + barrier.size.y();
    }

}

