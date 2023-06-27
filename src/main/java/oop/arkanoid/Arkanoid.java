package oop.arkanoid;

import javafx.application.Application;
import javafx.stage.Stage;

import static oop.arkanoid.AlertCreationUtil.createResourcesAlert;

public class Arkanoid extends Application {
    private static Stage stage;

    @Override
    public void start(Stage stage) {
        stage.setTitle("Arkanoid");
        Arkanoid.stage = stage;
        Arkanoid.stage.setResizable(false);

        Presenter presenter = new Presenter();

        try {
            presenter.loadResourcesBeforeStartApp();
            presenter.checkGeneratingAllLevels();
        } catch (Exception e) {
            createResourcesAlert(e.getMessage());
            return;
        }

        stage.show();

    }

    public static Stage getStage() {
        return stage;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
