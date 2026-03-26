package be.ac.umons.backend.geometry;

public abstract class Segment {

    protected final Point p1;
    protected final Point p2;


    /**
     *
     * @param p1 first extremity
     * @param p2 second extremity
     */
    public Segment(Point p1, Point p2) {
        this.p1 = p1;
        this.p2 = p2;
    }

    /**
     * Return the point to store in the PST, the left extremity for horizontal segment
     * and the bottom extremity for the vertical segment
     */
    public abstract Point getStoredPoint();

    /**
     * Return the other Point of the segment (the one that should not be stored in the PST)
     */
    public abstract Point getOtherPoint();

    public Point getP1() {
        return p1;
    }

    public Point getP2() {
        return p2;
    }

    @Override
    public String toString() {
        return String.format("(%s, %s)", p1, p2);
    }
}