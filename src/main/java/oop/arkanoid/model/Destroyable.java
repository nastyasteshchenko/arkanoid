package oop.arkanoid.model;

public interface Destroyable {

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    boolean isAlive();
    Point position();
    void onHit();
    int score();

}