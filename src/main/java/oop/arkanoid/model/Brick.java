package oop.arkanoid.model;


import oop.arkanoid.Notifications;

class Brick extends Barrier implements Destroyable {

    private final int score;
    final Health health;

    Brick(Point position, Point size, Health health) {
        super(position, size);
        this.health = health;
        score = health.getValue() * 2;
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
            Notifications.getInstance().publish(Notifications.EventType.DESTROY, this);
        }
    }
}