package oop.arkanoid.model.barrier;

import oop.arkanoid.model.Point;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BrickTest {

    @Test
    void isDeadTest() {
        Brick brick = new Brick(new Point(0,0), new Point(10, 10), new Health(1));
        brick.onHit();
        assertTrue(brick.isDead());
    }

    @Test
    void isNotDeadTest() {
        Brick brick = new Brick(new Point(0,0), new Point(10, 10), new Health(1));
        assertFalse(brick.isDead());
    }

    @Test
    void isImmortal() {
        Brick brick = new Brick(new Point(0,0), new Point(10, 10), Health.createImmortal());
        assertTrue(brick.isImmortal());
    }

    @Test
    void isNotImmortal() {
        Brick brick = new Brick(new Point(0,0), new Point(10, 10), new Health(1));
        assertFalse(brick.isImmortal());
    }

    @Test
    void onHitTest() {
        Brick brick = new Brick(new Point(0,0), new Point(10, 10), new Health(2));
        assertEquals(2, brick.health.getValue());
        brick.onHit();
        assertEquals(1, brick.health.getValue());
    }
}