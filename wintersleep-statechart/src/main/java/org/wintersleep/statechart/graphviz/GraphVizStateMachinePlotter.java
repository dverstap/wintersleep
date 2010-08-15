package org.wintersleep.statechart.graphviz;

import org.wintersleep.graphviz.DiGraph;
import org.wintersleep.graphviz.Node;
import org.wintersleep.statechart.CompositeState;
import org.wintersleep.statechart.State;
import org.wintersleep.statechart.StateMachine;

import java.util.HashMap;
import java.util.Map;

public class GraphVizStateMachinePlotter {

    private final Map<State, Node> stateToNodeMap = new HashMap<State, Node>();
    private final StateMachine stateMachine;


    public GraphVizStateMachinePlotter(StateMachine stateMachine) {
        this.stateMachine = stateMachine;
    }

    public DiGraph create() {
        DiGraph graph = new DiGraph(stateMachine.getName());
        addStateNodes(graph, stateMachine.getTop());
        return graph;
    }

    private void addStateNodes(DiGraph graph, State parent) {
        stateToNodeMap.put(parent, graph.addNode(parent.getName()));
        if (parent instanceof CompositeState) {
            CompositeState compositeState = (CompositeState) parent;
            for (State state : compositeState) {
                addStateNodes(graph, state);
            }
        }
    }
}
