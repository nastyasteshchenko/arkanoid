package oop.arkanoid.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BaseLinearEquationTest {

    @Test
    void hasIntersection() {
    }

    @Test
    void getY() {
        BaseLinearEquation baseLinearEquation = new BaseLinearEquation(45, 15);
        assertEquals(24, baseLinearEquation.getY(9));
    }
}