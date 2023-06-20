package oop.arkanoid.notifications;

import oop.arkanoid.model.barrier.Brick;

public interface DestroySubscriber {

    void update(Brick brick);
}
