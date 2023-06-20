package oop.arkanoid.notifications;

import oop.arkanoid.model.barrier.Brick;

public interface Subscriber {

    void update(Brick brick);
}
