package oop.arkanoid;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;
import oop.arkanoid.model.GameLevel;
import oop.arkanoid.model.GameState;
import oop.arkanoid.model.GeneratingGameException;
import oop.arkanoid.model.barrier.Brick;
import oop.arkanoid.notifications.*;
import oop.arkanoid.pane.*;
import oop.arkanoid.view.LevelView;

import java.io.*;
import java.net.URISyntaxException;

class Presenter {

    private final LevelsManager levelsManager = new LevelsManager();
    private final ScoresManager scoresManager = new ScoresManager();

    private final GameLosePane gameLosePane = new GameLosePane();
    private final GamePassedPane gamePassedPane = new GamePassedPane();
    private final GameWinPane gameWinPane = new GameWinPane();
    private final MainMenuPane mainMenuPane = new MainMenuPane();
    private final SaveScorePane saveScorePane = new SaveScorePane();
    private final ScoresPane scoresPane = new ScoresPane();
    private final StackPane rootStackPane;

    private double secondsPassed = 0.;

    private Timeline animation;
    private LevelView gameView;
    private GameLevel model;
    private boolean isPause = false;
    private int currentLevel;

    Presenter(StackPane rootStackPane) {
        this.rootStackPane = rootStackPane;
    }


    void initialize() throws IOException, GeneratingGameException, URISyntaxException {
        levelsManager.scanForLevels();
        levelsManager.checkGeneratingAllLevels();

        scoresManager.scanForScores();

        NotificationsManager.getInstance().subscribe(EventType.START_GAME, this, v -> startGame());
        NotificationsManager.getInstance().subscribe(EventType.EXIT, this, v -> exitGame());
        NotificationsManager.getInstance().subscribe(EventType.RECORDS, this, v -> watchRecords());
        NotificationsManager.getInstance().subscribe(EventType.BACK, this, v -> updateMainPane(mainMenuPane));

        final AboutPane aboutPane = new AboutPane();
        NotificationsManager.getInstance().subscribe(EventType.ABOUT, this, v -> updateMainPane(aboutPane));

        NotificationsManager.getInstance().subscribe(EventType.DESTROY, this, b -> {
            if (b instanceof Brick brick) {
                gameView.deleteBrick(brick.position());
                gameView.drawScore(model.getScore());
            }
        });

        NotificationsManager.getInstance().subscribe(EventType.START_PLAYING_GAME, this, v -> setGameIsStarted());
        NotificationsManager.getInstance().subscribe(EventType.PAUSE, this, v -> setPause());
        NotificationsManager.getInstance().subscribe(EventType.MOVE_PLATFORM, this, v -> {
            if (v instanceof Double x) {
                movePlatform(x);
            }
        });

        NotificationsManager.getInstance().subscribe(EventType.RESTART_LEVEL, this, v -> startLevel());
        NotificationsManager.getInstance().subscribe(EventType.RESTART_GAME, this, v -> restartAllGame());
        NotificationsManager.getInstance().subscribe(EventType.SAVE_SCORE, this, str -> {
            if (str instanceof String name) {
                setRecord(name, secondsPassed);
                prepareForLevelOver();
            }
        });

        NotificationsManager.getInstance().subscribe(EventType.DONT_SAVE_SCORE, this, v -> prepareForLevelOver());

        updateMainPane(mainMenuPane);
    }

    private void setGameIsStarted() {
        animation.play();
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
        if (animation.getCurrentRate() != 0.) {
            gameView.drawPlatform(model.updatePlatformPosition(x));
        }
    }

    private void restartAllGame() {
        updateMainPane(mainMenuPane);
    }

    private void exitGame() {
        System.exit(0);
    }

    private void endGame() {
        animation.stop();

        if (scoresManager.isNewScore("level" + currentLevel, model.getScore(), secondsPassed)) {
            saveScorePane.setShowingScore(model.getScore(), secondsPassed);
            updateMainPane(saveScorePane);
        } else {
            prepareForLevelOver();
        }
    }

    private void startGame() {
        currentLevel = 1;
        startLevel();
    }

    private void watchRecords() {
        scoresPane.updateScores(scoresManager.getScores());
        updateMainPane(scoresPane);
    }

    private void prepareForLevelOver() {
        animation.stop();
        switch (model.gameState()) {
            case WIN -> {
                updateMainPane(gameWinPane);
                currentLevel++;
            }
            case LOSE -> updateMainPane(gameLosePane);
        }
    }

    private void setRecord(String author, double time) {
        scoresManager.writeScore("level" + currentLevel, author, model.getScore(), time);
        scoresManager.storeRecords();
    }

    private void startLevel() {
        String levelName = "level" + currentLevel;
        try {
            model = levelsManager.initLevelModel(levelName);
            if (model == null) {
                updateMainPane(gamePassedPane);
                return;
            }
        } catch (GeneratingGameException ignore) {
            //never throws because all checks have already done
        }

        gameView = levelsManager.initLevelView(model, scoresManager.getScoreForLevel(levelName));
        updateMainPane(gameView.getGamePane());
        startAnimation();

    }

    private void startAnimation() {
        animation = new Timeline(new KeyFrame(Duration.millis(2.5), ae -> {
            secondsPassed += animation.getKeyFrames().get(0).getTime().toSeconds();
            gameView.drawBall(model.nextBallPosition());
            if (model.gameState() != GameState.PROCESS) {
                endGame();
            }
        }));
        animation.setCycleCount(Animation.INDEFINITE);
    }

    private void updateMainPane(Pane pane) {
        rootStackPane.getChildren().clear();
        rootStackPane.getChildren().add(pane);
    }

}