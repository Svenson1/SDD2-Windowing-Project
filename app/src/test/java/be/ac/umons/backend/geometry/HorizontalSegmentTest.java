package be.ac.umons.backend.geometry;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class HorizontalSegmentTest {

    @Test
    void testStoredPoint_whenP1IsLeft() {
        Point p1 = new Point(1, 5);
        Point p2 = new Point(4, 5);

        HorizontalSegment s = new HorizontalSegment(p1, p2);

        assertEquals(p1, s.getStoredPoint());
        assertEquals(p2, s.getOtherPoint());
    }

    @Test
    void testStoredPoint_whenP2IsLeft() {
        Point p1 = new Point(10, 5);
        Point p2 = new Point(2, 5);

        HorizontalSegment s = new HorizontalSegment(p1, p2);

        assertEquals(p2, s.getStoredPoint());
        assertEquals(p1, s.getOtherPoint());
    }

    @Test
    void testStoredPoint_whenEqualX() {
        Point p1 = new Point(3, 5);
        Point p2 = new Point(3, 5);

        HorizontalSegment s = new HorizontalSegment(p1, p2);


        assertEquals(p1, s.getStoredPoint());
        assertEquals(p2, s.getOtherPoint());
    }
}