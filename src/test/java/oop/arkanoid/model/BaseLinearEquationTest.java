package oop.arkanoid.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BaseLinearEquationTest {

    @Test
    void hasIntersection() {
        BaseLinearEquation baseLinearEquation = new BaseLinearEquation(0, 15);
        CircleEquation circleEquation = new CircleEquation(new Point(40, 10), 14);
        assertFalse(baseLinearEquation.getIntersectionPoints(circleEquation).isEmpty());
    }

    @Test
    void noIntersection() {
        BaseLinearEquation baseLinearEquation = new BaseLinearEquation(0, 15);
        CircleEquation circleEquation = new CircleEquation(new Point(40, 40), 14);
        assertTrue(baseLinearEquation.getIntersectionPoints(circleEquation).isEmpty());
    }

    @Test
    void getY() {
        BaseLinearEquation baseLinearEquation = new BaseLinearEquation(45, 15);
        assertEquals(24, baseLinearEquation.getY(9));
    }
}