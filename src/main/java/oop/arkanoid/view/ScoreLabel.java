package oop.arkanoid.view;

import javafx.scene.text.Font;
import oop.arkanoid.model.Point;

public record ScoreLabel(Point position, int score, Font font, String fontSize) {
}
