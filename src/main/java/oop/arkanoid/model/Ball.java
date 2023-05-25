package oop.arkanoid.model;

import java.util.ArrayList;
import java.util.List;

class Ball {
    final double radius;
    final MotionTrajectory motionTrajectory;
    private final List<Barrier> visibleCollisions = new ArrayList<>();

    Ball(Point position, double radius) {
        this.radius = radius;
        motionTrajectory = new MotionTrajectory(new Point(1, 1), position);
    }

    Point move(List<Barrier> barriers) {
        if (visibleCollisions.isEmpty()) {
            detectVisibleCollisions(barriers);
        }
        checkCollisions(barriers);
        return motionTrajectory.nextPoint();
    }

    boolean isCollisionWithTop(Barrier barrier) {
        return motionTrajectory.position.x() - radius <= barrier.position.x() + barrier.size.x() && motionTrajectory.position.x() + radius >= barrier.position.x()
                && Math.abs(motionTrajectory.position.y() + radius - barrier.position.y()) <= Math.abs(motionTrajectory.trajectory.dy) / 2;
    }

    private void detectVisibleCollisions(List<Barrier> barriers) {
        barriers.forEach(barrier -> {
            if (barrier instanceof Platform) {
                visibleCollisions.add(barrier);
            } else {
                if (barrier.hasVisibleCollisions(motionTrajectory.trajectory, radius)) {
                    visibleCollisions.add(barrier);
                }
            }
        });
    }

    private void checkCollisions(List<Barrier> barriers) {
        boolean hasCollision = false;
        for (Barrier barrier: visibleCollisions){
            if (isCollisionWithBottom(barrier) || isCollisionWithTop(barrier)) {
                if (!hasCollision) {
                    motionTrajectory.trajectory.dy = -motionTrajectory.trajectory.dy;
                    if (barrier instanceof Platform p) {
                        changeAngleAccordingToPlatform(p);
                    }
                    motionTrajectory.trajectory.recountB(motionTrajectory.position);
                    hasCollision=true;
                }
                handleCollision(barriers, barrier);
            } else if (isCollisionWithLeftSide(barrier) || isCollisionWithRightSide(barrier)) {
                if (!hasCollision) {
                    motionTrajectory.trajectory.dx = -motionTrajectory.trajectory.dx;
                    motionTrajectory.trajectory.recountB(motionTrajectory.position);
                    hasCollision= true;
                }
                handleCollision(barriers, barrier);
            }
        }
        if (hasCollision) {
            visibleCollisions.clear();
        }
    }

    private static void handleCollision(List<Barrier> barriers, Barrier barrier) {
        if (barrier instanceof Destroyable d) {
            d.onHit();
            if (!d.isAlive()) {
                barriers.remove(d);
            }
        }
    }

    private void changeAngleAccordingToPlatform(Platform platform) {
        double halfPlatformXSize = platform.size.x() / 2;
        double platformCenterX = platform.position.x() + halfPlatformXSize;
        motionTrajectory.changeAngle((motionTrajectory.position.x() - platformCenterX) / halfPlatformXSize + 0.1);
    }

    private boolean isCollisionWithBottom(Barrier barrier) {
        return motionTrajectory.position.x() - radius <= barrier.position.x() + barrier.size.x() && motionTrajectory.position.x() + radius >= barrier.position.x()
                && Math.abs(motionTrajectory.position.y() - radius - barrier.position.y() - barrier.size.y()) <= Math.abs(motionTrajectory.trajectory.dy) / 2;
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

