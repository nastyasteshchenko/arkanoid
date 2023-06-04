package oop.arkanoid.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BaseLinearEquationTest {

    @Test
    void hasIntersection() {
    }

    @Test
    void getY() {
        BaseLinearEquation baseLinearEquation = new BaseLinearEquation(45, 15, new Point(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY));
        assertEquals(24, baseLinearEquation.getY(9));
    }
}