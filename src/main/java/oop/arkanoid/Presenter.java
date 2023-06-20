package oop.arkanoid;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
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
    static final LevelsManager LEVELS_MANAGER = new LevelsManager();
    static final ScoresManager SCORES_MANAGER = new ScoresManager();
    private static final ScenesManager SCENES_MANAGER = new ScenesManager();
    private static LevelInitiator levelsInitiator;
    private static Timeline animation;
    private static LevelView gameView;
    private static GameLevel model;
    private static boolean gameIsStarted = false;
    private static boolean isPause = false;
    private static int currentLevel;

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

    static void checkGeneratingAllLevels() throws GeneratingGameException {
        LEVELS_MANAGER.checkGeneratingAllLevels();
    }

    static void loadResourcesBeforeStartApp() throws IOException {
        LEVELS_MANAGER.scanForLevels();
        SCORES_MANAGER.scanForScores();
        SCENES_MANAGER.scanForScenes();
    }

    @FXML
    protected void backToMenu() {
        try {
            SCORES_MANAGER.scanForScores();
            SCENES_MANAGER.changeRecordsScene();
        } catch (Exception e) {
            alert(e.getMessage());
        }
        changeScene(SCENES_MANAGER.getScene("main"));
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
        Notifications.getInstance().subscribe(EventType.DESTROY, this, b -> {
            Brick brick = (Brick) b;
            gameView.deleteBrick(brick.position());
            gameView.drawScore(model.getScore());
        });

        Notifications.getInstance().subscribe(EventType.START_GAME, this, v -> setGameIsStarted());

        Notifications.getInstance().subscribe(EventType.PAUSE, this, v -> setPause());

        Notifications.getInstance().subscribe(EventType.MOVE_PLATFORM, this, x -> movePlatform((double) x));

        currentLevel = 1;
        levelsInitiator = new LevelInitiator(currentLevel);
        startLevel();
    }

    @FXML
    protected void backToMainScene() {
        changeScene(SCENES_MANAGER.getScene("main"));
    }

    @FXML
    protected void watchAboutGame() {
        changeScene(SCENES_MANAGER.getScene("about"));
    }

    @FXML
    protected void watchRecords() {
        changeScene(SCENES_MANAGER.getScene("records"));
    }

    private void gameLose() {
        prepareForGameOver(SCENES_MANAGER.getScene("game_over"));
    }

    private void gameWin() {
        currentLevel++;
        prepareForGameOver(SCENES_MANAGER.getScene("game_win"));
        levelsInitiator = new LevelInitiator(currentLevel);
    }

    private void prepareForGameOver(Scene scene) {
        animation.stop();
        setRecord();
        gameIsStarted = false;
        changeScene(scene);
    }

    private void setRecord() {
        SCORES_MANAGER.writeScore(levelsInitiator.getLevelName(), model.getScore());
        SCORES_MANAGER.storeRecords();
    }

    private void startLevel() {
        try {
            model = levelsInitiator.initLevelModel();
            if (model == null) {
                changeScene(SCENES_MANAGER.getScene("game_passed"));
                return;
            }
        } catch (GeneratingGameException e) {
            alert(e.getMessage());
        }

        gameView = levelsInitiator.initLevelView(model);
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