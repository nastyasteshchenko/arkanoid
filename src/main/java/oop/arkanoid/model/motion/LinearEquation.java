package oop.arkanoid.model.motion;

import oop.arkanoid.model.CircleEquation;
import oop.arkanoid.model.Point;
import oop.arkanoid.model.barrier.CollisionPlace;

import java.util.List;

import static oop.arkanoid.model.ModelUtils.tan;

//TODO: объединить в motion????
public interface LinearEquation {

    List<Double> getIntersectionPoints(CircleEquation circleEquation);

    double getY(double x);

    //TODO: передавать в аргументы угол, который считается ранее
    LinearEquation rotate(Point currPoint, CollisionPlace place);

    LinearEquation rotate(Point currPoint, double diffBetweenBallAndCenterPlatform);

    static LinearEquation linearEquation(double angle, Point position) {
        double b = position.y() - position.x() * tan(angle);
        return new BaseLinearEquation(angle, b);
    }

    static LinearEquation xLinearMotionEquation(double x) {
        return new XLinearEquation(x);
    }

}
