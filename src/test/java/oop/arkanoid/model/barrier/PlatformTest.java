package oop.arkanoid.model.barrier;

import oop.arkanoid.model.CircleEquation;
import oop.arkanoid.model.GeneratingGameException;
import oop.arkanoid.model.Point;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlatformTest {

    @Test
    void updatePlatformTest() {
        Platform platform = new Platform(new Point(10, 20), new Point(30, 30));
        platform.update(100);
        assertEquals(100, platform.position.x());
    }

    @Test
    void hasCollisionWithBallTest() {
        CircleEquation circleEquation = new CircleEquation(new Point(25, 30), 15);
        Platform platform = new Platform(new Point(10, 44), new Point(30, 30));
        assertDoesNotThrow(() -> platform.checkCollisionWithBall(circleEquation));
    }

    @Test
    void NoCollisionWithBallTest() {
        CircleEquation circleEquation = new CircleEquation(new Point(25, 30), 15);
        Platform platform = new Platform(new Point(10, 46), new Point(30, 30));
        assertThrows(GeneratingGameException.class, () -> platform.checkCollisionWithBall(circleEquation));
    }

}