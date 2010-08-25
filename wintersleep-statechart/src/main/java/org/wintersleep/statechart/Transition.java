package org.wintersleep.statechart;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class Transition {

    private static final Logger log = LoggerFactory.getLogger(Transition.class);

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

    public List<State> getTargetToLCAPath() {
        return targetToLCAPath;
    }

    public State getLCAState() {
        return lcaState;
    }

    public Signal getTriggerSignal() {
        return triggerSignal;
    }

    public boolean isInternal() {
        return isInternal;
    }

    public boolean passesGuard(Event event) {
        return guard == null || guard.passes(event);
    }

    public void executeActions(Event event) {
        if (transitionActions.length > 0) {
            String id = toString();
            for (TransitionAction action : transitionActions) {
                log.debug("{}: executing action: {}", id, action.getName());
                action.run(event);
            }
        }
    }

    public String getLabel() {
        StringBuilder result = new StringBuilder(triggerSignal.getName());
        if (guard != null) {
            result.append(guard.getLabel());
        }
        for (TransitionAction transitionAction : transitionActions) {
            result.append("\\n").append(transitionAction.getName());
        }
        return result.toString();
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder(statechart.getName());
        builder.append(":").append(startState.getName()).append("->").append(targetState.getName());
        builder.append(":").append(triggerSignal.getName());
        if (guard != null) {
            builder.append("[").append(guard.getName()).append("]");
        }
        return builder.toString();
    }
}
