package be.ac.umons.backend.windowing;

import be.ac.umons.backend.geometry.HorizontalSegment;
import be.ac.umons.backend.geometry.Point;
import be.ac.umons.backend.geometry.Segment;
import be.ac.umons.backend.pst.PST;

import java.util.ArrayList;
import java.util.List;

/**
 *Engine that apply windowing request to a set of segments axis-//
 * We take two list, one of horizontal-segment and one of vertical-segment
 *
 *
 */
public class WindowerEngine {
    private final PST pstH;
    private final PST pstV;

    private final java.util.Map<Segment, Segment> invertedToOrigin;

    public WindowerEngine(List<Segment> horizontals, List<Segment> verticals) {
        this.pstH = new PST();
        this.pstV = new PST();

        pstH.build(horizontals);
        pstV.build(invertSegments(verticals));
    }

    private List<Segment> invertSegments(List<Segment> segments) {
        List<Segment> inverted = new ArrayList<>();
        for (Segment s : segments) {
            Point stored = s.getStoredPoint();
            Point other = s.getOtherPoint();
            Point invertedStored = new Point(stored.getY(), stored.getX());
            Point invertedOther = new Point(other.getY(), other.getX());
            Segment invertedSegment = new HorizontalSegment(invertedStored, invertedOther);
            inverted.add(invertedSegment);
            invertedToOrigin.put(invertedSegment, s);
        }
        return inverted;
    }
}