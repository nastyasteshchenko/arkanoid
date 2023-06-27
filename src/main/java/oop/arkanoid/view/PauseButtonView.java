package oop.arkanoid.view;

import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import oop.arkanoid.model.Point;

public record PauseButtonView(Point position, Point size, Font font, String fontSize, Color textColor, String backgroundStyle) {
}
