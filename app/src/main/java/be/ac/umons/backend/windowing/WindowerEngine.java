package be.ac.umons.backend.windowing;

import be.ac.umons.backend.geometry.HorizontalSegment;
import be.ac.umons.backend.geometry.Point;
import be.ac.umons.backend.geometry.Segment;
import be.ac.umons.backend.pst.PST;
import be.ac.umons.backend.pst.PstH;
import be.ac.umons.backend.pst.PstV;

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

    public WindowerEngine(List<Segment> horizontals, List<Segment> verticals) {
        this.pstH = new PstH();
        this.pstV = new PstV();

        pstH.build(horizontals);
        pstV.build(verticals);
    }
}