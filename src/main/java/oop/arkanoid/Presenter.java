package oop.arkanoid;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Scene;
import javafx.util.Duration;
import oop.arkanoid.model.GameLevel;
import oop.arkanoid.model.GeneratingGameException;
import oop.arkanoid.model.barrier.Brick;
import oop.arkanoid.notifications.*;
import oop.arkanoid.view.LevelView;

import java.io.*;

import static oop.arkanoid.AlertCreationUtil.alert;

public class Presenter {

    private final LevelsManager levelsManager = new LevelsManager();
    private final ScoresManager scoresManager = new ScoresManager();
    private final ScenesManager scenesManager = new ScenesManager();
    private LevelInitiator levelsInitiator;
    private Timeline animation;
    private LevelView gameView;
    private GameLevel model;
    private boolean gameIsStarted = false;
    private boolean isPause = false;
    private int currentLevel;

    public void setGameIsStarted() {
        gameIsStarted = true;
    }

    public void setPause() {
        isPause = !isPause;
        if (isPause) {
            animation.pause();
        } else {
            animation.play();
        }
    }

    public void movePlatform(double x) {
        if (gameIsStarted) {
            gameView.drawPlatform(model.updatePlatformPosition(x));
        }
    }

    void checkGeneratingAllLevels() throws GeneratingGameException {
        levelsManager.checkGeneratingAllLevels();
    }

    void loadResourcesBeforeStartApp() throws IOException {
        levelsManager.scanForLevels();
        scoresManager.scanForScores();
        scenesManager.scanForScenes(scoresManager);

        Notifications.getInstance().subscribe(EventType.START_GAME, this, v -> startGame());

        Notifications.getInstance().subscribe(EventType.EXIT, this, v -> endGame());

        Notifications.getInstance().subscribe(EventType.RECORDS, this, v -> watchRecords());

        Notifications.getInstance().subscribe(EventType.BACK, this, v -> backToMainScene());

        Notifications.getInstance().subscribe(EventType.ABOUT, this, v -> watchAboutGame());

        Notifications.getInstance().subscribe(EventType.RESTART_LEVEL, this, v -> startLevel());

        Notifications.getInstance().subscribe(EventType.RESTART_GAME, this, v -> restartAllGame());

        Arkanoid.getStage().setScene(scenesManager.getScene("main"));
    }

    private void restartAllGame() {
        try {
            scoresManager.scanForScores();
            scenesManager.changeRecordsScene(scoresManager);
        } catch (Exception e) {
            alert(e.getMessage());
        }
        changeScene(scenesManager.getScene("main"));
    }

    private void endGame() {
        System.exit(0);
    }

    private void startGame() {
        Notifications.getInstance().subscribe(EventType.DESTROY, this, b -> {
            if (b instanceof Brick brick) {
                gameView.deleteBrick(brick.position());
                gameView.drawScore(model.getScore());
            }
        });

        Notifications.getInstance().subscribe(EventType.START_PLAYING_GAME, this, v -> setGameIsStarted());

        Notifications.getInstance().subscribe(EventType.PAUSE, this, v -> setPause());

        Notifications.getInstance().subscribe(EventType.MOVE_PLATFORM, this, v -> {
            if (v instanceof Double x) {
                movePlatform(x);
            }
        });

        currentLevel = 1;
        levelsInitiator = new LevelInitiator(currentLevel, levelsManager);
        startLevel();
    }

    private void backToMainScene() {
        changeScene(scenesManager.getScene("main"));
    }

    private void watchAboutGame() {
        changeScene(scenesManager.getScene("about"));
    }

    private void watchRecords() {
        changeScene(scenesManager.getScene("records"));
    }

    private void gameLose() {
        prepareForGameOver(scenesManager.getScene("game_over"));
    }

    private void gameWin() {
        currentLevel++;
        prepareForGameOver(scenesManager.getScene("game_win"));
        levelsInitiator = new LevelInitiator(currentLevel, levelsManager);
    }

    private void prepareForGameOver(Scene scene) {
        animation.stop();
        setRecord();
        gameIsStarted = false;
        changeScene(scene);
    }

    private void setRecord() {
        scoresManager.writeScore(levelsInitiator.getLevelName(), model.getScore());
        scoresManager.storeRecords();
    }

    private void startLevel() {
        try {
            model = levelsInitiator.initLevelModel();
            if (model == null) {
                changeScene(scenesManager.getScene("game_passed"));
                return;
            }
        } catch (GeneratingGameException e) {
            alert(e.getMessage());
        }

        gameView = levelsInitiator.initLevelView(model, scoresManager);
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

    private void changeScene(Scene scene) {
        Arkanoid.getStage().setScene(scene);
    }
}