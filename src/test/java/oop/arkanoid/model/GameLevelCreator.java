package oop.arkanoid.model;

import oop.arkanoid.model.barrier.CollisionPlace;

public class GameLevelCreator {

    static GameLevel createLevel() throws GeneratingGameException {
        Point sceneSize = new Point(600, 900);
        GameLevel.Builder builder = new GameLevel.Builder(sceneSize);
        builder.ball(new Point(289, 740), 15)
                .platform(new Point(236, 756), new Point(124, 22))
                .addBrick(new Point(477, 81), new Point(114, 30), 1)
                .addBrick(new Point(126, 81), new Point(114, 30), 1)
                .addBrick(new Point(243, 81), new Point(114, 30), 1)
                .addBrick(new Point(360, 81), new Point(114, 30), 1)
                .addWall(new Point(0, 0), new Point(0, sceneSize.y()), CollisionPlace.LEFT)
                .addWall(new Point(sceneSize.x(), 0), new Point(0, sceneSize.y()), CollisionPlace.RIGHT)
                .addWall(new Point(0, 0), new Point(sceneSize.x(), 0), CollisionPlace.BOTTOM);

        return builder.build();
    }


}
