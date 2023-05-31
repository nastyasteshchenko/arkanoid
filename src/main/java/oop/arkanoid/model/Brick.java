package oop.arkanoid.model;

import oop.arkanoid.Notifications;

import java.util.EnumMap;

class Brick extends Barrier implements Destroyable {
    private final int score;
    final Health health;

    private final EnumMap<CollisionPlace, LinearEquation> linearEquations = new EnumMap<>(CollisionPlace.class);

    Brick(Point position, Point size, Health health) {
        super(position, size);
        this.health = health;
        score = health.getValue() * 5;

        linearEquations.put(CollisionPlace.TOP, LinearEquation.linearEquation(0, position.y()));
        linearEquations.put(CollisionPlace.LEFT, LinearEquation.xlinearMotionEquation(position.x()));
        linearEquations.put(CollisionPlace.RIGHT, LinearEquation.xlinearMotionEquation(position.x() + size.x()));
        linearEquations.put(CollisionPlace.BOTTOM, LinearEquation.linearEquation(0, position.y() + size.y()));
    }

//    @Override
//
//            Notifications.getInstance().publish(Notifications.EventType.DESTROY, this);

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