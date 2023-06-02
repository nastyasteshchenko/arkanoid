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

        linearEquations.put(CollisionPlace.BOTTOM, LinearEquation.linearEquation(0, position.y() + size.y(), new Point(position.x() + 1, position.x() + size.x() - 1)));
        linearEquations.put(CollisionPlace.TOP, LinearEquation.linearEquation(0, position.y(), new Point(position.x() + 1, position.x() + size.x() - 1)));
        linearEquations.put(CollisionPlace.LEFT, LinearEquation.xLinearMotionEquation(position.x(), new Point(position.y() + 1, position.y() + size.y() - 1)));
        linearEquations.put(CollisionPlace.RIGHT, LinearEquation.xLinearMotionEquation(position.x() + size.x(), new Point(position.y() + 1, position.y() + size.y() - 1)));
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