package be.ac.umons.backend.geometry;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class VerticalSegmentTest {

    @Test
    void testStoredPoint_whenP1IsBottom() {
        Point p1 = new Point(2, 1);
        Point p2 = new Point(2, 5);

        VerticalSegment s = new VerticalSegment(p1, p2);

        assertEquals(p1, s.getStoredPoint());
        assertEquals(p2, s.getOtherPoint());
    }

    @Test
    void testStoredPoint_whenP2IsBottom() {
        Point p1 = new Point(2, 10);
        Point p2 = new Point(2, 3);

        VerticalSegment s = new VerticalSegment(p1, p2);

        assertEquals(p2, s.getStoredPoint());
        assertEquals(p1, s.getOtherPoint());
    }

    @Test
    void testStoredPoint_whenSameY() {
        Point p1 = new Point(2, 4);
        Point p2 = new Point(2, 4);

        VerticalSegment s = new VerticalSegment(p1, p2);

        assertEquals(p1, s.getStoredPoint());
        assertEquals(p2, s.getOtherPoint());
    }
}