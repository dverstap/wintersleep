package org.wintersleep.statechart.graphviz;

import org.wintersleep.graphviz.DiGraph;
import org.wintersleep.graphviz.Node;
import org.wintersleep.statechart.CompositeState;
import org.wintersleep.statechart.State;
import org.wintersleep.statechart.Statechart;

import java.util.HashMap;
import java.util.Map;

public class GraphVizStatechartPlotter {

    private final Map<State, Node> stateToNodeMap = new HashMap<State, Node>();
    private final Statechart statechart;


    public GraphVizStatechartPlotter(Statechart statechart) {
        this.statechart = statechart;
    }

    public DiGraph create() {
        DiGraph graph = new DiGraph(statechart.getName());
        addStateNodes(graph, statechart.getTop());
        return graph;
    }

    private void addStateNodes(DiGraph graph, State parent) {
        if (parent instanceof CompositeState) {
            stateToNodeMap.put(parent, graph.addNode(parent.getName()));
            CompositeState compositeState = (CompositeState) parent;
            for (State state : compositeState) {
                addStateNodes(graph, state);
            }
        } else {
            stateToNodeMap.put(parent, graph.addNode(parent.getName()));
        }
    }
}
