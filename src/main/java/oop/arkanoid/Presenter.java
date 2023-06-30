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
import oop.arkanoid.notifications.wrapper.DoubleWrapper;
import oop.arkanoid.notifications.wrapper.StringWrapper;
import oop.arkanoid.pane.*;
import oop.arkanoid.view.LevelView;

import java.io.*;
import java.net.URISyntaxException;

class Presenter {

    private LevelsManager levelsManager;
    private ScoresManager scoresManager;

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

    private Presenter(StackPane rootStackPane) {
        this.rootStackPane = rootStackPane;
    }

    static void startPresenter(StackPane stackPane) throws GeneratingGameException, IOException, URISyntaxException {
        Presenter presenter = new Presenter(stackPane);
        presenter.initialize();
    }

    private void initialize() throws IOException, GeneratingGameException, URISyntaxException {
        levelsManager = LevelsManager.create();
        levelsManager.checkGeneratingAllLevels();

        scoresManager = ScoresManager.create();

        NotificationsManager.getInstance().subscribe(new Subscriber<>(EventTypeWithNoData.START_GAME) {
            @Override
            public void actionPerformed(EventData eventData) {
                startGame();
            }
        });

        NotificationsManager.getInstance().subscribe(new Subscriber<>(EventTypeWithNoData.EXIT) {
            @Override
            public void actionPerformed(EventData eventData) {
                exitGame();
            }
        });

        NotificationsManager.getInstance().subscribe(new Subscriber<>(EventTypeWithNoData.RECORDS) {
            @Override
            public void actionPerformed(EventData eventData) {
                watchRecords();
            }
        });

        NotificationsManager.getInstance().subscribe(new Subscriber<>(EventTypeWithNoData.BACK) {
            @Override
            public void actionPerformed(EventData eventData) {
                updateMainPane(mainMenuPane);
            }
        });

        final AboutPane aboutPane = new AboutPane();

        NotificationsManager.getInstance().subscribe(new Subscriber<>(EventTypeWithNoData.ABOUT) {
            @Override
            public void actionPerformed(EventData eventData) {
                updateMainPane(aboutPane);
            }
        });

        NotificationsManager.getInstance().subscribe(new Subscriber<Brick>(EventTypeWithData.DESTROY_BRICK) {
            @Override
            public void actionPerformed(Brick brick) {
                gameView.deleteBrick(brick.position());
                gameView.drawScore(model.getScore());
            }
        });

        NotificationsManager.getInstance().subscribe(new Subscriber<>(EventTypeWithNoData.START_PLAYING_GAME) {
            @Override
            public void actionPerformed(EventData eventData) {
                startPlayingAnimation();
            }
        });

        NotificationsManager.getInstance().subscribe(new Subscriber<>(EventTypeWithNoData.PAUSE) {
            @Override
            public void actionPerformed(EventData eventData) {
                setPause();
            }
        });

        NotificationsManager.getInstance().subscribe(new Subscriber<DoubleWrapper>(EventTypeWithData.MOVE_PLATFORM) {
            @Override
            public void actionPerformed(DoubleWrapper x) {
                movePlatform(x.value());
            }
        });

        NotificationsManager.getInstance().subscribe(new Subscriber<>(EventTypeWithNoData.RESTART_LEVEL) {
            @Override
            public void actionPerformed(EventData eventData) {
                startLevel();
            }
        });

        NotificationsManager.getInstance().subscribe(new Subscriber<>(EventTypeWithNoData.RESTART_GAME) {
            @Override
            public void actionPerformed(EventData eventData) {
                restartAllGame();
            }
        });


        NotificationsManager.getInstance().subscribe(new Subscriber<StringWrapper>(EventTypeWithData.SAVE_SCORE) {
            @Override
            public void actionPerformed(StringWrapper author) {
                setRecord(author.value(), secondsPassed);
                prepareForLevelOver();
            }
        });

        NotificationsManager.getInstance().subscribe(new Subscriber<>(EventTypeWithNoData.DONT_SAVE_SCORE) {
            @Override
            public void actionPerformed(EventData eventData) {
                prepareForLevelOver();
            }
        });

        updateMainPane(mainMenuPane);

    }

    private void startPlayingAnimation() {
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
        scoresManager.writeScore(new SingleScore("level" + currentLevel, author, model.getScore(), time));
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