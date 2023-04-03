package oop.arkanoid;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class Arkanoid extends Application {
    static private Stage stage;

    @Override
    public void start(Stage stage) throws IOException {
        Parent fxmlLoader1 = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("main-scene.fxml")));
        Scene startScene = new Scene(fxmlLoader1, Color.MOCCASIN);
        stage.setTitle("Arcanoid");
        stage.setScene(startScene);
        Arkanoid.stage = stage;
        stage.show();
    }

    public static void gameOver() throws IOException {
        Scene scene = new Scene(FXMLLoader.load(Objects.requireNonNull(Arkanoid.class.getResource("game-over-scene.fxml"))), Color.MOCCASIN);
        changeScene(scene);
    }

    public static void gameWin() throws IOException {
        Parent fxmlLoader1 = FXMLLoader.load(Objects.requireNonNull(Arkanoid.class.getResource("game-win-scene.fxml")));
        Scene scene = new Scene(fxmlLoader1, Color.MOCCASIN);
        changeScene(scene);
    }
    public static void changeScene(Scene scene) {
        stage.setScene(scene);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
