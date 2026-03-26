package backend.geometry;

/**
 * Represent a point in R^2, a point is going to be used in segment
 */
public class Point {
    private final double x;
    private final double y;

    /**
     *
     * @param x coordonée en x
     * @param y coordonnée en y
     */
    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }
    public double getY() {
        return y;
    }

    @Override
    public String toString() {
        return "Point{" + "x=" + x + ", y=" + y + '}';
    }
}