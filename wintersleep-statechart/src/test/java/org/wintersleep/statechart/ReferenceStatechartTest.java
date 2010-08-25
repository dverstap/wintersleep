package org.wintersleep.statechart;

import org.junit.Before;
import org.junit.Test;
import org.wintersleep.graphviz.DiGraph;
import org.wintersleep.statechart.graphviz.GraphVizStatechartPlotter;
import org.wintersleep.test.util.FileTestUtil;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import static org.junit.Assert.*;
import static org.wintersleep.test.util.FileTestUtil.assertCreated;

public class ReferenceStatechartTest {

    private final File outputDir = FileTestUtil.makeOutputDir(ReferenceStatechartTest.class);

    private ReferenceStatechart sc;

    @Before
    public void before() {
        sc = new ReferenceStatechart();
    }

    @Test
    public void testIsChildOf() {
        assertFalse(sc.getTop().isChildOf(sc.getTop()));
        assertTrue(sc.s1.isChildOf(sc.getTop()));
        assertTrue(sc.s211.isChildOf(sc.getTop()));
        assertFalse(sc.getTop().isChildOf(sc.s1));
    }

    @Test
    public void testTransitionPaths() {
        for (Transition transition : sc.getTransitions()) {
            for (State state : transition.getTargetToLCAPath()) {
                assertNotNull(transition.toString(), state);
            }
        }
    }


    @Test
    public void testGraphViz() throws IOException {
        PrintWriter w = new PrintWriter(System.out);
        sc.getTop().print(w, "");
        w.flush();
        
        GraphVizStatechartPlotter plotter = new GraphVizStatechartPlotter(sc);
        DiGraph diGraph = plotter.create();
        assertCreated(diGraph.makeImageFile(outputDir, "png", true));
    }

    @Test
    public void testStart() {
        sc.start();
        assertEquals(sc.s11, sc.getCurrentState());
    }

    @Test
    public void testTransitions() {
        sc.start();
        assertEquals(sc.s11, sc.getCurrentState());

        sc.processEvent(new Event(ReferenceStatechart.A));
        // TODO assert actions
        assertEquals(sc.s11, sc.getCurrentState());

        sc.processEvent(new Event(ReferenceStatechart.B));
        assertEquals(sc.s11, sc.getCurrentState());

        sc.processEvent(new Event(ReferenceStatechart.C));
        assertEquals(sc.s211, sc.getCurrentState());

        sc.processEvent(new Event(ReferenceStatechart.D));
        assertEquals(sc.s211, sc.getCurrentState());

        sc.processEvent(new Event(ReferenceStatechart.E));
        assertEquals(sc.s211, sc.getCurrentState());

        sc.processEvent(new Event(ReferenceStatechart.F));
        assertEquals(sc.s11, sc.getCurrentState());

        sc.processEvent(new Event(ReferenceStatechart.G));
        assertEquals(sc.s211, sc.getCurrentState());

        sc.processEvent(new Event(ReferenceStatechart.H));
        assertEquals(sc.s211, sc.getCurrentState());

    }

}

