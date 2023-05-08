package oop.arkanoid.model;

public interface Destroyable {

    boolean isAlive();

    Point position();

    void onHit();
}