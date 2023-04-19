package oop.arkanoid.model2;


import oop.arkanoid.Notifications;

class Brick extends Barrier implements Destroyable {

    final Health health;

    Brick(Point position, Point size, Health health) {
        super(position, size);
        this.health = health;
    }

    boolean isAlive() {
        return health.isAlive();
    }

    @Override
    public Point position() {
        return null;
    }

    @Override
    public void onHit() {
        assert health.isAlive();
        health.decrease();
        if (!isAlive()) {
            Notifications.getInstance().publish(Notifications.EventType.DESTROY, this);
        }
    }
}
