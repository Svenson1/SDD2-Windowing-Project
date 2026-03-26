package be.ac.umons.backend.geometry;

/**
 * Horizontal segment, with the y constant
 * With stock the left extremity in the PST
 */
public class HorizontalSegment extends Segment{

    /**
     *
     * @param p1 first extremity
     * @param p2 second extremity
     */
    public HorizontalSegment(Point p1, Point p2) {
        super(p1, p2);
    }

    /**
     *
     * @return the lef extremity of the segment (with the minimal x)
     */
    @Override
    public Point getStoredPoint() {
        if ( p1.getX() <= p2.getX() ) {
            return p1;
        }
        return p2;
    }

    /**
     *
     * @return the right extremity of the segment
     */
    @Override
    public Point getOtherPoint() {
        if ( p1.getX() <= p2.getX() ) {
            return p2;
        }
        return p1;
    }


}