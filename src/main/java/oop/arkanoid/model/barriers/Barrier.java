package oop.arkanoid.model.barriers;

import javafx.util.Pair;
import oop.arkanoid.model.*;

import java.util.EnumMap;
import java.util.List;

import static oop.arkanoid.model.RangeChecker.checkRange;


public abstract sealed class Barrier permits Wall, Brick, Platform {

    public final Point position;
    public final Point size;

    Barrier(Point position, Point size) {
        this.position = position;
        this.size = size;
    }

    public CollisionPlace findCollision(CircleEquation circleEquation) {
        CollisionPlace collisionPlace = null;

        Pair<CollisionPlace, LinearEquation> vertical = null;
        Pair<CollisionPlace, LinearEquation> horizontal = null;

        var linearEquations = getLinearEquations();
        for (var entry : linearEquations.entrySet()) {
            if (entry.getValue().hasIntersection(circleEquation)) {
                if (entry.getKey() == CollisionPlace.LEFT || CollisionPlace.RIGHT == entry.getKey()) {
                    vertical = new Pair<>(entry.getKey(), entry.getValue());
                } else {
                    horizontal = new Pair<>(entry.getKey(), entry.getValue());
                }
                collisionPlace = entry.getKey();
            }
        }

        if (collisionPlace == null) {
            return null;
        }

        if (vertical == null || horizontal == null) {
            return collisionPlace;
        }

        double distanceX = horizontal.getValue().getDistanceBallCrossingLine(circleEquation, vertical.getKey());
        double distanceY = vertical.getValue().getDistanceBallCrossingLine(circleEquation, horizontal.getKey());

        return distanceX <= distanceY ? vertical.getKey() : horizontal.getKey();
    }

    abstract EnumMap<CollisionPlace, LinearEquation> getLinearEquations();

    public void checkIfCollisions(List<Barrier> barriers) throws GeneratingGameException {
        if (barriers.stream().anyMatch(this::hasCollisionWithObject)) {
            throw GeneratingGameException.collisionWithOtherObjects();
        }
    }

    public void checkIfOutOfScene(Point scene) throws GeneratingGameException {
        if (!checkRange(0, scene.x(), position.x()) || !checkRange(0, scene.y(), position.y()) || !checkRange(0, scene.x(), position.x() + size.x()) || !checkRange(0, scene.y(), position.y() + size.y())) {
            throw GeneratingGameException.outOfScene();
        }
    }

    private boolean hasCollisionWithObject(Barrier barrier) {
        return (checkRange(barrier.position.x(), barrier.position.x() + barrier.size.x(), position.x()) || checkRange(barrier.position.x(), barrier.position.x() + barrier.size.x(), position.x() + size.x()))
                && (checkRange(barrier.position.y(), barrier.position.y() + barrier.size.y(), position.y()) || checkRange(barrier.position.y(), barrier.position.y() + barrier.size.y(), position.y() + size.y()));
    }
}