package oop.arkanoid.model;

import oop.arkanoid.model.barrier.CollisionPlace;

import java.util.List;

//TODO: объединить в motion????
public interface LinearEquation {

    List<Double> getIntersectionPoints(CircleEquation circleEquation);

    double getY(double x);

    //TODO: передавать в аргументы угол, который считается ранее
    LinearEquation rotate(Point currPoint, CollisionPlace place);

    LinearEquation rotate(Point currPoint, double diffBetweenBallAndCenterPlatform);

    //TODO подумать над тем чтобы считать b внутри функции
    static LinearEquation linearEquation(double angle, double b) {
        return new BaseLinearEquation(angle, b);
    }

    static LinearEquation xLinearMotionEquation(double x) {
        return new XLinearEquation(x);
    }

}
