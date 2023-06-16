package oop.arkanoid.model.barrier;

import oop.arkanoid.model.CircleEquation;
import oop.arkanoid.model.GeneratingGameException;
import oop.arkanoid.model.Point;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WallTest {

    @Test
    void checkWallsTest1() throws GeneratingGameException {
        Point sceneSize = new Point(300, 600);
        Wall leftWall = new Wall(new Point(0, 0), new Point(0, sceneSize.y()), CollisionPlace.LEFT);
        assertDoesNotThrow(() -> leftWall.checkWall(sceneSize));
        Wall rightWall = new Wall(new Point(sceneSize.x(), 0), new Point(0, sceneSize.y()), CollisionPlace.RIGHT);
        assertDoesNotThrow(() -> rightWall.checkWall(sceneSize));
        Wall topWall = new Wall(new Point(0, 0), new Point(sceneSize.x(), 0), CollisionPlace.BOTTOM);
        assertDoesNotThrow(() -> topWall.checkWall(sceneSize));
    }

    @Test
    void checkWallsTest2() throws GeneratingGameException {
        Point sceneSize = new Point(300, 600);
        Wall leftWall = new Wall(new Point(0, 10), new Point(0, sceneSize.y() - 10), CollisionPlace.LEFT);
        assertThrows(GeneratingGameException.class, () -> leftWall.checkWall(sceneSize));
    }

    @Test
    void collisionWithRightWallTest() throws GeneratingGameException {
        CircleEquation circleEquation = new CircleEquation(new Point(100, 50), 15);
        Wall wall = new Wall(new Point(115, 0), new Point(0, 200), CollisionPlace.RIGHT);
        assertEquals(CollisionPlace.RIGHT, wall.findCollision(circleEquation));
    }

    @Test
    void collisionWithTopWallTest() throws GeneratingGameException {
        CircleEquation circleEquation = new CircleEquation(new Point(0, 15), 15);
        Wall wall = new Wall(new Point(0, 0), new Point(200, 0), CollisionPlace.BOTTOM);
        assertEquals(CollisionPlace.BOTTOM, wall.findCollision(circleEquation));
    }

    @Test
    void creatingBottomWallTest() {
        assertThrows(GeneratingGameException.class, () -> new Wall(new Point(0, 0), new Point(200, 0), CollisionPlace.TOP));
    }
}