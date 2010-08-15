package org.wintersleep.statechart;

import java.util.ArrayList;
import java.util.List;

public class Transition {
    private final StateMachine stateMachine;
    private final State startState;
    private final State targetState;
    private final List<State> targetToLCAPath = new ArrayList<State>();
    private final State lcaState;
    private final Signal triggerSignal;
    private final Guard guard;
    private final TransitionAction[] transitionActions;
    private final boolean isInternal;


    public Transition(StateMachine stateMachine, State startState, State targetState, Signal triggerSignal, Guard guard,
                      boolean internal, TransitionAction... transitionActions) {
        this.stateMachine = stateMachine;
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

    public StateMachine getStateMachine() {
        return stateMachine;
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


}
