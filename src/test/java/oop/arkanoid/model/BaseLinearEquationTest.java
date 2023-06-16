package oop.arkanoid.model;

import oop.arkanoid.model.motion.BaseLinearEquation;
import oop.arkanoid.model.motion.LinearEquation;
import org.junit.jupiter.api.Test;

import java.util.List;

import static oop.arkanoid.model.TestUtils.roundToThousandths;
import static org.junit.jupiter.api.Assertions.*;

class BaseLinearEquationTest {

    @Test
    void hasIntersection() {
        LinearEquation baseLinearEquation = LinearEquation.linearEquation(0, new Point(7, 15));
        CircleEquation circleEquation = new CircleEquation(new Point(40, 15), 14);
        List<Double> points = baseLinearEquation.getIntersectionPoints(circleEquation);
        assertEquals(26, points.get(0));
        assertEquals(54, points.get(1));
    }

    @Test
    void noIntersection() {
        LinearEquation baseLinearEquation = LinearEquation.linearEquation(0, new Point(7, 15));
        CircleEquation circleEquation = new CircleEquation(new Point(40, 40), 14);
        assertTrue(baseLinearEquation.getIntersectionPoints(circleEquation).isEmpty());
    }

    @Test
    void getYTest() {
        LinearEquation baseLinearEquation = LinearEquation.linearEquation(45, new Point(7, 15));
        assertEquals(17, baseLinearEquation.getY(9));
    }

    @Test
    void rotateTest() {
        BaseLinearEquation baseLinearEquation = (BaseLinearEquation) LinearEquation.linearEquation(165, new Point(7, 15));
        baseLinearEquation = (BaseLinearEquation) baseLinearEquation.rotate(45, new Point(7, 15));
        assertEquals(1, roundToThousandths(baseLinearEquation.k()));
        assertEquals(45, baseLinearEquation.angle());
    }

}