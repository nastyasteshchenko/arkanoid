package oop.arkanoid.model;

import oop.arkanoid.model.barriers.CollisionPlace;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LinearMotionTest {

    @Test
    void nextPoint() {

    }

    @Test
    void changeStepIfNeeded() {
        LinearMotion linearMotion = new LinearMotion(new BaseLinearEquation(45, 15), MotionDirection.RIGHT, 1, new Point(0, 0));
        linearMotion = linearMotion.changeStepIfNeeded(1.5);
        assertEquals(1.5, linearMotion.step);
    }

    @Test
    void flipDirection() {
        LinearMotion linearMotion = new LinearMotion(new BaseLinearEquation(45, 15), MotionDirection.RIGHT, 1, new Point(0, 0));
        linearMotion = linearMotion.flipDirection();
        assertEquals(MotionDirection.LEFT, linearMotion.direction);
    }

    @Test
    void flipDirectionByPlatform1() {
        LinearMotion linearMotion = new LinearMotion(new BaseLinearEquation(45, 15), MotionDirection.RIGHT, 1, new Point(0, 0));
        linearMotion = linearMotion.flipDirection(-10);
        assertEquals(MotionDirection.RIGHT, linearMotion.direction);
    }

    @Test
    void flipDirectionByPlatform2() {
        LinearMotion linearMotion = new LinearMotion(new BaseLinearEquation(45, 15), MotionDirection.RIGHT, 1, new Point(0, 0));
        linearMotion = linearMotion.flipDirection(10);
        assertEquals(MotionDirection.LEFT, linearMotion.direction);
    }

    @Test
    void rotateByVerticalCollision() {
        LinearMotion linearMotion = new LinearMotion(new BaseLinearEquation(45, 15), MotionDirection.RIGHT, 1, new Point(0, 0));
        linearMotion = linearMotion.rotate(CollisionPlace.RIGHT);
        assertEquals(Math.tan(Math.toRadians(-225)), linearMotion.linearEquation.k);

    }

    @Test
    void rotateByHorizontalCollision() {
        LinearMotion linearMotion = new LinearMotion(new BaseLinearEquation(45, 15), MotionDirection.RIGHT, 1, new Point(0, 0));
        linearMotion = linearMotion.rotate(CollisionPlace.TOP);
        assertEquals(Math.tan(Math.toRadians(-45)), linearMotion.linearEquation.k);
    }

    @Test
    void rotateByPlatform() {
        LinearMotion linearMotion = new LinearMotion(new BaseLinearEquation(45, 15), MotionDirection.RIGHT, 1, new Point(0, 0));
        linearMotion = linearMotion.rotate(10);
        assertEquals(Math.tan(Math.toRadians(-100)), linearMotion.linearEquation.k);
    }

}