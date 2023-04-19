package oop.arkanoid.model;

public class Ball {

    private boolean isVerticalCollisionWithBrick = false;
    private boolean isHorizontalCollisionWithBrick = false;
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

    public boolean isVerticalCollisionWithBrick() {
        return isVerticalCollisionWithBrick;
    }

    public void detectVerticalCollisionWithBrick() {
        this.isVerticalCollisionWithBrick = true;
    }

    public boolean isHorizontalCollisionWithBrick() {
        return isHorizontalCollisionWithBrick;
    }

    public void detectHorizontalCollisionWithBrick() {
        this.isHorizontalCollisionWithBrick = true;
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

    public boolean isCollisionWithUpperSceneSide() {
        return centerY <= radius;
    }

    public boolean isCollisionWithRightSceneSide(double sceneWidth){
        return centerX >= sceneWidth - radius;
    }

    public boolean isCollisionWithLeftSceneSide(){
        return centerX <= radius;
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
        isHorizontalCollisionWithBrick = false;
        isVerticalCollisionWithBrick = false;
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
