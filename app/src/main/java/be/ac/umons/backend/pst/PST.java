package be.ac.umons.backend.pst;

import be.ac.umons.backend.geometry.Point;
import be.ac.umons.backend.geometry.Segment;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Priority search tree,
 * Take a List of segment and Store them by taking the first point in consideration for the PST,
 * Segment need do be of the form ((x,y),(x',y)) because we only find horizontal segment in the
 * query.
 * If you want to find vertical segment, you need to invert your x and y before creating the PST
 * and do not forget to reverse the window for the query
 * Can take query like (-inf, qx] x [qy, qy'] and it will return all the segment
 * that lie in this window.
 * The construction of the pst is done in O(n log(n))
 *
 * A pst is referenced by is root node
 */
public class PST {
    /**
     * The rootNode of the PST
     */
    private PstNode root;


    public void build(List<Segment> segments) {
        if (segments == null || segments.isEmpty()) {
            root = null;
            return;
        }
        //tri des points en fonction de y et en fonction de x si les points sont egaux (methode composite numbers)
        // ce tri est en O(nlogn), java utilise TimSort
        segments.sort(
                Comparator
                        .comparingDouble((Segment s) -> s.getStoredPoint().getY())
                        .thenComparingDouble(s -> s.getStoredPoint().getX())
        );
        root = buildRecursive(segments);
    }

    /**
     * recursive methode to create the PST from a list of segment
     * that are sorted by the y coordinate of the point that is to stored
     * @param segments sorted list of segments
     * @return the root note corresponding at the point with the minimum x coordinate
     */
    private PstNode buildRecursive(List<Segment> segments) {
        if (segments.isEmpty()) return null;

        // 1. trouver xMin en O(n)
        Segment xMin = segments.get(0);
        // comparaison avec methode composite number pour trouvre le plus petit x
        for (Segment s : segments) {
            if (s.getStoredPoint().getX() < xMin.getStoredPoint().getX() || (s.getStoredPoint().getX() == xMin.getStoredPoint().getX() && s.getStoredPoint().getY() < xMin.getStoredPoint().getY())) {
                xMin = s;
            }
        }

        // 2. créer remaining
        List<Segment> remaining = new ArrayList<>(segments);
        remaining.remove(xMin);

        // 3. cas d'une feuille
        if (remaining.isEmpty()) {
            return new PstNode(xMin, xMin.getStoredPoint().getY());
        }

        // 4. médiane (liste déjà triée par Y)
        int mid = (remaining.size() - 1) / 2;
        double yMid = remaining.get(mid).getStoredPoint().getY();

        // 5. split, en se servant de l'index car les segments sont trié en fonction des y
        List<Segment> below = new ArrayList<>(remaining.subList(0, mid + 1));
        List<Segment> above = new ArrayList<>(remaining.subList(mid + 1, remaining.size()));
        //--> pas de risque d'index out of band car subList(1,1) renvoie une liste vide
        // 6. construire le noeud
        PstNode node = new PstNode(xMin, yMid);

        node.setLeft(buildRecursive(below));
        node.setRight(buildRecursive(above));

        return node;
    }

    public List<Segment> query(double xMin, double xMax, double yMin, double yMax) {
        List<Segment> res = new ArrayList<>();
        queryPst(root, xMin, xMax, yMin, yMax, res);
        return res;
    }

    private void queryPst(PstNode root, double xMin, double xMax, double yMin, double yMax, List<Segment> res) {
        if (root == null) return;

        // phase 1 descendre jusqu'a vSplit ==> noeud auquel yMin et yMax se séparent
        PstNode vSplit;
        while (root != null) {
            testAndReport(root, xMin, xMax, yMin, yMax, res);
            if ( yMin <= root.getYMid() && yMax <= root.getYMid()) {
                root = root.getLeft();
            } else if (yMin > root.getYMid() && yMax > root.getYMid()) {
                root = root.getRight();
            }else {
                break;
            }
        }

        vSplit = root;
        if (vSplit == null) return;

        //phase 2 : descendre vers yMin :
        PstNode v = vSplit.getLeft();

        while (v != null) {
            testAndReport(v, xMin, xMax, yMin, yMax, res);
            if (v.getYMid() < yMin) {
                v = v.getRight();
            }else if (v.getYMid() >= yMin) {
                reportInSubTree(v.getRight(), xMin, xMax, res);
                v = v.getLeft();
            }
        }

        //phase 3 : descendre vers yMax:
        v = vSplit.getRight();
        while (v != null) {
            testAndReport(v, xMin, xMax, yMin, yMax, res);
            if(v.getYMid() < yMax) {
                reportInSubTree(v.getLeft(), xMin, xMax, res);
                v = v.getRight();
            }
            else if (v.getYMid() >= yMax) {
                v = v.getLeft();
            }
        }
    }

    private void reportInSubTree(PstNode root, double xMin, double xMax, List<Segment> res) {
        if (root == null) return;
        Point p = root.getPoint();
        Point other = root.getSegment().getOtherPoint();
        if (p.getX() <= xMax){
            if (other.getX() >= xMin) {res.add(root.getSegment());}
            reportInSubTree(root.getLeft(), xMin, xMax, res);
            reportInSubTree(root.getRight(), xMin, xMax, res);
        }
    }

    private void testAndReport(PstNode root, double xMin, double xMax, double yMin, double yMax, List<Segment> res) {
        if (root == null) return;
        Point p = root.getPoint();
        Point other = root.getSegment().getOtherPoint();
        if (p.getX() <= xMax && other.getX() >= xMin && p.getY() <= yMax && p.getY() >= yMin) {
            res.add(root.getSegment());
        }
    }

}
