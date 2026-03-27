package be.ac.umons.backend.geometry;

/**
 * Represent a point in R^2, a point is going to be used in segment
 */
public class Point {
    private final double x;
    private final double y;

    /**
     *
     * @param x coordonnée en x
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

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Point)) return false;

        Point other = (Point) obj;
        return Double.compare(x, other.x) == 0 &&
                Double.compare(y, other.y) == 0;
    }
}