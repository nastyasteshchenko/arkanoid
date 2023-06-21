package oop.arkanoid.notifications;

import java.util.function.Consumer;

public record Subscriber(Consumer<Object> handler) {
}
