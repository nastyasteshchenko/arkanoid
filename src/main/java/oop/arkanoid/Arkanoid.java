package oop.arkanoid;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class Arkanoid extends Application {
    static private Stage stage;

    @Override
    public void start(Stage stage) throws IOException {
        Scene startScene = new Scene(FXMLLoader.load(Objects.requireNonNull(getClass().getResource("main-scene.fxml"))));
        stage.setTitle("Arkanoid");
        stage.setScene(startScene);
        Arkanoid.stage = stage;
        Arkanoid.stage.setResizable(false);
        Presenter.loadScenes();
        stage.show();
    }

    public static void changeScene(Scene scene) {
        stage.setScene(scene);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
