package be.ac.umons.backend.geometry;

/**
 * Represent a point in R^2, a point is going to be used in a segment
 */
public class Point {
    private final double x;
    private final double y;

    /**
     *Creates a point with the given coordinates.
     * 
     * @param x coordonnée en x
     * @param y coordonnée en y
     */
    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Returns the x coordinate of this point.
     *
     * @return the x coordinate
     */
    public double getX() {
        return x;
    }

    /**
     * Returns the y coordinate of this point.
     *
     * @return the y coordinate
     */
    public double getY() {
        return y;
    }

    /**
     * Returns a string representation of this point in the format: Point{x=..., y=...}
     *
     * @return a string representation of this point
     */
    @Override
    public String toString() {
        return "Point{" + "x=" + x + ", y=" + y + '}';
    }

    /**
     * Checks whether this point is equal to another object.
     * Two points are equal if they have the same x and y coordinates.
     *
     * @param obj the object to compare with
     * @return true if obj is a Point with the same coordinates, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Point)) return false;

        Point other = (Point) obj;
        return Double.compare(x, other.x) == 0 &&
                Double.compare(y, other.y) == 0;
    }
}