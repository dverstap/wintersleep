package org.wintersleep.statechart.graphviz;

import org.wintersleep.graphviz.DiGraph;
import org.wintersleep.graphviz.Edge;
import org.wintersleep.graphviz.Node;
import org.wintersleep.statechart.CompositeState;
import org.wintersleep.statechart.PseudoState;
import org.wintersleep.statechart.SimpleState;
import org.wintersleep.statechart.State;
import org.wintersleep.statechart.Statechart;
import org.wintersleep.statechart.Transition;

import java.util.LinkedHashMap;
import java.util.Map;

public class GraphVizStatechartPlotter {

    private final Map<CompositeState, DiGraph> compositeStateToGraphMap = new LinkedHashMap<CompositeState, DiGraph>();
    private final Map<State, Node> stateToNodeMap = new LinkedHashMap<State, Node>();
    private final Statechart statechart;


    public GraphVizStatechartPlotter(Statechart statechart) {
        this.statechart = statechart;
    }

    public DiGraph create() {
        DiGraph graph = new DiGraph(statechart.getName());
        graph.getAttributeList().setCompound(true);
        addStateNodes(graph, statechart.getTop());
        addTransitions(graph);
        return graph;
    }


    private void addStateNodes(DiGraph graph, State state) {
        // TODO should use the visitor pattern here
        if (state instanceof CompositeState) {
            CompositeState compositeState = (CompositeState) state;
            DiGraph subGraph = graph.addSubGraph(state.getName());
            subGraph.getAttributeList().setLabel(state.getName());
            compositeStateToGraphMap.put(compositeState, subGraph);
            for (State childState : compositeState) {
                addStateNodes(subGraph, childState);
            }
        } else if (state instanceof SimpleState) {
            Node node = graph.addNode(state.getName());
            node.addAttributeList().setShape("box")
                    .setStyle("rounded");
            stateToNodeMap.put(state, node);
        } else if (state instanceof PseudoState) {
            PseudoState pseudoState = (PseudoState) state;
            Node node = graph.addNode(pseudoState.getName());
            if (pseudoState.getType() == PseudoState.Type.INITIAL) {
                node.addAttributeList().setShape("point")
                        .setWidth(0.15)
                        .setHeight(0.15);
            }
            stateToNodeMap.put(state, node);
        } else {
            throw new IllegalArgumentException();
        }
    }

    private void addTransitions(DiGraph graph) {
        for (Transition transition : statechart.getTransitions()) {
            Edge edge = graph.addEdge(findTransitionStateName(transition.getStartState()),
                    findTransitionStateName(transition.getTargetState()));
            if (transition.getStartState() instanceof CompositeState) {
                CompositeState compositeStartState = (CompositeState) transition.getStartState();
                edge.addAttributeList().setLTail(compositeStateToGraphMap.get(compositeStartState));
            }
            if (transition.getTargetState() instanceof CompositeState) {
                CompositeState compositeTargetState = (CompositeState) transition.getTargetState();
                edge.addAttributeList().setLHead(compositeStateToGraphMap.get(compositeTargetState));
            }
        }
    }

    private String findTransitionStateName(State state) {
        String stateName = state.getName();
        if (state instanceof CompositeState) {
            CompositeState compositeState = (CompositeState) state;
            stateName = compositeState.getInitialState().getName();
        }
        return stateName;
    }

}
