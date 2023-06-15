package oop.arkanoid.model;

public interface Destroyable {

    //TODO rename isDead
    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    boolean isAlive();
    Point position();
    void onHit();
    //TODO delete
    int score();

}