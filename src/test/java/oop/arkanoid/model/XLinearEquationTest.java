package oop.arkanoid.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class XLinearEquationTest {

    @Test
    void hasIntersection() {
        XLinearEquation xLinearEquation = new XLinearEquation(10);
        CircleEquation circleEquation = new CircleEquation(new Point(1, 40), 14);
        assertFalse(xLinearEquation.getIntersectionPoints(circleEquation).isEmpty());
    }

    @Test
    void noIntersection() {
        XLinearEquation xLinearEquation = new XLinearEquation(10);
        CircleEquation circleEquation = new CircleEquation(new Point(40, 40), 14);
        assertTrue(xLinearEquation.getIntersectionPoints(circleEquation).isEmpty());
    }

}