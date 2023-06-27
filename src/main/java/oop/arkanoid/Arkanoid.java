package oop.arkanoid;

import javafx.application.Application;
import javafx.stage.Stage;

import static oop.arkanoid.AlertCreationUtil.createResourcesAlert;

public class Arkanoid extends Application {

    @Override
    public void start(Stage stage) {
        stage.setTitle("Arkanoid");
        stage.setResizable(false);

        Presenter presenter = new Presenter(stage);

        try {
            presenter.loadResourcesBeforeStartApp();
        } catch (Exception e) {
            createResourcesAlert(e.getMessage());
            return;
        }

        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
