package be.ac.umons.backend.pst;

import be.ac.umons.backend.geometry.HorizontalSegment;
import be.ac.umons.backend.geometry.Point;
import be.ac.umons.backend.geometry.Segment;
import be.ac.umons.backend.geometry.VerticalSegment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PSTTest {

    private PST pst;
    private PST pstV;
    private List<Segment> segments;
    private List<Segment> verticalSegments;

    @BeforeEach
    void setUp() {
        pst = new PstH();
        segments = new ArrayList<>();

        pstV = new PstV();
        verticalSegments = new ArrayList<>();

        // Exemple simple : 5 segments horizontaux
        segments.add(new HorizontalSegment(new Point(1, 5), new Point(4, 5)));
        segments.add(new HorizontalSegment(new Point(2, 3), new Point(5, 3)));
        segments.add(new HorizontalSegment(new Point(0, 1), new Point(3, 1)));
        segments.add(new HorizontalSegment(new Point(3, 4), new Point(6, 4)));
        segments.add(new HorizontalSegment(new Point(2, 2), new Point(5, 2)));

        //5segments Verticaux
        verticalSegments.add(new VerticalSegment(new Point(2, 1), new Point(2, 5)));
        verticalSegments.add(new VerticalSegment(new Point(4, 3), new Point(4, 6)));
        verticalSegments.add(new VerticalSegment(new Point(1, 0), new Point(1, 4)));
        verticalSegments.add(new VerticalSegment(new Point(3, 2), new Point(3, 7)));

    }

    @Test
    void testBuildNotEmpty() {
        pst.build(segments);
        assertNotNull(pst, "Le PST ne doit pas être nul après build");
    }

    @Test
    void testRootPointIsXMin() {
        pst.build(segments);
        PstNode root = getRoot(pst);
        assertNotNull(root, "La racine ne doit pas être nulle");

        // Vérifie que le point stocké est bien celui avec le x minimum
        double xMin = segments.stream()
                .mapToDouble(s -> s.getStoredPoint().getX())
                .min()
                .orElseThrow();

        assertEquals(xMin, root.getPoint().getX());
    }

    @Test
    void testPstVRootIsYMin() {
        pstV.build(verticalSegments);

        PstNode root = getRoot(pstV);
        assertNotNull(root);

        double yMin = verticalSegments.stream()
                .mapToDouble(s -> s.getStoredPoint().getY())
                .min()
                .orElseThrow();

        assertEquals(yMin, root.getPoint().getY());
    }

    @Test
    void testLeafNode() {
        pst.build(segments);

        // Descendre jusqu'à une feuille pour vérifier qu'elle n'a pas de sous-arbre
        PstNode node = getRoot(pst);
        while (node.getLeft() != null) {
            node = node.getLeft();
        }
        assertNull(node.getLeft(), "Feuille doit avoir left null");
        assertNull(node.getRight(), "Feuille doit avoir right null");
    }

    @Test
    void testQueryEmptyTree() {
        pst.build(new ArrayList<>());
        List<Segment> res= pst.query(0, 10, 0, 10);
        assertTrue(res.isEmpty());
    }

    @Test
    void testQueryNoMatch() {
        pst.build(segments);
        List<Segment> res= pst.query(10, 20, 10, 20);
        assertTrue(res.isEmpty());
    }

    @Test
    void testQueryFllInside() {
        pst.build(segments);
        List<Segment> res= pst.query(0, 10, 0, 10);
        assertEquals(res.size(), segments.size());
    }

    @Test
    void testQueryPartialInside() {
        pst.build(segments);
        List<Segment> res= pst.query(3, 4, 0, 10);
        assertFalse(res.isEmpty());

        for(Segment seg : res) {
            assertTrue(
                    seg.getOtherPoint().getX() >= 3 &&
                            seg.getStoredPoint().getX() <= 4 &&
                            seg.getStoredPoint().getY() <= 10 &&
                            seg.getStoredPoint().getY() >= 0
                    );
        }
    }

    @Test
    void testQueryCrossing(){
        pst.build(segments);

        List<Segment> res= pst.query(2.5, 3.5, 0, 10);
        for(Segment seg : res) {
            assertTrue(
                    seg.getOtherPoint().getX() >= 2.5 &&
                            seg.getStoredPoint().getX() <= 3.5 &&
                            seg.getStoredPoint().getY() <= 10 &&
                            seg.getStoredPoint().getY() >= 0
            );
        }
    }

    @Test
    void testQueryInf(){
        pst.build(segments);

        List<Segment> res= pst.query(Double.NEGATIVE_INFINITY, 3, 0, 10);
        for(Segment seg : res) {
            assertTrue(
                            seg.getStoredPoint().getX() <= 3 &&
                            seg.getStoredPoint().getY() <= 10 &&
                            seg.getStoredPoint().getY() >= 0
            );
            assertEquals(5, res.size());
        }
    }

    @Test
    void testQueryPlusInf(){
        pst.build(segments);

        List<Segment> res= pst.query(5, Double.POSITIVE_INFINITY, 0, 10);
        for(Segment seg : res) {
            assertTrue(
                    seg.getOtherPoint().getX() >= 5 &&
                            seg.getStoredPoint().getY() <= 10 &&
                            seg.getStoredPoint().getY() >= 0
            );
            assertEquals(3, res.size());
        }
    }

    @Test
    void testPstVQueryBasic() {
        pstV.build(verticalSegments);

        // heap = y, sort = x
        List<Segment> res = pstV.query(1, 5, 1, 3);

        assertFalse(res.isEmpty());

        for (Segment s : res) {
            assertTrue(
                    s.getStoredPoint().getY() <= 5 &&
                            s.getOtherPoint().getY() >= 1 &&
                            s.getStoredPoint().getX() >= 1 &&
                            s.getStoredPoint().getX() <= 3
            );
        }
    }

    @Test
    void testPstVCrossing() {
        pstV.build(verticalSegments);

        List<Segment> res = pstV.query(2, 4, 1, 3);
        for (Segment s : res) {
            assertTrue(
                    s.getStoredPoint().getY() <= 4 &&
                            s.getOtherPoint().getY() >= 2 &&
                            s.getStoredPoint().getX() >= 1 &&
                            s.getStoredPoint().getX() <= 3
            );
        }

    }


    /**
     * Petit utilitaire pour récupérer la racine
     */
    private PstNode getRoot(PST pst) {
        try {
            var field = PST.class.getDeclaredField("root");
            field.setAccessible(true);
            return (PstNode) field.get(pst);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            fail("Impossible d'accéder à la racine du PST");
            return null;
        }
    }
}