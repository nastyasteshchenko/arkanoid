package oop.arkanoid.model.barrier;

import oop.arkanoid.model.*;
import oop.arkanoid.model.motion.LinearEquation;

import java.util.EnumMap;

public final class Wall extends Barrier {

    private final LinearEquation linearEquation;
    private final CollisionPlace collisionPlace;

    public Wall(Point position, Point size, CollisionPlace collisionPlace) throws GeneratingGameException {
        super(position, size);
        this.collisionPlace = collisionPlace;
        linearEquation = switch (collisionPlace) {
            case LEFT, RIGHT -> LinearEquation.xLinearMotionEquation(position.x());
            case BOTTOM -> LinearEquation.linearEquation(0, new Point(0, 0));
            case TOP -> throw GeneratingGameException.addingBottomWall();
        };
    }

    public void checkWall(Point scene) throws GeneratingGameException {
        switch (collisionPlace) {
            case BOTTOM -> {
                if (position.x() != 0 || position.y() != 0 || size.x() != scene.x()) {
                    throw GeneratingGameException.wrongWallPosition();
                }
            }
            case RIGHT -> {
                if (position.x() != scene.x() || position.y() != 0 || size.y() != scene.y()) {
                    throw GeneratingGameException.wrongWallPosition();
                }
            }
            case LEFT -> {
                if (position.x() != 0 || position.y() != 0 || size.y() != scene.y()) {
                    throw GeneratingGameException.wrongWallPosition();
                }
            }
        }
    }

    @Override
    public CollisionPlace findCollision(CircleEquation circleEquation) {
        var linearEquations = getLinearEquations();
        for (var entry : linearEquations.entrySet()) {
            if (!entry.getValue().getIntersectionPoints(circleEquation).isEmpty()) {
                return entry.getKey();
            }
        }
        return null;
    }

    @Override
    EnumMap<CollisionPlace, LinearEquation> getLinearEquations() {
        var result = new EnumMap<CollisionPlace, LinearEquation>(CollisionPlace.class);
        result.put(collisionPlace, linearEquation);
        return result;
    }
}