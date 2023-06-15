module oop.arkanoid {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;

    opens oop.arkanoid to javafx.fxml;
    exports oop.arkanoid;
    exports oop.arkanoid.model;
    opens oop.arkanoid.model to javafx.fxml;
    exports oop.arkanoid.view;
    opens oop.arkanoid.view to javafx.fxml;
    exports oop.arkanoid.model.barrier;
    opens oop.arkanoid.model.barrier to javafx.fxml;
}