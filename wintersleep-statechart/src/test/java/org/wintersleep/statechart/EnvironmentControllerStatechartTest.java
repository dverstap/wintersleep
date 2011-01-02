package org.wintersleep.statechart;

import org.junit.Before;
import org.junit.Test;
import org.wintersleep.graphviz.DiGraph;
import org.wintersleep.statechart.graphviz.GraphVizStatechartPlotter;
import org.wintersleep.test.util.FileTestUtil;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import static org.junit.Assert.assertEquals;
import static org.wintersleep.test.util.FileTestUtil.assertCreated;

public class EnvironmentControllerStatechartTest {

    private final File outputDir = FileTestUtil.makeOutputDir(ReferenceStatechartTest.class);

    private EnvironmentControllerStatechart sc;
    private EnvironmentController controller;

    private static final int TOO_COLD = 64;
    private static final int COMFORTABLE = 70;
    private static final int TOO_HOT = 76;

    @Before
    public void before() {
        sc = new EnvironmentControllerStatechart(EnvironmentController.class);
        controller = new EnvironmentController();
        sc.start(controller);
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
    public void whenComfortableOnTooColdStartHeating() {
        assertTick(TOO_COLD, "HcB");
    }

    @Test
    public void whenComfortableOnTooHotStartCooling() {
        assertTick(TOO_HOT, "hCB");
    }

    @Test
    public void whenTooHotOnComfortableStopHeating() {
        assertTick(TOO_COLD, "HcB");
        assertTick(COMFORTABLE, "hcB");
    }

    @Test
    public void whenHeaterTurnedOffBlowerKeepsRunningFiveMinutes() {
        assertTick(TOO_COLD, "HcB");
        assertTick(COMFORTABLE, "hcB");
        assertTicks(4, COMFORTABLE, "hcB");
        assertTick(COMFORTABLE, "hcb");
    }

    private void assertTicks(int nrOfTicks, int temperature, String expectedState) {
        for (int i = 0; i < nrOfTicks; i++) {
            assertTick(temperature, expectedState);
        }
    }


    private void assertTick(int temperature, String expectedState) {
        controller.tick(sc, temperature);
        assertEquals(expectedState, controller.getState());
    }
}
