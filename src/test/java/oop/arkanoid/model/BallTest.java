package oop.arkanoid.model;

import oop.arkanoid.model.barrier.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static oop.arkanoid.model.TestUtils.roundToThousandths;
import static org.junit.jupiter.api.Assertions.*;

class BallTest {

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
        Ball ball = new Ball(15, new Point(150, 165));
        List<Barrier> barriers = createBarriers();
        Point nextPosition = ball.move(barriers);
        assertSame(ball.position(), nextPosition);
        assertEquals(150.75, roundToThousandths(nextPosition.x()));
        assertEquals(166.299, roundToThousandths(nextPosition.y()));
    }

    @Test
    void moveBallWithRightCollisionTest() throws GeneratingGameException {
        Ball ball = new Ball(15, new Point(285, 25));
        List<Barrier> barriers = createBarriers();
        barriers.add(new Wall(new Point(300, 0), new Point(0, 600), CollisionPlace.RIGHT));
        Point nextPosition = ball.move(barriers);
        assertSame(ball.position(), nextPosition);
        assertEquals(284.25, roundToThousandths(nextPosition.x()));
        assertEquals(23.701, roundToThousandths(nextPosition.y()));
    }

    @Test
    void moveBallWithTopCollisionTest() {
        Ball ball = new Ball(15, new Point(165, 85));
        List<Barrier> barriers = createBarriers();
        Point nextPosition = ball.move(barriers);
        assertSame(ball.position(), nextPosition);
        assertEquals(165.75, roundToThousandths(nextPosition.x()));
        assertEquals(86.299, roundToThousandths(nextPosition.y()));
    }

    @Test
    void moveBallWithLeftCollisionTest() {
        Ball ball = new Ball(15, new Point(85, 125));
        List<Barrier> barriers = createBarriers();
        Point nextPosition = ball.move(barriers);
        assertSame(ball.position(), nextPosition);
        assertEquals(84.25, roundToThousandths(nextPosition.x()));
        assertEquals(123.701, roundToThousandths(nextPosition.y()));
    }

    private static List<Barrier> createBarriers() {
        List<Barrier> barriers = new ArrayList<>();
        barriers.add(new Brick(new Point(100, 100), new Point(200, 50), new Health(1)));
        barriers.add(new Brick(new Point(200, 200), new Point(200, 50), new Health(1)));
        return barriers;
    }
}