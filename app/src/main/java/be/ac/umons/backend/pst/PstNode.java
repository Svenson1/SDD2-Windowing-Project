package be.ac.umons.backend.pst;

import be.ac.umons.backend.geometry.Point;
import be.ac.umons.backend.geometry.Segment;

/**
 * Node of a PST
 * Store a segment and the separation value yMid for the subtree
 */
public class PstNode {

    /**
     * Separation value of the y coordinates
     */
    private final double yMid;

    /**
     * Segment stored in this node
     */
    private final Segment segment;

    /**
     * left subtree
     */
    private PstNode left;

    /**
     * right subtree
     */
    private PstNode right;

    /**
     * COnstructor
     * @param segment segment to store
     * @param yMid
     */
    public PstNode(Segment segment, double yMid) {
        this.segment = segment;
        this.yMid = yMid;
    }

    public void setLeft(PstNode left) {
        this.left = left;
    }

    public void setRight(PstNode right) {
        this.right = right;
    }

    public PstNode getLeft() {
        return left;
    }

    public PstNode getRight() {
        return right;
    }

    /**
     * Return the stored point (derived from the segment)
     */
    public Point getPoint() {
        return segment.getStoredPoint();
    }

    public Segment getSegment() {
        return segment;
    }

    public double getYMid() {
        return yMid;
    }

    @Override
    public String toString() {
        return "Node{" +
                "point=" + getPoint() +
                ", yMid=" + yMid +
                '}';
    }
}