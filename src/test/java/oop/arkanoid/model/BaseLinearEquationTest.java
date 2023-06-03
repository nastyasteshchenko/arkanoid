package oop.arkanoid.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BaseLinearEquationTest {

    @Test
    void hasIntersection() {
        CircleEquation circleEquation = new CircleEquation(new Point(123, 130), 15);
        BaseLinearEquation baseLinearEquation = new BaseLinearEquation(0, 145, new Point(100, 160));
        assertTrue(baseLinearEquation.hasIntersection(circleEquation));
    }

    @Test
    void noIntersection() {
        CircleEquation circleEquation = new CircleEquation(new Point(123, 130), 15);
        BaseLinearEquation baseLinearEquation = new BaseLinearEquation(0, 100, new Point(100, 160));
        assertFalse(baseLinearEquation.hasIntersection(circleEquation));
    }

    @Test
    void getY() {
        BaseLinearEquation baseLinearEquation = new BaseLinearEquation(45, 15, new Point(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY));
        assertEquals(24, baseLinearEquation.getY(9));
    }
}