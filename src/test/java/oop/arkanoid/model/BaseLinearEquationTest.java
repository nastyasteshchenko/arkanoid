package oop.arkanoid.model;

import oop.arkanoid.model.barriers.CollisionPlace;
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
    void getYTest() {
        BaseLinearEquation baseLinearEquation = new BaseLinearEquation(45, 15);
        assertEquals(24, baseLinearEquation.getY(9));
    }

    @Test
    void rotateByVerticalCollision(){
        BaseLinearEquation baseLinearEquation = new BaseLinearEquation(45, 15);
        baseLinearEquation = (BaseLinearEquation) baseLinearEquation.rotate(new Point(15,15), CollisionPlace.RIGHT);
        assertEquals(Math.tan(Math.toRadians(-225)), baseLinearEquation.k);
    }

    @Test
    void rotateByHorizontalCollision(){
        BaseLinearEquation baseLinearEquation = new BaseLinearEquation(45, 15);
        baseLinearEquation = (BaseLinearEquation) baseLinearEquation.rotate(new Point(15,15), CollisionPlace.TOP);
        assertEquals(Math.tan(Math.toRadians(-45)), baseLinearEquation.k);
    }

    @Test
    void rotateByPlatform(){
        BaseLinearEquation baseLinearEquation = new BaseLinearEquation(45, 15);
        baseLinearEquation = (BaseLinearEquation) baseLinearEquation.rotate(new Point(15,15), 10);
        assertEquals(Math.tan(Math.toRadians(-100)), baseLinearEquation.k);
    }
}