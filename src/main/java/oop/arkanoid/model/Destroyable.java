package oop.arkanoid.model;

public interface Destroyable {

    boolean isDead();
    boolean isImmortal();
    Point position();
    void onHit();
    int score();

}