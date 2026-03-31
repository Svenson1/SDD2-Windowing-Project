package be.ac.umons.backend.pst;

import be.ac.umons.backend.geometry.Segment;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Abstract Priority Search Tree.
 * Stores a list of segments and supports range queries of the form
 * (-inf : heapMax] x [sortMin : sortMax] in O(log n + k).
 * Subclasses define which coordinate plays the role of the heap axis
 * and which plays the role of the sort axis, allowing the same structure
 * to handle both horizontal and vertical segments without coordinate inversion.
 * Construction is done in O(n log n) via a single sort before recursion.
 * Each recursive call then costs O(n) only.
 *
 * <p>Convention : sortCoord <= yMid goes left, sortCoord > yMid goes right.</p>
 */
public abstract class PST {

    /**
     * The root node of the PST.
     */
    private PstNode root;

    /**
     * Returns the heap coordinate of the stored endpoint of a segment.
     * Used for the heap property (minimum at root).
     * x for horizontal segments, y for vertical segments.
     *
     * @param s the segment
     * @return heap coordinate of the stored endpoint
     */
    protected abstract double getHeapCoord(Segment s);

    /**
     * Returns the sort coordinate of the stored endpoint of a segment.
     * Used for sorting and splitting into subtrees.
     * y for horizontal segments, x for vertical segments.
     *
     * @param s the segment
     * @return sort coordinate of the stored endpoint
     */
    protected abstract double getSortCoord(Segment s);

    /**
     * Returns the heap coordinate of the other endpoint of a segment.
     * Used to verify the heapMin bound during queries.
     * x for horizontal segments, y for vertical segments.
     *
     * @param s the segment
     * @return heap coordinate of the other endpoint
     */
    protected abstract double getOtherHeapCoord(Segment s);

    /**
     * Builds the PST from a list of segments in O(n log n).
     * Sorts by sort coordinate first (composite numbers if equal),
     * then builds recursively. Each recursive call costs O(n).
     *
     * @param segments list of segments to store
     */
    public void build(List<Segment> segments) {
        if (segments == null || segments.isEmpty()) {
            root = null;
            return;
        }
        // single sort by sortCoord, composite numbers on heapCoord if equal
        // O(n log n), Java uses TimSort
        segments.sort(
                Comparator
                        .comparingDouble((Segment s) -> getSortCoord(s))
                        .thenComparingDouble(s -> getHeapCoord(s))
        );
        root = buildRecursive(segments);
    }

    /**
     * Recursive construction on a list already sorted by sort coordinate.
     * At each call : find heap minimum in O(n), read median in O(1),
     * split by index in O(1). Total cost : O(n log n).
     *
     * @param segments sorted list of segments
     * @return root node of the constructed subtree
     */
    private PstNode buildRecursive(List<Segment> segments) {
        if (segments.isEmpty()) return null;

        // 1. find heap minimum in O(n), composite numbers if equal
        Segment min = segments.get(0);
        for (Segment s : segments) {
            double heapSegment = getHeapCoord(s);
            double heapMin     = getHeapCoord(min);
            if (heapSegment < heapMin || (heapSegment == heapMin && getSortCoord(s) < getSortCoord(min))) {
                min = s;
            }
        }

        // 2. build remaining list
        List<Segment> remaining = new ArrayList<>(segments);
        remaining.remove(min);

        // 3. leaf case
        if (remaining.isEmpty()) {
            return new PstNode(min, getSortCoord(min));
        }

        // 4. median by index, O(1) because remaining is already sorted
        int mid    = (remaining.size() - 1) / 2;
        double yMid = getSortCoord(remaining.get(mid));

        // 5. split by index, no loop needed
        // subList(1,1) returns empty list, no index out of bounds risk
        List<Segment> below = new ArrayList<>(remaining.subList(0, mid + 1));
        List<Segment> above = new ArrayList<>(remaining.subList(mid + 1, remaining.size()));

        // 6. build node and recurse
        PstNode node = new PstNode(min, yMid);
        node.setLeft(buildRecursive(below));
        node.setRight(buildRecursive(above));

        return node;
    }

