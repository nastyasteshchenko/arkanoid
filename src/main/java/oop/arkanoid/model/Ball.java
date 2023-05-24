package oop.arkanoid.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

enum CollisionResult {
    HAS_COLLISION,
    NO_COLLISION
}

class Ball {
    final double radius;
    MotionTrajectory motionTrajectory;

    List<Barrier> visibleCollisions = new ArrayList<>();

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
            if (barrier instanceof Platform) {
                visibleCollisions.add(barrier);
            }
            if (barrier.hasVisibleCollisions(motionTrajectory.trajectory, radius)) {
                visibleCollisions.add(barrier);
            }
        }
    }

    private void checkCollisions(List<Barrier> barriers) {
        AtomicBoolean hasVerticalCollision = new AtomicBoolean(false);
        AtomicBoolean hasHorizontalCollision = new AtomicBoolean(false);
        visibleCollisions.forEach(barrier -> {
            if (isCollisionWithBottom(barrier) || isCollisionWithTop(barrier)) {
                if (!hasHorizontalCollision.get()) {
                    motionTrajectory.trajectory.dy = -motionTrajectory.trajectory.dy;
                    if (barrier instanceof Platform p) {
                        changeAngleByPlatform(p);
                    }
                    motionTrajectory.trajectory.recountB(motionTrajectory.position);
                }
                if (barrier instanceof Destroyable d) {
                    d.onHit();
                    if (!d.isAlive()) {
                        barriers.remove(d);
                    }
                }
                hasHorizontalCollision.set(true);
            } else if (isCollisionWithLeftSide(barrier) || isCollisionWithRightSide(barrier)) {
                if (barrier instanceof Destroyable d) {
                    d.onHit();
                    if (!d.isAlive()) {
                        barriers.remove(d);
                    }
                }
                if (!hasHorizontalCollision.get()) {
                    motionTrajectory.trajectory.dx = -motionTrajectory.trajectory.dx;
                    motionTrajectory.trajectory.recountB(motionTrajectory.position);
                }
                hasHorizontalCollision.set(true);
            }
        });
        if (hasVerticalCollision.get() || hasHorizontalCollision.get()) {
            visibleCollisions.clear();
        }
    }

    private void changeAngleByPlatform(Platform platform) {
        double halfPlatformXSize = platform.size.x() / 2;
        double platformCenterX = platform.position.x() + halfPlatformXSize;
        motionTrajectory.changeAngle((motionTrajectory.position.x() - platformCenterX) / halfPlatformXSize);
    }

    private boolean isCollisionWithBottom(Barrier barrier) {
        return motionTrajectory.position.x() - radius <= barrier.position.x() + barrier.size.x() && motionTrajectory.position.x() + radius >= barrier.position.x()
                && Math.abs(motionTrajectory.position.y() - radius - barrier.position.y() - barrier.size.y()) <= Math.abs(motionTrajectory.trajectory.dy) / 2;
    }

    private boolean isCollisionWithTop(Barrier barrier) {
        return motionTrajectory.position.x() - radius <= barrier.position.x() + barrier.size.x() && motionTrajectory.position.x() + radius >= barrier.position.x()
                && Math.abs(motionTrajectory.position.y() + radius - barrier.position.y()) <= Math.abs(motionTrajectory.trajectory.dy) / 2;
    }

    private boolean isCollisionWithLeftSide(Barrier barrier) {
        return Math.abs(motionTrajectory.position.x() + radius - barrier.position.x()) <= Math.abs(motionTrajectory.trajectory.dx) / 2
                && motionTrajectory.position.y() + radius >= barrier.position.y() && motionTrajectory.position.y() - radius <= barrier.position.y() + barrier.size.y();
    }

    private boolean isCollisionWithRightSide(Barrier barrier) {
        return Math.abs(motionTrajectory.position.x() - radius - barrier.position.x() - barrier.size.x()) <= Math.abs(motionTrajectory.trajectory.dx) / 2
                && motionTrajectory.position.y() + radius >= barrier.position.y() && motionTrajectory.position.y() - radius <= barrier.position.y() + barrier.size.y();
    }

}

