package be.ac.umons.backend.geometry;

/**
 * Vertical segment with the x constant
 * We stock the bottom extremity into the pst
 */
public class VerticalSegment extends Segment {

    /**
     *
     * @param p1 the first extremity point
     * @param p2 the second extremity point
     */
    public VerticalSegment(Point p1, Point p2) {
        super(p1, p2);
    }

    /**
     *
     * @return the point that is at the bottom of the segment ie. with the lower y
     */
    @Override
    public Point getStoredPoint() {
        if (p1.getY() <= p2.getY()) {
            return p1;
        }
        return p2;
    }

    /**
     *
     * @return the point that is at the top of the segment ie. with the higher y
     */
    @Override
    public Point getOtherPoint() {
       if(p1.getY() <= p2.getY()) {
           return p2;
       }
       return p1;
    }
}
