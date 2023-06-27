package oop.arkanoid.view;

import javafx.scene.paint.Color;
import oop.arkanoid.model.Point;

public record BrickView(Point position, Point size, Color color, Color strokeColor, String strokeWidth) {
}
