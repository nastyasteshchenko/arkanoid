package oop.arkanoid.model;

public interface Destroyable {

    boolean isDead();
    Point position();
    void onHit();
    //TODO delete
    int score();

}