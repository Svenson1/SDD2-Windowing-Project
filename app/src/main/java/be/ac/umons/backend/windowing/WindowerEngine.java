package be.ac.umons.backend.windowing;

import be.ac.umons.backend.geometry.Segment;
import be.ac.umons.backend.pst.PST;
import be.ac.umons.backend.pst.PstH;
import be.ac.umons.backend.pst.PstV;

import java.util.ArrayList;
import java.util.List;

/**
 * Engine that applies windowing queries to a set of axis-aligned segments.
 * This engine relies on two Priority Search Trees (PST):
 * one for horizontal segments and one for vertical segments.
 * This allows efficient querying in O(log n + k), where k is the number
 * of reported segments.
 *Horizontal segments are stored in a PST where the heap axis is x and the sort axis is y.
 *Vertical segments are stored in a PST where the heap axis is y and the sort axis is x.
 */
public class WindowerEngine {
    /**
     * Priority Search Tree for horizontal segments.
     */
    private final PST pstH;

    /**
     * Priority Search Tree for vertical segments.
     */
    private final PST pstV;

    /**
     * Constructs a windowing engine from two lists of segments.
     *The input lists must already be separated into horizontal and vertical segments.
     *
     * @param horizontals list of horizontal segments
     * @param verticals   list of vertical segments
     */
    public WindowerEngine(List<Segment> horizontals, List<Segment> verticals) {
        this.pstH = new PstH();
        this.pstV = new PstV();

        pstH.build(horizontals);
        pstV.build(verticals);
    }

    /**
     * Applies a windowing query on the stored segments.
     *
     *The query window is defined as [xMin, xMax] × [yMin, yMax].
     *
     * The process is the following:
     * Query the horizontal PST using x as heap axis and y as sort axis.
     * Query the vertical PST using y as heap axis and x as sort axis
     *
     * @param w the query window
     * @return list of segments intersecting the window
     * @throws IllegalArgumentException if the window is not valid
     */
    public List<Segment> query(Window w) {
        if (!w.isValid()) {
            throw new IllegalArgumentException("Invalid window : " + w);
        }

        List<Segment> results = new ArrayList<>();

        // Horizontal segments
        results.addAll(pstH.query(w.xMin, w.xMax, w.yMin, w.yMax));

        // Vertical segments (axes logically swapped)
        results.addAll(pstV.query(w.yMin, w.yMax, w.xMin, w.xMax));

        return results;
    }
}