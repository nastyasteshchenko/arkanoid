package oop.arkanoid.view;

import javafx.scene.paint.Color;
import oop.arkanoid.model.Point;

public record BallView(Point position, double radius, Color color, Color strokeColor, String strokeWidth) {
}
