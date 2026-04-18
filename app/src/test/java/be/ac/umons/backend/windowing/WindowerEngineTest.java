package be.ac.umons.backend.windowing;

import be.ac.umons.backend.geometry.HorizontalSegment;
import be.ac.umons.backend.geometry.Point;
import be.ac.umons.backend.geometry.VerticalSegment;
import be.ac.umons.backend.windowing.Window;
import be.ac.umons.backend.windowing.WindowerEngine;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class WindowerEngineTest {

    private static final Window W = new Window(0, 10, 0, 10);

    private static HorizontalSegment h(double x1, double y, double x2) {
        return new HorizontalSegment(new Point(x1, y), new Point(x2, y));
    }

    private static VerticalSegment v(double x, double y1, double y2) {
        return new VerticalSegment(new Point(x, y1), new Point(x, y2));
    }

    private static <T> List<T> modifiableList(T... elements) {
        List<T> list = new ArrayList<>();
        for (T element : elements) {
            list.add(element);
        }
        return list;
    }

    @Test
    void emptyEngine_returnsEmptyList() {
        WindowerEngine e = new WindowerEngine(new ArrayList<>(), new ArrayList<>());
        assertTrue(e.query(W).isEmpty());
    }

    @Test
    void horizontalSegment_insideWindow_isReported() {
        WindowerEngine e = new WindowerEngine(modifiableList(h(2, 5, 8)), new ArrayList<>());
        assertEquals(1, e.query(W).size());
    }

    @Test
    void horizontalSegment_outsideWindow_isNotReported() {
        WindowerEngine e = new WindowerEngine(modifiableList(h(2, 15, 8)), new ArrayList<>());
        assertTrue(e.query(W).isEmpty());
    }

    @Test
    void horizontalSegment_crossingWindow_isReported() {
        WindowerEngine e = new WindowerEngine(modifiableList(h(-5, 5, 15)), new ArrayList<>());
        assertEquals(1, e.query(W).size());
    }

    @Test
    void verticalSegment_insideWindow_isReported() {
        WindowerEngine e = new WindowerEngine(new ArrayList<>(), modifiableList(v(5, 2, 8)));
        assertEquals(1, e.query(W).size());
    }

    @Test
    void verticalSegment_outsideWindow_isNotReported() {
        WindowerEngine e = new WindowerEngine(new ArrayList<>(), modifiableList(v(15, 2, 8)));
        assertTrue(e.query(W).isEmpty());
    }

    @Test
    void verticalSegment_crossingWindow_isReported() {
        WindowerEngine e = new WindowerEngine(new ArrayList<>(), modifiableList(v(5, -5, 15)));
        assertEquals(1, e.query(W).size());
    }

    @Test
    void invalidWindow_throwsException() {
        WindowerEngine e = new WindowerEngine(new ArrayList<>(), new ArrayList<>());
        assertThrows(IllegalArgumentException.class,
                () -> e.query(new Window(10, 0, 0, 10)));
    }
}