package oop.arkanoid;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;


public class Arkanoid extends Application {

    @Override
    public void start(Stage stage) {
        stage.setTitle("Arkanoid");

        StackPane mainStackPane = new StackPane();

        try {
            Presenter.startPresenter(mainStackPane);
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Error");
            alert.setContentText("Error: " + e.getMessage());
            alert.showAndWait();
            return;
        }

        stage.setScene(new Scene(mainStackPane));
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
