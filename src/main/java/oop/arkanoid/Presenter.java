package oop.arkanoid;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import oop.arkanoid.model.GameLevel;
import oop.arkanoid.model.GeneratingGameException;
import oop.arkanoid.model.barrier.Brick;
import oop.arkanoid.notifications.*;
import oop.arkanoid.pane.*;
import oop.arkanoid.view.LevelView;

import java.io.*;

import static oop.arkanoid.AlertCreationUtil.createResourcesAlert;

class Presenter {

    private final LevelsManager levelsManager = new LevelsManager();
    private final ScoresManager scoresManager = new ScoresManager();
    private final ScenesManager scenesManager = new ScenesManager();

    private final AboutPane aboutPane = new AboutPane();
    private final GameOverPane gameOverPane = new GameOverPane();
    private final GamePassedPane gamePassedPane = new GamePassedPane();
    private final GameWinPane gameWinPane = new GameWinPane();
    private final MainMenuPane mainMenuPane = new MainMenuPane();

    private Timeline animation;
    private LevelView gameView;
    private GameLevel model;
    private boolean gameIsStarted = false;
    private boolean isPause = false;
    private int currentLevel;
    private final StackPane rootStackPane;

    Presenter(StackPane rootStackPane) {
        this.rootStackPane = rootStackPane;
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

       updateMainPane(mainMenuPane);
    }

    private void setGameIsStarted() {
        gameIsStarted = true;
    }

    private void setPause() {
        isPause = !isPause;
        if (isPause) {
            animation.pause();
        } else {
            animation.play();
        }
    }

    private void movePlatform(double x) {
        if (gameIsStarted) {
            gameView.drawPlatform(model.updatePlatformPosition(x));
        }
    }

    private void restartAllGame() {
        try {
            scoresManager.scanForScores();
            scenesManager.changeRecordsScene(scoresManager);
        } catch (IOException e) {
            createResourcesAlert(e.getMessage());
            endGame();
        }
       updateMainPane(mainMenuPane);
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
       updateMainPane(mainMenuPane);
    }

    private void watchAboutGame() {
        updateMainPane(aboutPane);
    }

    private void watchRecords() {
       //updateMainPane();
    }

    private void gameLose() {
        prepareForGameOver(gameOverPane);
    }

    private void gameWin() {
        prepareForGameOver(gameWinPane);
        currentLevel++;
    }

    private void prepareForGameOver(Pane pane) {
        animation.stop();
        setRecord();
        gameIsStarted = false;
        updateMainPane(pane);
    }

    private void setRecord() {
        scoresManager.writeScore("level" + currentLevel, model.getScore());
        scoresManager.storeRecords();
    }

    private void startLevel() {
        try {
            model = levelsManager.initLevelModel(currentLevel);
            if (model == null) {
               updateMainPane(gamePassedPane);
                return;
            }
        } catch (GeneratingGameException e) {
            createResourcesAlert(e.getMessage());
        }

        gameView = levelsManager.initLevelView(model, scoresManager);
        updateMainPane(gameView.getGamePane());
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

    private void updateMainPane(Pane pane) {
        rootStackPane.getChildren().clear();
        rootStackPane.getChildren().add(pane);
    }

}