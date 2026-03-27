package be.ac.umons.backend.pst;

import be.ac.umons.backend.geometry.Point;
import be.ac.umons.backend.geometry.Segment;

/**
 * Node of a PST
 * Store a point with is (px, py) coordinate ,
 * the separation value yMid for the subtree,
 * and a link to the original segment
 */
public class PstNode {
    /**
     * Point stocked in this node (the one with the minimum x)
     */
    private final Point point;
    /**
     * Separation value of the y coordinates
     */
    private final double yMid;
    /**
     * link to the original segment
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
     *
     * @param point the Point that is going to be stored in the node
     * @param yMid the separation value for the y coordinates
     * @param segment the original segment
     */
    public PstNode(Point point, double yMid, Segment segment) {
        this.point = point;
        this.yMid = yMid;
        this.segment = segment;
        this.left = null;
        this.right = null;
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
    public Point getPoint() {
        return point;
    }
    public Segment getSegment() {
        return segment;
    }
    public double getYMid() {
        return yMid;
    }


}
