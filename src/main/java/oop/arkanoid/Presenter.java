package oop.arkanoid;

import com.google.gson.JsonObject;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.util.Duration;
import oop.arkanoid.model.Ball;
import oop.arkanoid.model.GameLevel;
import oop.arkanoid.model.GeneratingGameException;
import oop.arkanoid.model.barriers.Barrier;
import oop.arkanoid.model.barriers.Brick;
import oop.arkanoid.model.barriers.Health;
import oop.arkanoid.model.barriers.Platform;
import oop.arkanoid.view.LevelView;

import java.io.*;
import java.util.*;

import static oop.arkanoid.Arkanoid.changeScene;
import static oop.arkanoid.Arkanoid.createAlert;

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

    static void loadResourcesBeforeStartApp() {
        try {
            scoresManager = new ScoresManager();
            levelsManager = new LevelsManager();
        } catch (Exception e) {
            createAlert(e);
        }

        try {
            scenesManager = new ScenesManager(scoresManager);
        } catch (IOException e) {
            createAlert(e);
        }
    }

    @FXML
    protected void backToMenu() {
        loadResourcesBeforeStartApp();
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
            model.increaseScore(destroyable.score());
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
        JsonObject paramsForLevel = levelsManager.getCurrentLevelJsonObject();

        if (paramsForLevel == null) {
            changeScene(scenesManager.getScene("game_passed"));
        }

        try {
            model = GameLevel.initLevel(paramsForLevel);
        } catch (GeneratingGameException e) {
            createAlert(e);
        }

        setGameView(paramsForLevel);

        changeScene(gameView.getGameScene());

        startAnimation();

    }

    private void startAnimation() {
        animation = new Timeline(new KeyFrame(Duration.millis(2.5), ae -> {
            if (gameIsStarted) {
                gameView.drawBall(model.nextBallPosition());
                switch (model.gameState()) {
                    case GAME_WIN -> gameWin();
                    case GAME_LOSE -> gameLose();
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
                builder.platform(platform.position, platform.size);
                continue;
            }
            if (barrier instanceof Brick brick) {
                if (brick.health instanceof Health.Immortal) {
                    builder.addImmortalBrick(brick.position, brick.size);
                    continue;
                }
                if (brick.health.getValue() == 1) {
                    builder.addStandardBrick(brick.position, brick.size);
                    continue;
                }
                if (brick.health.getValue() == 2) {
                    builder.addDoubleHitBrick(brick.position, brick.size);
                }
            }
        }

        Ball ball = model.getBall();
        builder.ball(ball.getPosition(), ball.radius).gameScene(model.getSceneSize());

        builder.highScore(scoresManager.getScoreForLevel(levelsManager.getCurrentLevel()));

        gameView = builder.build();
    }

}