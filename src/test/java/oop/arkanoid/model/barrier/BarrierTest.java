package oop.arkanoid.model.barrier;

import oop.arkanoid.model.CircleEquation;
import oop.arkanoid.model.GeneratingGameException;
import oop.arkanoid.model.Point;
import org.junit.jupiter.api.Test;

import java.util.List;

import static oop.arkanoid.model.TestUtils.createBarriers;
import static org.junit.jupiter.api.Assertions.*;

class BarrierTest {

    @Test
    void findLeftCollisionTest() {
        Brick brick = new Brick(new Point(123, 81), new Point(114, 30), new Health(1));
        CircleEquation circleEquation = new CircleEquation(new Point(108, 100), 15);
        assertEquals(CollisionPlace.LEFT, brick.findCollision(circleEquation));
    }

    @Test
    void findRightCollisionTest() {
        Brick brick = new Brick(new Point(123, 81), new Point(114, 30), new Health(1));
        CircleEquation circleEquation = new CircleEquation(new Point(250, 100), 15);
        assertEquals(CollisionPlace.RIGHT, brick.findCollision(circleEquation));
    }

    @Test
    void findTopCollisionTest() {
        Brick brick = new Brick(new Point(123, 81), new Point(114, 30), new Health(1));
        CircleEquation circleEquation = new CircleEquation(new Point(150, 78), 15);
        assertEquals(CollisionPlace.TOP, brick.findCollision(circleEquation));
    }

    @Test
    void findBottomCollisionTest() {
        Brick brick = new Brick(new Point(123, 81), new Point(114, 30), new Health(1));
        CircleEquation circleEquation = new CircleEquation(new Point(150, 115), 15);
        assertEquals(CollisionPlace.BOTTOM, brick.findCollision(circleEquation));
    }

    @Test
    void findCollisionWhenTwoLinesCrossedTest() {
        Brick brick = new Brick(new Point(123, 81), new Point(114, 30), new Health(1));
        CircleEquation circleEquation = new CircleEquation(new Point(120, 82), 15);
        assertEquals(CollisionPlace.TOP, brick.findCollision(circleEquation));
    }

    @Test
    void hasCollisionsWithOtherBarrierTest() {
        List<Barrier> barriers = createBarriers();
        Brick brick = new Brick(new Point(100, 81), new Point(114, 30), new Health(1));
        assertThrows(GeneratingGameException.class, () -> brick.checkIfCollisionsWithOtherBarrier(barriers));
    }

    @Test
    void noCollisionsWithOtherBarrierTest() {
        List<Barrier> barriers = createBarriers();
        Brick brick = new Brick(new Point(9, 81), new Point(114, 30), new Health(1));
        assertDoesNotThrow(() -> brick.checkIfCollisionsWithOtherBarrier(barriers));
    }

    @Test
    void isOutOfScene() {
        Point sceneSize = new Point(300, 600);
        Brick brick = new Brick(new Point(-100, 10), new Point(100, 100), new Health(1));
        assertThrows(GeneratingGameException.class, () -> brick.checkIfOutOfScene(sceneSize));
    }

    @Test
    void isNotOutOfScene() {
        Point sceneSize = new Point(300, 600);
        Brick brick = new Brick(new Point(100, 10), new Point(100, 100), new Health(1));
        assertDoesNotThrow(() -> brick.checkIfOutOfScene(sceneSize));
    }



}