package oop.arkanoid;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Duration;
import oop.arkanoid.model.GameLevel;
import oop.arkanoid.model.GeneratingGameException;
import oop.arkanoid.model.barrier.Brick;
import oop.arkanoid.notifications.*;
import oop.arkanoid.view.LevelView;

import java.io.*;

import static oop.arkanoid.AlertCreationUtil.createResourcesAlert;

public class Presenter {

    private final LevelsManager levelsManager = new LevelsManager();
    private final ScoresManager scoresManager = new ScoresManager();
    private final ScenesManager scenesManager = new ScenesManager();
    private Timeline animation;
    private LevelView gameView;
    private GameLevel model;
    private boolean gameIsStarted = false;
    private boolean isPause = false;
    private int currentLevel;
    private final Stage stage;

    Presenter(Stage stage) {
        this.stage = stage;
    }

    void setGameIsStarted() {
        gameIsStarted = true;
    }

    void setPause() {
        isPause = !isPause;
        if (isPause) {
            animation.pause();
        } else {
            animation.play();
        }
    }

    void movePlatform(double x) {
        if (gameIsStarted) {
            gameView.drawPlatform(model.updatePlatformPosition(x));
        }
    }

    void loadResourcesBeforeStartApp() throws IOException, GeneratingGameException {
        levelsManager.scanForLevels();
        levelsManager.checkGeneratingAllLevels();

        scoresManager.scanForScores();
        scenesManager.scanForScenes(scoresManager);

        Notifications.getInstance().subscribe(EventType.START_GAME, this, v -> startGame());

        Notifications.getInstance().subscribe(EventType.EXIT, this, v -> endGame());

        Notifications.getInstance().subscribe(EventType.RECORDS, this, v -> watchRecords());

        Notifications.getInstance().subscribe(EventType.BACK, this, v -> backToMainScene());

        Notifications.getInstance().subscribe(EventType.ABOUT, this, v -> watchAboutGame());

        stage.setScene(scenesManager.getScene("main"));
    }

    private void restartAllGame() {
        try {
            scoresManager.scanForScores();
            scenesManager.changeRecordsScene(scoresManager);
        } catch (IOException e) {
            createResourcesAlert(e.getMessage());
            endGame();
        }
        stage.setScene(scenesManager.getScene("main"));
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

        Notifications.getInstance().subscribe(EventType.RESTART_LEVEL, this, v -> startLevel());

        Notifications.getInstance().subscribe(EventType.RESTART_GAME, this, v -> restartAllGame());

        currentLevel = 1;
        startLevel();
    }

    private void backToMainScene() {
        stage.setScene(scenesManager.getScene("main"));
    }

    private void watchAboutGame() {
       stage.setScene(scenesManager.getScene("about"));
    }

    private void watchRecords() {
        stage.setScene(scenesManager.getScene("records"));
    }

    private void gameLose() {
        prepareForGameOver(scenesManager.getScene("game_over"));
    }

    private void gameWin() {
        prepareForGameOver(scenesManager.getScene("game_win"));
        currentLevel++;
    }

    private void prepareForGameOver(Scene scene) {
        animation.stop();
        setRecord();
        gameIsStarted = false;
        stage.setScene(scene);
    }

    private void setRecord() {
        scoresManager.writeScore("level" + currentLevel, model.getScore());
        scoresManager.storeRecords();
    }

    private void startLevel() {
        try {
            model = levelsManager.initLevelModel(currentLevel);
            if (model == null) {
                stage.setScene(scenesManager.getScene("game_passed"));
                return;
            }
        } catch (GeneratingGameException e) {
            createResourcesAlert(e.getMessage());
        }

        gameView = levelsManager.initLevelView(model, scoresManager);
        stage.setScene(gameView.getGameScene());
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

}