package oop.arkanoid.model.barriers;

import oop.arkanoid.Notifications;
import oop.arkanoid.model.*;

import java.util.EnumMap;

public final class Brick extends Barrier implements Destroyable {
    private final int score;
    public final Health health;

    private final EnumMap<CollisionPlace, LinearEquation> linearEquations = new EnumMap<>(CollisionPlace.class);

    public Brick(Point position, Point size, Health health) {
        super(position, size);
        this.health = health;
        score = health.getValue() * 5;

        linearEquations.put(CollisionPlace.BOTTOM, LinearEquation.linearEquation(0, position.y() + size.y()));
        linearEquations.put(CollisionPlace.TOP, LinearEquation.linearEquation(0, position.y()));
        linearEquations.put(CollisionPlace.LEFT, LinearEquation.xLinearMotionEquation(position.x()));
        linearEquations.put(CollisionPlace.RIGHT, LinearEquation.xLinearMotionEquation(position.x() + size.x()));
    }

    @Override
    public boolean isAlive() {
        return health.isAlive();
    }

    @Override
    public Point position() {
        return position;
    }

    @Override
    public void onHit() {
        health.decrease();
        if (!isAlive()) {
            GameLevel.increaseScore(score);
            Notifications.getInstance().publish(Notifications.EventType.DESTROY, this);
        }
    }

    @Override
    EnumMap<CollisionPlace, LinearEquation> getLinearEquations() {
        return linearEquations;
    }
}