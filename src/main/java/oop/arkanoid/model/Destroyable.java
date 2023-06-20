package oop.arkanoid.model;

//TODO подумать над тем что можно убрать
public interface Destroyable {

    //TODO посмотреть можно ли удалить
    boolean isDead();
    boolean isImmortal();
    Point position();
    void onHit();
    int score();

}