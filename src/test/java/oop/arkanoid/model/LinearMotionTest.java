package oop.arkanoid.model;

import oop.arkanoid.model.motion.BaseLinearEquation;
import oop.arkanoid.model.motion.LinearEquation;
import oop.arkanoid.model.motion.LinearMotion;
import oop.arkanoid.model.motion.MotionDirection;
import org.junit.jupiter.api.Test;

import static oop.arkanoid.model.TestUtils.roundToThousandths;
import static org.junit.jupiter.api.Assertions.*;

class LinearMotionTest {

    @Test
    void nextPointTest() {
        Point nextPoint = createLinearMotion().nextPoint();
        assertEquals(1, roundToThousandths(nextPoint.x()));
        assertEquals(1, roundToThousandths(nextPoint.y()));
    }

    @Test
    void flipDirectionTest() {
        LinearMotion linearMotion = createLinearMotion().flipDirection();
        assertEquals(MotionDirection.LEFT, linearMotion.direction);
    }

    @Test
    void rotateByVerticalCollisionTest() {
        LinearMotion linearMotion = createLinearMotion().rotate(67);
        assertEquals(67, linearMotion.getMotionAngle());
    }

    private static LinearMotion createLinearMotion() {
        return new LinearMotion((BaseLinearEquation) LinearEquation.linearEquation(45, new Point(0, 0)), MotionDirection.RIGHT, Math.sqrt(2), new Point(0, 0));
    }
}
