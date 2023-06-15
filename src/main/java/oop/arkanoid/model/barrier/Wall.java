package oop.arkanoid.model.barrier;

import oop.arkanoid.model.*;
import oop.arkanoid.model.motion.LinearEquation;

import java.util.EnumMap;
import java.util.Objects;

public final class Wall extends Barrier {

    private final LinearEquation linearEquation;
    private final CollisionPlace collisionPlace;

    public Wall(Point position, Point size, CollisionPlace collisionPlace) throws GeneratingGameException {
        super(position, size);
        this.collisionPlace = collisionPlace;
        linearEquation = switch (collisionPlace) {
            case LEFT, RIGHT ->
                    LinearEquation.xLinearMotionEquation(position.x());
            case BOTTOM ->
                    LinearEquation.linearEquation(0, 0);
            case TOP -> throw GeneratingGameException.addingBottomWall();
        };
    }

    public void checkWall(Point scene, Point wallPosition) throws GeneratingGameException {
        switch (collisionPlace) {
            case LEFT, BOTTOM -> {
                if (wallPosition.x() != 0 || wallPosition.y() != 0) {
                    throw GeneratingGameException.wrongWallPosition();
                }
            }
            case RIGHT -> {
                if (!Objects.equals(wallPosition.x(), scene.x()) || wallPosition.y() != 0) {
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