package oop.arkanoid.model;

public class Ball {

    private boolean isToChangeXDirection = false;
    private boolean isToChangeYDirection = false;
    private final double speed = 1.5;
    private double angle = Math.random() * 60 - 120;
    private final double radius;
    private double centerX;
    private double centerY;

    public Ball(double radius, double centerX, double centerY) {
        this.radius = radius;
        this.centerX = centerX;
        this.centerY = centerY;
    }

    public boolean isToChangeXDirection() {
        return isToChangeXDirection;
    }

    public void detectCollisionX() {
        this.isToChangeXDirection = true;
    }

    public boolean isToChangeYDirection() {
        return isToChangeYDirection;
    }

    public void detectCollisionY() {
        this.isToChangeYDirection = true;
    }

    public boolean isCollisionWithPlatform(Platform platform) {
        return centerX - radius <= platform.getX() + platform.getWidth() && centerX + radius >= platform.getX() &&
                Math.abs(centerY + radius - platform.getY()) <= speed / 2;
    }

    public boolean isCollisionWithBrickBottom(Brick brick) {
        return centerX - radius < brick.getX() + brick.getWidth() && centerX + radius > brick.getX()
                && Math.abs(centerY - radius - brick.getY() - brick.getHeight()) <= speed / 2;
    }

    public boolean isCollisionWithBrickTop(Brick brick) {
        return centerX - radius < brick.getX() + brick.getWidth() && centerX + radius > brick.getX()
                && Math.abs(centerY + radius - brick.getY()) <= speed / 2;
    }

    public boolean isCollisionWithBrickLeftSide(Brick brick) {
        return Math.abs(centerX + radius - brick.getX()) <= speed / 2
                && centerY + radius > brick.getY() && centerY - radius < brick.getY() + brick.getHeight();
    }

    public boolean isCollisionWithBrickRightSide(Brick brick) {
        return Math.abs(centerX - radius - brick.getX() - brick.getWidth()) <= speed / 2
                && centerY + radius > brick.getY() && centerY - radius < brick.getY() + brick.getHeight();
    }

    public void renewCoordinates() {
        centerX += speed * Math.cos(Math.toRadians(angle));
        centerY += speed * Math.sin(Math.toRadians(angle));
        isToChangeYDirection = false;
        isToChangeXDirection = false;
    }

    public double getRadius() {
        return radius;
    }

    public double getCenterX() {
        return centerX;
    }

    public double getCenterY() {
        return centerY;
    }

    public double getAngle() {
        return angle;
    }

    public void setAngle(double angle) {
        this.angle = angle;
    }

}
