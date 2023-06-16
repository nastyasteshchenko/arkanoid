package oop.arkanoid.notifications;

import oop.arkanoid.model.Destroyable;

public interface Subscriber {

    void update(Destroyable destroyable);
}
