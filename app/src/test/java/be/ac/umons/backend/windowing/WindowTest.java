package be.ac.umons.backend.windowing;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class WindowTest {
    @Test
    void constructor_assignsFieldsCorrectly() {
        Window w = new Window(-5, 10, -3, 7);
        assertEquals(-5, w.xMin);
        assertEquals(10, w.xMax);
        assertEquals(-3, w.yMin);
        assertEquals(7,  w.yMax);
    }
    
    @Test
    void isValid_whenAllBoundsAreValid() {
        Window w = new Window(0, 10, 0, 10);
        assertTrue(w.isValid());
    }

    @Test void isValid_whenXMinIsGreaterThanXMax() {
        Window w = new Window(10, 0, 0, 10);
        assertFalse(w.isValid());
    }
    
    @Test void isValid_whenYMinIsGreaterThanYMax() {
        Window w = new Window(0, 10, 10, 0);
        assertFalse(w.isValid());
    }
    
    @Test
    void isValid_whenXMinEqualsInfinity() {
        Window w = new Window(Double.NEGATIVE_INFINITY, 10, 0, 10);
        assertTrue(w.isValid());
    }
}
