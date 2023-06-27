package oop.arkanoid.model.barrier;

import javafx.util.Pair;
import oop.arkanoid.model.*;
import oop.arkanoid.model.motion.LinearEquation;

import java.util.EnumMap;
import java.util.List;

import static oop.arkanoid.model.ModelUtils.isInRange;

public abstract sealed class Barrier permits Wall, Brick, Platform {

    public final Point size;

    protected Point position;

    Barrier(Point position, Point size) {
        this.position = position;
        this.size = size;
    }

    abstract EnumMap<CollisionPlace, LinearEquation> getLinearEquations();

    public Point position() {
        return position;
    }

    public CollisionPlace findCollision(CircleEquation circleEquation) {

        Pair<CollisionPlace, Double> vertical = null;
        Pair<CollisionPlace, Double> horizontal = null;

        var linearEquations = getLinearEquations();
        for (var entry : linearEquations.entrySet()) {
            List<Double> intersectionPoints = entry.getValue().getIntersectionPoints(circleEquation);
            for (Double root : intersectionPoints) {
                if (entry.getKey().needToChangeDirection && isInRange(position.y(), position.y() + size.y(), root)) {
                    vertical = new Pair<>(entry.getKey(), root);
                    break;
                }
                if (!entry.getKey().needToChangeDirection && isInRange(position.x(), position.x() + size.x(), root)) {
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

        return getDistanceXFromEdge(horizontal) < getDistanceYFromEdge(vertical) ? vertical.getKey() : horizontal.getKey();
    }

    public void checkIfCollisionsWithOtherBarrier(List<Barrier> barriers) throws GeneratingGameException {
        if (barriers.stream().anyMatch(this::hasCollisionWithBarrier)) {
            throw GeneratingGameException.collisionWithOtherObjects();
        }
    }

    public void checkIfOutOfScene(Point scene) throws GeneratingGameException {
        if (!isInRange(0, scene.x(), position.x()) || !isInRange(0, scene.y(), position.y()) ||
                !isInRange(0, scene.x(), position.x() + size.x()) || !isInRange(0, scene.y(), position.y() + size.y())) {
            throw GeneratingGameException.outOfScene();
        }
    }

    private boolean hasCollisionWithBarrier(Barrier barrier) {
        return (isInRange(barrier.position.x(), barrier.position.x() + barrier.size.x(), position.x()) ||
                isInRange(barrier.position.x(), barrier.position.x() + barrier.size.x(), position.x() + size.x())) &&
                (isInRange(barrier.position.y(), barrier.position.y() + barrier.size.y(), position.y()) ||
                        isInRange(barrier.position.y(), barrier.position.y() + barrier.size.y(), position.y() + size.y()));
    }

    private double getDistanceYFromEdge(Pair<CollisionPlace, Double> vertical) {
        return vertical.getKey() == CollisionPlace.TOP ? Math.abs(vertical.getValue() - position.y()) : Math.abs(vertical.getValue() - position.y() - size.y());
    }

    private double getDistanceXFromEdge(Pair<CollisionPlace, Double> horizontal) {
        return horizontal.getKey() == CollisionPlace.LEFT ? Math.abs(horizontal.getValue() - position.x()) : Math.abs(horizontal.getValue() - position.x() - size.x());
    }

}