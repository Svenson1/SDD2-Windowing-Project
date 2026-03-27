package be.ac.umons.backend.pst;

import be.ac.umons.backend.geometry.HorizontalSegment;
import be.ac.umons.backend.geometry.Point;
import be.ac.umons.backend.geometry.Segment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PSTTest {

    private PST pst;
    private List<Segment> segments;

    @BeforeEach
    void setUp() {
        pst = new PST();
        segments = new ArrayList<>();

        // Exemple simple : 5 segments horizontaux
        segments.add(new HorizontalSegment(new Point(1, 5), new Point(4, 5)));
        segments.add(new HorizontalSegment(new Point(2, 3), new Point(5, 3)));
        segments.add(new HorizontalSegment(new Point(0, 1), new Point(3, 1)));
        segments.add(new HorizontalSegment(new Point(3, 4), new Point(6, 4)));
        segments.add(new HorizontalSegment(new Point(2, 2), new Point(5, 2)));
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

        assertEquals(xMin, root.getPoint().getX(), "Le point de la racine doit avoir le x minimum");
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

    /**
     * Petit utilitaire pour récupérer la racine via réflexion si nécessaire
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