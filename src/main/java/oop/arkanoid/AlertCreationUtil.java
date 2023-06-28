package oop.arkanoid;

import javafx.scene.control.Alert;

class AlertCreationUtil {

    static void createResourcesAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText("Error initializing resources");
        alert.setContentText("Error: " + msg);
        alert.showAndWait();
    }

}
