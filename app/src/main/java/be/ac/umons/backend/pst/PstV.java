// backend/pst/PstV.java
package be.ac.umons.backend.pst;

import be.ac.umons.backend.geometry.Segment;

/**
 * Priority Search Tree for vertical segments.
 * Heap axis = y, sort axis = x.
 * No coordinate inversion needed — the abstract methods handle the permutation.
 */
public class PstV extends PST {

    @Override
    protected double getHeapCoord(Segment s) {
        return s.getStoredPoint().getY();
    }

    @Override
    protected double getSortCoord(Segment s) {
        return s.getStoredPoint().getX();
    }

    @Override
    protected double getOtherHeapCoord(Segment s) {
        return s.getOtherPoint().getY();
    }
}