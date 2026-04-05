package be.ac.umons.backend.geometry;

/**
 * Vertical segment with the x constant
 * We stock the bottom extremity into the pst
 */
public class VerticalSegment extends Segment {

    /**
     *Creates a vertical segment with the given endpoints.
     * 
     * @param p1 the first extremity point
     * @param p2 the second extremity point
     */
    public VerticalSegment(Point p1, Point p2) {
        super(p1, p2);
    }

    /**
     * Returns the bottom endpoint of this segment, i.e. the one with the lower y coordinate.
     *
     * @return the bottom endpoint
     */
    @Override
    public Point getStoredPoint() {
        if (p1.getY() <= p2.getY()) {
            return p1;
        }
        return p2;
    }

    /**
     * Returns the top endpoint of this segment, i.e. the one with the higher y coordinate.
     *
     * @return the top endpoint
     */
    @Override
    public Point getOtherPoint() {
       if(p1.getY() <= p2.getY()) {
           return p2;
       }
       return p1;
    }
}
