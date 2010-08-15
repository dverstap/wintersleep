package org.wintersleep.statechart;

import org.junit.Test;
import org.wintersleep.graphviz.DiGraph;
import org.wintersleep.statechart.graphviz.GraphVizStatechartPlotter;
import org.wintersleep.test.util.FileTestUtil;

import java.io.File;
import java.io.IOException;

import static org.wintersleep.test.util.FileTestUtil.assertCreated;

public class ReferenceStatechartTest {

    private final File outputDir = FileTestUtil.makeOutputDir(ReferenceStatechartTest.class);

    @Test
    public void test() throws IOException {
        ReferenceStatechart sc = new ReferenceStatechart();
        GraphVizStatechartPlotter plotter = new GraphVizStatechartPlotter(sc);
        DiGraph diGraph = plotter.create();
        assertCreated(diGraph.makeImageFile(outputDir, "png", true));
    }

}

