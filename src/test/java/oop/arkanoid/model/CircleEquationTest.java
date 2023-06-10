package oop.arkanoid.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CircleEquationTest {

    @Test
    void getY() {
        CircleEquation circleEquation = new CircleEquation( new Point(10, 10), 15);
        assertEquals(1.0, circleEquation.getY(22).get(0));
        assertEquals(19.0, circleEquation.getY(22).get(1));
    }
}