    /**
     * Queries the PST with a range (-inf : heapMax] x [sortMin : sortMax].
     * heapMin is verified via the other endpoint reference.
     * Returns all candidate segments in O(log n + k).
     *
     * For PstH : heapMin=xMin, heapMax=xMax, sortMin=yMin, sortMax=yMax.
     * For PstV : heapMin=yMin, heapMax=yMax, sortMin=xMin, sortMax=xMax.
     *
     * @param heapMin lower bound on the heap coordinate (checked via other endpoint)
     * @param heapMax upper bound on the heap coordinate
     * @param sortMin lower bound on the sort coordinate
     * @param sortMax upper bound on the sort coordinate
     * @return list of candidate segments
     */
    public List<Segment> query(double heapMin, double heapMax, double sortMin, double sortMax) {
        List<Segment> res = new ArrayList<>();
        queryPst(root, heapMin, heapMax, sortMin, sortMax, res);
        return res;
    }

    /**
     * Three phases :
     * 1. Descend to vSplit, the node where paths to sortMin and sortMax diverge.
     * 2. Descend toward sortMin in the left subtree of vSplit :
     *    when the path goes left, report the entire right subtree.
     * 3. Descend toward sortMax in the right subtree of vSplit :
     *    when the path goes right, report the entire left subtree.
     *
     * @param root    current node
     * @param heapMin lower bound on heap coordinate
     * @param heapMax upper bound on heap coordinate
     * @param sortMin lower bound on sort coordinate
     * @param sortMax upper bound on sort coordinate
     * @param res     list to add results to
     */
    private void queryPst(PstNode root, double heapMin, double heapMax, double sortMin, double sortMax, List<Segment> res) {
        if (root == null) return;

        // phase 1 : descend to vSplit
        PstNode vSplit;
        while (root != null) {
            testAndReport(root, heapMin, heapMax, sortMin, sortMax, res);
            if (sortMin <= root.getYMid() && sortMax < root.getYMid()) {
                root = root.getLeft();
            } else if (sortMin > root.getYMid() && sortMax >= root.getYMid()) {
                root = root.getRight();
            } else {
                break;
            }
        }

        vSplit = root;
        if (vSplit == null) return;

        // phase 2 : descend toward sortMin in left subtree
        PstNode v = vSplit.getLeft();
        while (v != null) {
            testAndReport(v, heapMin, heapMax, sortMin, sortMax, res);
            if (v.getYMid() < sortMin) {
                v = v.getRight();
            } else {
                // path goes left → right subtree entirely in sort range
                reportInSubTree(v.getRight(), heapMin, heapMax, res);
                v = v.getLeft();
            }
        }

        // phase 3 : descend toward sortMax in right subtree
        v = vSplit.getRight();
        while (v != null) {
            testAndReport(v, heapMin, heapMax, sortMin, sortMax, res);
            if (v.getYMid() <= sortMax) {
                // path goes right → left subtree entirely in sort range
                reportInSubTree(v.getLeft(), heapMin, heapMax, res);
                v = v.getRight();
            } else {
                v = v.getLeft();
            }
        }
    }

    /**
     * Reports all segments in the subtree whose heap coordinate is at most heapMax.
     * heapMin is verified via the other endpoint.
     *
     * @param root    root of the subtree
     * @param heapMin lower bound on heap coordinate (checked via other endpoint)
     * @param heapMax upper bound on heap coordinate
     * @param res     list to add results to
     */
    private void reportInSubTree(PstNode root, double heapMin, double heapMax, List<Segment> res) {
        if (root == null) return;
        Segment s = root.getSegment();
        if (getHeapCoord(s) <= heapMax) {
            if (getOtherHeapCoord(s) >= heapMin) { res.add(root.getSegment()); }
            reportInSubTree(root.getLeft(),  heapMin, heapMax, res);
            reportInSubTree(root.getRight(), heapMin, heapMax, res);
        }
    }

    /**
     * Tests whether the segment stored at a node lies in the query range
     * and reports it if so.
     *
     * @param root    node to test
     * @param heapMin lower bound on heap coordinate (checked via other endpoint)
     * @param heapMax upper bound on heap coordinate
     * @param sortMin lower bound on sort coordinate
     * @param sortMax upper bound on sort coordinate
     * @param res     list to add result to
     */
    private void testAndReport(PstNode root, double heapMin, double heapMax, double sortMin, double sortMax, List<Segment> res) {
        if (root == null) return;
        Segment s = root.getSegment();
        if (getHeapCoord(s) <= heapMax && getOtherHeapCoord(s) >= heapMin && getSortCoord(s) >= sortMin && getSortCoord(s) <= sortMax) {
            res.add(s);
        }
    }
}