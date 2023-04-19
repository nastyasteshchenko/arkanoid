package oop.arkanoid.model2;

public record Point(double x, double y) implements Comparable<Point> {

    @Override
    public int compareTo(Point o) {
        int xRes = Double.compare(x, o.x);
        if (xRes > 0) {

        }
        return 0;
    }
}
