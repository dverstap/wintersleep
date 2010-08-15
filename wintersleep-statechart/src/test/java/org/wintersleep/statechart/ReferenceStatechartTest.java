package org.wintersleep.statechart;

import org.junit.Test;
import org.wintersleep.graphviz.DiGraph;
import org.wintersleep.statechart.graphviz.GraphVizStatechartPlotter;
import org.wintersleep.test.util.FileTestUtil;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.wintersleep.test.util.FileTestUtil.assertCreated;

public class ReferenceStatechartTest {

    private final File outputDir = FileTestUtil.makeOutputDir(ReferenceStatechartTest.class);

    private final ReferenceStatechart sc = new ReferenceStatechart();

    @Test
    public void testIsChildOf() {
        assertFalse(sc.getTop().isChildOf(sc.getTop()));
        assertTrue(sc.s1.isChildOf(sc.getTop()));
        assertTrue(sc.s211.isChildOf(sc.getTop()));
        assertFalse(sc.getTop().isChildOf(sc.s1));
    }

    @Test
    public void test() throws IOException {
        PrintWriter w = new PrintWriter(System.out);
        sc.getTop().print(w, "");
        w.flush();
        
        GraphVizStatechartPlotter plotter = new GraphVizStatechartPlotter(sc);
        DiGraph diGraph = plotter.create();
        assertCreated(diGraph.makeImageFile(outputDir, "png", true));
    }

}

