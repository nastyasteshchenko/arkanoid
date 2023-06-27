package oop.arkanoid.view;

import javafx.scene.paint.Color;
import oop.arkanoid.model.Point;

public record PlatformView(Point position, Point size, Color color) {
}
