package oop.arkanoid.view;

import javafx.scene.paint.Color;
import oop.arkanoid.model.Point;

public record GameSceneView(Point size, Color color, double opacity) {
}
