package oop.arkanoid.model;

import oop.arkanoid.model.motion.XLinearEquation;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class XLinearEquationTest {

    @Test
    void hasIntersectionTest() {
        XLinearEquation xLinearEquation = new XLinearEquation(10);
        CircleEquation circleEquation = new CircleEquation(new Point(10, 40), 14);
        List<Double> points = xLinearEquation.getIntersectionPoints(circleEquation);
        assertEquals(26, points.get(0));
        assertEquals(54, points.get(1));
    }

    @Test
    void noIntersectionTest() {
        XLinearEquation xLinearEquation = new XLinearEquation(10);
        CircleEquation circleEquation = new CircleEquation(new Point(40, 40), 14);
        assertTrue(xLinearEquation.getIntersectionPoints(circleEquation).isEmpty());
    }

    @Test
    void unsupportedOperationTest() {
        XLinearEquation xLinearEquation = new XLinearEquation(10);
        assertThrows(UnsupportedOperationException.class, () -> xLinearEquation.rotate(80, new Point(0, 0)));
    }

    @Test
    void getYTest() {
        XLinearEquation xLinearEquation = new XLinearEquation(10);
        assertEquals(Double.NaN, xLinearEquation.getY(10));
    }
}