package oop.arkanoid.model;

import oop.arkanoid.model.barrier.*;
import oop.arkanoid.notifications.EventType;
import oop.arkanoid.notifications.Notifications;
import org.junit.jupiter.api.Test;

import java.util.List;

import static oop.arkanoid.model.TestUtils.createBarriers;
import static oop.arkanoid.model.TestUtils.roundToThousandths;
import static org.junit.jupiter.api.Assertions.*;

class BallTest {

    @Test
    void deletingStandardBricksTest() {
        Ball ball = new Ball(15, new Point(14, 114));
        List<Barrier> barriers = createBarriers();
        Brick brick = new Brick(new Point(9, 81), new Point(114, 30), new Health(1));
        barriers.add(brick);
        ball.move(barriers);
        assertFalse(barriers.contains(brick));
    }

    @Test
    void deletingDoubleHitBricksTest() {
        Ball ball = new Ball(15, new Point(14, 114));
        List<Barrier> barriers = createBarriers();
        Brick brick = new Brick(new Point(9, 81), new Point(114, 30), new Health(2));
        barriers.add(brick);
        ball.move(barriers);
        assertTrue(barriers.contains(brick));
    }

    @Test
    void deletingImmortalBricksTest() {
        Ball ball = new Ball(15, new Point(14, 114));
        List<Barrier> barriers = createBarriers();
        Brick brick = new Brick(new Point(9, 81), new Point(114, 30), Health.createImmortal());
        barriers.add(brick);
        ball.move(barriers);
        assertTrue(barriers.contains(brick));
    }

    @Test
    void moveBallWithoutCollisionsTest() {
        Ball ball = new Ball(15, new Point(10, 10));
        List<Barrier> barriers = createBarriers();
        Point nextPosition = ball.move(barriers);
        assertSame(ball.position(), nextPosition);
        assertEquals(10.75, roundToThousandths(nextPosition.x()));
        assertEquals(8.701, roundToThousandths(nextPosition.y()));
    }


    @Test
    void moveBallWithBottomCollisionTest() {
        Notifications.getInstance().subscribe(EventType.DESTROY, this, b -> {});
        Ball ball = new Ball(15, new Point(150, 115));
        List<Barrier> barriers = createBarriers();
        Point nextPosition = ball.move(barriers);
        assertSame(ball.position(), nextPosition);
        assertEquals(150.75, roundToThousandths(nextPosition.x()));
        assertEquals(116.299, roundToThousandths(nextPosition.y()));
        Notifications.getInstance().unsubscribe(EventType.DESTROY, this);
    }

    @Test
    void moveBallWithRightCollisionTest() throws GeneratingGameException {
        Notifications.getInstance().subscribe(EventType.DESTROY, this, b -> {});
        Ball ball = new Ball(15, new Point(285, 25));
        List<Barrier> barriers = createBarriers();
        barriers.add(new Wall(new Point(300, 0), new Point(0, 600), CollisionPlace.RIGHT));
        Point nextPosition = ball.move(barriers);
        assertSame(ball.position(), nextPosition);
        assertEquals(284.25, roundToThousandths(nextPosition.x()));
        assertEquals(23.701, roundToThousandths(nextPosition.y()));
        Notifications.getInstance().unsubscribe(EventType.DESTROY, this);
    }

    @Test
    void moveBallWithTopCollisionTest() {
        Notifications.getInstance().subscribe(EventType.DESTROY, this, b -> {});
        Ball ball = new Ball(15, new Point(150, 75));
        List<Barrier> barriers = createBarriers();
        Point nextPosition = ball.move(barriers);
        assertSame(ball.position(), nextPosition);
        assertEquals(150.75, roundToThousandths(nextPosition.x()));
        assertEquals(76.299, roundToThousandths(nextPosition.y()));
        Notifications.getInstance().unsubscribe(EventType.DESTROY, this);
    }

    @Test
    void moveBallWithLeftCollisionTest() {
        Notifications.getInstance().subscribe(EventType.DESTROY, this, b -> {});
        Ball ball = new Ball(15, new Point(113, 90));
        List<Barrier> barriers = createBarriers();
        Point nextPosition = ball.move(barriers);
        assertSame(ball.position(), nextPosition);
        assertEquals(112.25, roundToThousandths(nextPosition.x()));
        assertEquals(88.701, roundToThousandths(nextPosition.y()));
        Notifications.getInstance().unsubscribe(EventType.DESTROY, this);
    }

}