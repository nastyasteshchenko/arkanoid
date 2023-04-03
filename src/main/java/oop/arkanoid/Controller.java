package oop.arkanoid;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import oop.arkanoid.model.Model;
import oop.arkanoid.view.FirstLevelView;
import oop.arkanoid.view.LevelView;
import oop.arkanoid.view.SecondLevelView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;

public class Controller {

    private static Properties records = new Properties();

    private static int numLevel = 0;

    private static Timeline animation;

    private static Timeline pauseTimeline;

    private static LevelView gameView = new FirstLevelView();

    private static final Model model = new Model(LevelView.SCENE_HEIGHT, LevelView.SCENE_WIDTH);

    private void pause() {
        pauseTimeline = new Timeline(new KeyFrame(Duration.millis(2.5), ae -> {
            if (gameView.isPause()) {
                animation.pause();
            } else {
                animation.play();
                pauseTimeline.stop();
            }
        }));
        pauseTimeline.setCycleCount(Animation.INDEFINITE);
        pauseTimeline.play();
    }

    private void setRecord(){
        if(model.getScore()>Integer.parseInt(records.getProperty(String.valueOf(numLevel)))){
            records.setProperty(String.valueOf(numLevel), String.valueOf(model.getScore()));
        }
    }
    private void moveBall() {
        animation = new Timeline(new KeyFrame(Duration.millis(2.5), ae -> {
            if (gameView.isStartMovingBall()) {
                try {
                    gameView.deleteBlock(model.detectCollisionsWithBlocks());
                    gameView.moveBall(model.recountBallCoordinates());
                    gameView.changeScore(model.getScore());
                    if (model.getAmountOfBreakableBlocks() == 0) {
                        Arkanoid.gameWin();
                        setRecord();
                        ++numLevel;
                        animation.stop();
                        if (pauseTimeline != null)
                            pauseTimeline.stop();
                    }
                    if (gameView.isPause()) {
                        pause();
                    }
                    gameView.movePlatform(model.recountPlatformX(gameView.getPlatformX()));
                    if (model.isGameOver()) {
                        Arkanoid.gameOver();
                        setRecord();
                        animation.stop();
                        if (pauseTimeline != null)
                            pauseTimeline.stop();
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }));
        animation.setCycleCount(Animation.INDEFINITE);
        animation.play();
    }

    @FXML
    protected void restartGame() throws IOException {
        records.store(new FileOutputStream("src/main/resources/oop/arkanoid/records.properties"), null);
        gameView.clear();
        startGame();
    }

    private void startFirstLevel() {

        gameView.setHighScoreCountLabel(records.getProperty(String.valueOf(numLevel)));
        gameView.render();

        model.restartModel(FirstLevelView.AMOUNT_OF_BLOCKS, FirstLevelView.AMOUNT_OF_BREAKABLE_BLOCKS);
        Rectangle platform = gameView.getPlatform();
        model.setPlatform(platform.getX(), platform.getY(), platform.getWidth(), platform.getHeight());

        Circle ball = gameView.getBall();
        model.setBall(ball.getRadius(), ball.getCenterX(), ball.getCenterY());

        HashMap<String, Rectangle> blocks = gameView.getBlocks();

        for (int i = 0; i < blocks.size(); i++) {
            Rectangle block = blocks.get(String.valueOf(i));
            model.addStandartBlock(block.getX(), block.getY(), block.getWidth(), block.getHeight(), block.getId());
        }

        Arkanoid.changeScene(gameView.getGameScene());
        moveBall();
    }

    private void startSecondLevel() {
        gameView = new SecondLevelView();
        gameView.setHighScoreCountLabel(records.getProperty(String.valueOf(numLevel)));
        gameView.render();

        model.restartModel(SecondLevelView.AMOUNT_OF_BLOCKS, SecondLevelView.AMOUNT_OF_BREAKABLE_BLOCKS);
        Rectangle platform = gameView.getPlatform();
        model.setPlatform(platform.getX(), platform.getY(), platform.getWidth(), platform.getHeight());

        Circle ball = gameView.getBall();
        model.setBall(ball.getRadius(), ball.getCenterX(), ball.getCenterY());

        HashMap<String, Rectangle> blocks = gameView.getBlocks();

        for (int i = 0; i < SecondLevelView.AMOUNT_OF_BREAKABLE_BLOCKS; i++) {
            Rectangle block = blocks.get(String.valueOf(i));
            model.addStandartBlock(block.getX(), block.getY(), block.getWidth(), block.getHeight(), block.getId());
        }

        for (int i = SecondLevelView.AMOUNT_OF_BREAKABLE_BLOCKS; i < SecondLevelView.AMOUNT_OF_BLOCKS; i++) {
            Rectangle block = blocks.get(String.valueOf(i));
            model.addIndestructibleBlock(block.getX(), block.getY(), block.getWidth(), block.getHeight(), block.getId());
        }

        Arkanoid.changeScene(gameView.getGameScene());
        moveBall();
    }

    @FXML
    protected void endGame() {
        System.exit(0);
    }

    @FXML
    protected void startGame() throws IOException {
        if (numLevel == 0) {
            records.load(new FileInputStream("src/main/resources/oop/arkanoid/records.properties"));
            numLevel++;
        }

        switch (numLevel) {
            case 1 -> startFirstLevel();
            case 2 -> startSecondLevel();
        }
    }

}