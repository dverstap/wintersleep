package org.wintersleep.statechart;

import java.util.ArrayList;
import java.util.List;

public class Transition {
    private final Statechart statechart;
    private final State startState;
    private final State targetState;
    private final List<State> targetToLCAPath = new ArrayList<State>();
    private final State lcaState;
    private final Signal triggerSignal;
    private final Guard guard;
    private final TransitionAction[] transitionActions;
    private final boolean isInternal;


    public Transition(Statechart statechart, State startState, State targetState, Signal triggerSignal, Guard guard,
                      boolean internal, TransitionAction... transitionActions) {
        this.statechart = statechart;
        this.startState = startState;
        this.targetState = targetState;
        this.lcaState = State.findLCA(startState, targetState, targetToLCAPath);
        this.triggerSignal = triggerSignal;
        this.guard = guard;
        this.transitionActions = transitionActions;
        isInternal = internal;

        startState.addOutgoingTransition(this);
        if (isInternal) {
            assert (startState == targetState);
        }
    }

    public Statechart getStateMachine() {
        return statechart;
    }

    public State getStartState() {
        return startState;
    }

    public State getTargetState() {
        return targetState;
    }

    public State getLcaState() {
        return lcaState;
    }

    public Signal getTriggerSignal() {
        return triggerSignal;
    }

    public boolean isInternal() {
        return isInternal;
    }

    public boolean passesGuard(Event event) {
        if (guard == null) {
            return true;
        }
        return guard.passes(event);
    }

    public void executeActions(Event event) {
        for (TransitionAction action : transitionActions) {
            action.run(event);
        }
    }

    public String getGraphVizStartStateId() {
        if (startState instanceof CompositeState) {
            CompositeState compositeStartState = (CompositeState) startState;
            if (targetState.isChildOf(startState)) {
                return compositeStartState.getGraphVizExternalNodeId();
//            } else if (startState.isChildOf(targetState)) {
//                return compositeStartState.getGraphVizInternalNodeId();
//            }
            } else {
                return compositeStartState.getInitialState().getName();
            }
        }
        return startState.getName();
    }

    public String getGraphVizTargetStateId() {
        if (targetState instanceof CompositeState) {
            CompositeState compositeTargetState = (CompositeState) targetState;
            if (startState.isChildOf(targetState)) {
                return compositeTargetState.getGraphVizExternalNodeId();
//            } else if (targetState.isChildOf(startState)) {
//                return compositeTargetState.getGraphVizInternalNodeId();
//            }
            } else {
                return compositeTargetState.getInitialState().getName();
            }
        }
        return targetState.getName();
    }

}
