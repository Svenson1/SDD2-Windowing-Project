package be.ac.umons.backend.pst;

import be.ac.umons.backend.geometry.Point;
import be.ac.umons.backend.geometry.Segment;

/**
 * Represents a node in a Priority Search Tree (PST).
 * Each node holds a segment and a y separation value used to partition the subtree.
 */
public class PstNode {

    /**
     * The y coordinate used to split the subtree into left and right children.
     */
    private final double yMid;

    /**
     * The segment stored in this node.
     */
    private final Segment segment;

    /**
     * The left child of this node.
     */
    private PstNode left;

    /**
     * The right child of this node.
     */
    private PstNode right;

    /**
     * Creates a PST node with the given segment and y separation value.
     *
     * @param segment the segment to store in this node
     * @param yMid    the y coordinate used to partition the subtree
     */
    public PstNode(Segment segment, double yMid) {
        this.segment = segment;
        this.yMid = yMid;
    }

    /**
     * Sets the left child of this node.
     *
     * @param left the left child node
     */
    public void setLeft(PstNode left) {
        this.left = left;
    }

    /**
     * Sets the right child of this node.
     *
     * @param right the right child node
     */
    public void setRight(PstNode right) {
        this.right = right;
    }

    /**
     * Returns the left child of this node.
     *
     * @return the left child node, or null if none
     */
    public PstNode getLeft() {
        return left;
    }

    /**
     * Returns the right child of this node.
     *
     * @return the right child node, or null if none
     */
    public PstNode getRight() {
        return right;
    }

    /**
     * Returns the stored point of the segment held by this node.
     *
     * @return the stored endpoint of the segment
     */
    public Point getPoint() {
        return segment.getStoredPoint();
    }

    /**
     * Returns the segment stored in this node.
     *
     * @return the segment
     */
    public Segment getSegment() {
        return segment;
    }

    /**
     * Returns the y separation value used to partition the subtree.
     *
     * @return the y separation value
     */
    public double getYMid() {
        return yMid;
    }

    /**
     * Returns a string representation of this node in the format: Node{point=..., yMid=...}
     *
     * @return a string representation of this node
     */
    @Override
    public String toString() {
        return "Node{" +
                "point=" + getPoint() +
                ", yMid=" + yMid +
                '}';
    }
}