package oop.arkanoid.model.motion;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MotionDirectionTest {

    @Test
    void flipDirectionTest() {
        MotionDirection motionDirection = MotionDirection.RIGHT;
        motionDirection = motionDirection.flip();
        assertEquals(MotionDirection.LEFT, motionDirection);
    }

}