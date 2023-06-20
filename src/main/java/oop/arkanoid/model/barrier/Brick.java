package oop.arkanoid.model.barrier;

import oop.arkanoid.notifications.NotificationsAboutDestroy;
import oop.arkanoid.model.*;
import oop.arkanoid.model.motion.LinearEquation;

import java.util.EnumMap;

public final class Brick extends Barrier{

    private final Health health;
    private final int score;
    private final EnumMap<CollisionPlace, LinearEquation> linearEquations = new EnumMap<>(CollisionPlace.class);

    public Brick(Point position, Point size, Health health) {
        super(position, size);
        this.health = health;
        score = health.getValue() * 5;

        linearEquations.put(CollisionPlace.BOTTOM, LinearEquation.linearEquation(0, new Point(position.x(), position.y() + size.y())));
        linearEquations.put(CollisionPlace.TOP, LinearEquation.linearEquation(0, position));
        linearEquations.put(CollisionPlace.LEFT, LinearEquation.xLinearMotionEquation(position.x()));
        linearEquations.put(CollisionPlace.RIGHT, LinearEquation.xLinearMotionEquation(position.x() + size.x()));
    }

    public boolean isDead() {
        return health.isDead();
    }

    public boolean isImmortal() {
        return health instanceof Health.Immortal;
    }

    public int score() {
        return score;
    }

    public void onHit() {
        health.decrease();
        if (isDead()) {
            NotificationsAboutDestroy.getInstance().publish(this);
        }
    }

    public int health(){
        return health.getValue();
    }

    @Override
    EnumMap<CollisionPlace, LinearEquation> getLinearEquations() {
        return linearEquations;
    }
}