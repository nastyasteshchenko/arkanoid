package oop.arkanoid;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import static oop.arkanoid.AlertCreationUtil.createResourcesAlert;

public class Arkanoid extends Application {

    @Override
    public void start(Stage stage) {
        stage.setTitle("Arkanoid");

        StackPane mainStackPane = new StackPane();
        Presenter presenter = new Presenter(mainStackPane);

        try {
            presenter.loadResourcesBeforeStartApp();
        } catch (Exception e) {
            createResourcesAlert(e.getMessage());
            return;
        }

        stage.setScene(new Scene(mainStackPane));
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
