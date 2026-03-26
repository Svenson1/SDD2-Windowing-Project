package be.ac.umons.backend.geometry;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PointTest {

    @Test
    void testConstructorAndGetters() {
        Point p = new Point(3.5, -2.1);

        assertEquals(3.5, p.getX());
        assertEquals(-2.1, p.getY());
    }
}