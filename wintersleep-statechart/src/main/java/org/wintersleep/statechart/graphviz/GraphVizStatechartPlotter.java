package org.wintersleep.statechart.graphviz;

import org.wintersleep.graphviz.DiGraph;
import org.wintersleep.graphviz.Edge;
import org.wintersleep.graphviz.Node;
import org.wintersleep.statechart.CompositeState;
import org.wintersleep.statechart.PseudoState;
import org.wintersleep.statechart.Signal;
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
        // TODO should use the visitor pattern here or something
        Node node = null;
        if (state instanceof CompositeState) {
            CompositeState compositeState = (CompositeState) state;
            DiGraph subGraph = graph.addSubGraph(state.getName());
            //subGraph.getAttributeList().setLabel(state.getName());
            if (compositeState.isTopState()) {
                subGraph.getAttributeList().setStyle("invis");
            } else {
                subGraph.getAttributeList().setStyle("rounded");                
                node = subGraph.addNode(compositeState.getName());
                        node.addAttributeList()
                        .setShape("box")
                        .setStyle("rounded,dotted")
                                .setLabel(compositeState.getGraphVizLabel());
            }
            compositeStateToGraphMap.put(compositeState, subGraph);
            for (State childState : compositeState) {
                addStateNodes(subGraph, childState);
            }
        } else if (state instanceof SimpleState) {
            node = graph.addNode(state.getName());
            node.addAttributeList().setShape("box")
                    .setStyle("rounded")
                    .setLabel(state.getGraphVizLabel());
            stateToNodeMap.put(state, node);
        } else if (state instanceof PseudoState) {
            PseudoState pseudoState = (PseudoState) state;
            node = graph.addNode(pseudoState.getName());
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
            Edge edge = graph.addEdge(transition.getStartState().getGraphVizNodeId(),
                    transition.getTargetState().getGraphVizNodeId());
            if (!transition.getTriggerSignal().equals(Signal.INIT)) {
                edge.addAttributeList().setLabel(transition.getLabel());
            }
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
