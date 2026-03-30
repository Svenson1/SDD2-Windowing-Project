// backend/pst/PstH.java
package be.ac.umons.backend.pst;

import be.ac.umons.backend.geometry.Segment;

/**
 * Priority Search Tree for horizontal segments.
 * Heap axis = x, sort axis = y.
 */
public class PstH extends PST {

    @Override
    protected double getHeapCoord(Segment s) {
        return s.getStoredPoint().getX();
    }

    @Override
    protected double getSortCoord(Segment s) {
        return s.getStoredPoint().getY();
    }

    @Override
    protected double getOtherHeapCoord(Segment s) {
        return s.getOtherPoint().getX();
    }
}