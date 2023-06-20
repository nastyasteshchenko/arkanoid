package oop.arkanoid;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

import static oop.arkanoid.AlertCreationUtil.alert;

public class Arkanoid extends Application {
    private static Stage stage;

    @Override
    public void start(Stage stage) throws IOException {
        Scene startScene = new Scene(FXMLLoader.load(Objects.requireNonNull(getClass().getResource("FXML/main-scene.fxml"))));
        stage.setTitle("Arkanoid");
        stage.setScene(startScene);
        Arkanoid.stage = stage;
        Arkanoid.stage.setResizable(false);
        try {
            Presenter.loadResourcesBeforeStartApp();
            Presenter.checkGeneratingAllLevels();
        } catch (Exception e) {
            //TODO подумать над тем чтобы перенести в presenter???
            //передавать не исключение, а сообщение
            createAlert(e);
            return;
        }
        stage.show();
    }

    static void createAlert(Exception errorMsg) {
        alert(stage, errorMsg);
    }

    static void changeScene(Scene scene) {
        stage.setScene(scene);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
