package oop.arkanoid.model2;

abstract class Barrier {

    final Point position;
    final Point size;

    Barrier(Point position, Point size) {
        this.position = position;
        this.size = size;
    }
}
