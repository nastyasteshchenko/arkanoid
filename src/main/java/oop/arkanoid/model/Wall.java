package oop.arkanoid.model;

import java.util.EnumMap;
import java.util.Objects;

public class Wall extends Barrier {

    private final LinearEquation linearEquation;
    private final CollisionPlace collisionPlace;

    Wall(Point position, Point size, CollisionPlace collisionPlace) throws GeneratingGameException {
        super(position, size);
        this.collisionPlace = collisionPlace;
        linearEquation = switch (collisionPlace) {
            case LEFT, RIGHT ->
                    LinearEquation.xLinearMotionEquation(position.x(), new Point(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY));
            case BOTTOM ->
                    LinearEquation.linearEquation(0, 0, new Point(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY));
            case TOP -> throw GeneratingGameException.addingBottomWall();
        };
    }

    void checkWall(Point scene, Point wallPosition) throws GeneratingGameException {
        switch (collisionPlace) {
            case LEFT, BOTTOM -> {
                if (wallPosition.x() != 0 || wallPosition.y() != 0) {
                    throw GeneratingGameException.wrongWallPosition();
                }
            }
            case RIGHT -> {
                if (!Objects.equals(wallPosition.x(), scene.x()) || wallPosition.y() != 0) {
                    System.out.println(wallPosition.x() + " " + wallPosition.y());
                    throw GeneratingGameException.wrongWallPosition();
                }
            }
        }
    }

    @Override
    EnumMap<CollisionPlace, LinearEquation> getLinearEquations() {
        var result = new EnumMap<CollisionPlace, LinearEquation>(CollisionPlace.class);
        result.put(collisionPlace, linearEquation);
        return result;
    }
}