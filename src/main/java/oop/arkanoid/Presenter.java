package oop.arkanoid;

import com.google.gson.JsonObject;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.util.Duration;
import oop.arkanoid.model.Ball;
import oop.arkanoid.model.GameLevel;
import oop.arkanoid.model.GeneratingGameException;
import oop.arkanoid.model.barrier.Barrier;
import oop.arkanoid.model.barrier.Brick;
import oop.arkanoid.model.barrier.Health;
import oop.arkanoid.model.barrier.Platform;
import oop.arkanoid.view.LevelView;

import java.io.*;
import java.util.*;

import static oop.arkanoid.Arkanoid.changeScene;
import static oop.arkanoid.Arkanoid.createAlert;

//TODO add .gitignore
public class Presenter {
    private static ScoresManager scoresManager;
    private static LevelsManager levelsManager;
    private static ScenesManager scenesManager;
    private static Timeline animation;
    private static LevelView gameView;
    private static GameLevel model;
    private static boolean gameIsStarted = false;
    private static boolean isPause = false;

    public static void startPlayingGame() {
        gameIsStarted = true;
    }

    public static void setPause() {
        isPause = !isPause;
        if (isPause) {
            animation.pause();
        } else {
            animation.play();
        }
    }

    public static void movePlatform(double x) {
        if (gameIsStarted) {
            gameView.drawPlatform(model.updatePlatformPosition(x));
        }
    }

    static void checkGeneratingAllLevels() throws GeneratingGameException {
        levelsManager.checkGeneratingAllLevels();
    }

    static void loadResourcesBeforeStartApp() throws IOException {
        levelsManager = new LevelsManager();
        scoresManager = new ScoresManager();
        scenesManager = new ScenesManager(scoresManager);
    }

    @FXML
    protected void backToMenu() {
        try {
            scoresManager = new ScoresManager();
            scenesManager.changeRecordsScene(scoresManager);
        } catch (Exception e) {
            createAlert(e);
        }
        changeScene(scenesManager.getScene("main"));
    }

    @FXML
    protected void restartGame() {
        startLevel();
    }

    @FXML
    protected void endGame() {
        System.exit(0);
    }

    @FXML
    protected void startGame() {
        Notifications.getInstance().subscribe(Notifications.EventType.DESTROY, destroyable -> {
            gameView.deleteBrick(destroyable.position());
            gameView.drawScore(model.getScore());
        });

        startLevel();
    }

    @FXML
    protected void backToMainScene() {
        changeScene(scenesManager.getScene("main"));
    }

    @FXML
    protected void watchAboutGame() {
        changeScene(scenesManager.getScene("about"));
    }

    @FXML
    protected void watchRecords() {
        changeScene(scenesManager.getScene("records"));
    }

    private void gameLose() {
        prepareForGameOver(scenesManager.getScene("game_over"));
    }

    private void gameWin() {
        prepareForGameOver(scenesManager.getScene("game_win"));
        levelsManager.increaseLevel();
    }

    private void prepareForGameOver(Scene gameWinScene) {
        animation.stop();
        setRecord();
        gameIsStarted = false;
        changeScene(gameWinScene);
    }

    private void setRecord() {
        scoresManager.writeScore(levelsManager.getCurrentLevel(), model.getScore());
        scoresManager.storeRecords();
    }

    private void startLevel() {

        try {
            model = levelsManager.initLevel();
            if (model == null) {
                changeScene(scenesManager.getScene("game_passed"));
            }
        } catch (GeneratingGameException e) {
            createAlert(e);
        }

        setGameView(levelsManager.getCurrentLevelJsonObject());
        changeScene(gameView.getGameScene());
        startAnimation();

    }

    private void startAnimation() {
        animation = new Timeline(new KeyFrame(Duration.millis(2.5), ae -> {
            if (gameIsStarted) {
                gameView.drawBall(model.nextBallPosition());
                switch (model.gameState()) {
                    case WIN -> gameWin();
                    case LOSE -> gameLose();
                }
            }
        }));
        animation.setCycleCount(Animation.INDEFINITE);
        animation.play();
    }


    private static void setGameView(JsonObject paramsForLevel) {
        LevelView.Builder builder = new LevelView.Builder(paramsForLevel);

        List<Barrier> barriers = model.getBarriers();
        for (Barrier barrier : barriers) {
            if (barrier instanceof Platform platform) {
                builder.platform(platform.position(), platform.size);
                continue;
            }
            if (barrier instanceof Brick brick) {
                if (brick.health instanceof Health.Immortal) {
                    builder.addImmortalBrick(brick.position(), brick.size);
                    continue;
                }
                if (brick.health.getValue() == 1) {
                    builder.addStandardBrick(brick.position(), brick.size);
                    continue;
                }
                if (brick.health.getValue() == 2) {
                    builder.addDoubleHitBrick(brick.position(), brick.size);
                }
            }
        }

        Ball ball = model.getBall();
        builder.ball(ball.getPosition(), ball.radius).gameScene(model.getSceneSize());

        builder.highScore(scoresManager.getScoreForLevel(levelsManager.getCurrentLevel()));

        gameView = builder.build();
    }

}