package oop.arkanoid.model;

import oop.arkanoid.model.barrier.CollisionPlace;
import oop.arkanoid.model.motion.BaseLinearEquation;
import oop.arkanoid.model.motion.LinearMotion;
import oop.arkanoid.model.motion.MotionDirection;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LinearMotionTest {

//    @Test
//    void nextPoint() {
//        Point nextPoint = createLinearMotion().nextPoint();
//        assertEquals(1, Math.round(nextPoint.x() * 100000) / 100000);
//        assertEquals(1, nextPoint.y());
//    }
//
//    @Test
//    void changeStepIfNeeded() {
//        LinearMotion linearMotion = createLinearMotion().changeStepIfNeeded(1.5);
//        assertEquals(1.5, linearMotion.step);
//    }
//
//    @Test
//    void flipDirection() {
//        LinearMotion linearMotion = createLinearMotion().flipDirection();
//        assertEquals(MotionDirection.LEFT, linearMotion.direction);
//    }
//
//    @Test
//    void flipDirectionByPlatform1() {
//        LinearMotion linearMotion = createLinearMotion().flipDirection(-10);
//        assertEquals(MotionDirection.RIGHT, linearMotion.direction);
//    }
//
//    @Test
//    void flipDirectionByPlatform2() {
//        LinearMotion linearMotion = createLinearMotion().flipDirection(10);
//        assertEquals(MotionDirection.LEFT, linearMotion.direction);
//    }

//    @Test
//    void rotateByVerticalCollision() {
//        LinearMotion linearMotion = createLinearMotion().rotate(CollisionPlace.RIGHT);
//        assertEquals(Math.tan(Math.toRadians(-225)), linearMotion.linearEquation.k);
//
//    }
//
//    @Test
//    void rotateByHorizontalCollision() {
//        LinearMotion linearMotion = createLinearMotion().rotate(CollisionPlace.TOP);
//        assertEquals(Math.tan(Math.toRadians(-45)), linearMotion.linearEquation.k);
//    }
//
//    @Test
//    void rotateByPlatform() {
//        LinearMotion linearMotion = createLinearMotion().rotate(10);
//        assertEquals(Math.tan(Math.toRadians(-100)), linearMotion.linearEquation.k);
//    }
//
//    private static LinearMotion createLinearMotion() {
//        return new LinearMotion(new BaseLinearEquation(45, BaseLinearEquation.countB(45, new Point(0, 0))), MotionDirection.RIGHT, Math.sqrt(2), new Point(0, 0));
//    }
}