//package oop.arkanoid.model;
//
//public sealed class Brick permits StandardBrick, IndestructibleBrick, DoubleHitBrick {
//    private final double x;
//    private final double y;
//    private final double width;
//    private final double height;
//    protected int points;
//    protected int countOfHits;
//
//    public Brick(double x, double y, double width, double height) {
//        this.x = x;
//        this.y = y;
//        this.width = width;
//        this.height = height;
//    }
//
//    public double getX() {
//        return x;
//    }
//
//    public double getY() {
//        return y;
//    }
//
//    public double getWidth() {
//        return width;
//    }
//
//    public double getHeight() {
//        return height;
//    }
//
//    public int getPoints() {
//        return points;
//    }
//
//    public int getCountOfHits() {
//        return countOfHits;
//    }
//    public void decreaseCountOfHits() {
//        --countOfHits;
//    }
//}
package oop.arkanoid.model;


import oop.arkanoid.Notifications;

class Brick extends Barrier implements Destroyable {

    final Health health;

    Brick(Point position, Point size, Health health) {
        super(position, size);
        this.health = health;
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