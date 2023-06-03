package oop.arkanoid.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class XLinearEquationTest {

    @Test
    void hasIntersection() {
        CircleEquation circleEquation = new CircleEquation(new Point(123, 130), 15);
        XLinearEquation xLinearEquation = new XLinearEquation(110, new Point(100, 160));
        assertTrue(xLinearEquation.hasIntersection(circleEquation));
    }

    @Test
    void noIntersection() {
        CircleEquation circleEquation = new CircleEquation(new Point(123, 130), 15);
        XLinearEquation xLinearEquation = new XLinearEquation(50, new Point(100, 160));
        assertFalse(xLinearEquation.hasIntersection(circleEquation));
    }

}