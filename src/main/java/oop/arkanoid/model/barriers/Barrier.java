package oop.arkanoid.model.barriers;

import javafx.util.Pair;
import oop.arkanoid.model.*;

import java.util.EnumMap;
import java.util.List;

import static oop.arkanoid.model.RangeCheckerUtil.checkRange;

public abstract sealed class Barrier permits Wall, Brick, Platform {

    public final Point position;
    public final Point size;

    Barrier(Point position, Point size) {
        this.position = position;
        this.size = size;
    }

    abstract EnumMap<CollisionPlace, LinearEquation> getLinearEquations();

    public CollisionPlace findCollision(CircleEquation circleEquation) {

        Pair<CollisionPlace, Double> vertical = null;
        Pair<CollisionPlace, Double> horizontal = null;

        var linearEquations = getLinearEquations();
        for (var entry : linearEquations.entrySet()) {
            List<Double> intersectionPoints = entry.getValue().getIntersectionPoints(circleEquation);
            for (Double root : intersectionPoints) {
                if ((entry.getKey() == CollisionPlace.LEFT || entry.getKey() == CollisionPlace.RIGHT) && checkRange(position.y(), position.y() + size.y(), root)) {
                    vertical = new Pair<>(entry.getKey(), root);
                    break;
                }
                if ((entry.getKey() == CollisionPlace.TOP || entry.getKey() == CollisionPlace.BOTTOM) && checkRange(position.x(), position.x() + size.x(), root)) {
                    horizontal = new Pair<>(entry.getKey(), root);
                    break;
                }
            }
        }

        if (vertical == null && horizontal == null) {
            return null;
        }

        if (horizontal == null) {
            return vertical.getKey();
        }
        if (vertical == null) {
            return horizontal.getKey();
        }

        return getDistanceXFromEdge(horizontal) <= getDistanceYFromEdge(vertical) ? vertical.getKey() : horizontal.getKey();
    }

    public void checkIfCollisions(List<Barrier> barriers) throws GeneratingGameException {
        if (barriers.stream().anyMatch(this::hasCollisionWithObject)) {
            throw GeneratingGameException.collisionWithOtherObjects();
        }
    }

    public void checkIfOutOfScene(Point scene) throws GeneratingGameException {
        if (!checkRange(0, scene.x(), position.x()) || !checkRange(0, scene.y(), position.y()) ||
                !checkRange(0, scene.x(), position.x() + size.x()) || !checkRange(0, scene.y(), position.y() + size.y())) {
            throw GeneratingGameException.outOfScene();
        }
    }

    private boolean hasCollisionWithObject(Barrier barrier) {
        return (checkRange(barrier.position.x(), barrier.position.x() + barrier.size.x(), position.x()) ||
                checkRange(barrier.position.x(), barrier.position.x() + barrier.size.x(), position.x() + size.x())) &&
                (checkRange(barrier.position.y(), barrier.position.y() + barrier.size.y(), position.y()) ||
                        checkRange(barrier.position.y(), barrier.position.y() + barrier.size.y(), position.y() + size.y()));
    }

    private double getDistanceYFromEdge(Pair<CollisionPlace, Double> vertical) {
        return vertical.getKey() == CollisionPlace.TOP ? Math.abs(vertical.getValue() - position.y()) : Math.abs(vertical.getValue() - position.y() - size.y());
    }

    private double getDistanceXFromEdge(Pair<CollisionPlace, Double> horizontal) {
        return horizontal.getKey() == CollisionPlace.LEFT ? Math.abs(horizontal.getValue() - position.x()) : Math.abs(horizontal.getValue() - position.x() - size.x());
    }

}