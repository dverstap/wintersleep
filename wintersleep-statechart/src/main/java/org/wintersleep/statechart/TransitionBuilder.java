package org.wintersleep.statechart;

public class TransitionBuilder {

    private final State sourceState;
    private final Signal signal;
    private boolean positiveCondition = true;
    private String condition;
    private boolean internal = false;
    private State targetState;
    private String[] actions = {};


    public TransitionBuilder(State sourceState, Signal signal) {
        this.sourceState = sourceState;
        this.signal = signal;
    }


    public TransitionBuilder when(String condition) {
        this.positiveCondition = true;
        this.condition = condition;
        return this;
    }

    public TransitionBuilder whenNot(String condition) {
        this.positiveCondition = false;
        this.condition = condition;
        return this;
    }

    public TransitionBuilder transitionTo(State targetState) {
        this.targetState = targetState;
        return this;
    }

    public TransitionBuilder transitionInternally() {
        this.targetState = this.sourceState;
        this.internal = true;
        return this;
    }

    public TransitionBuilder execute(String... actions) {
        this.actions = actions;
        return this;
    }

    public void build() {
        if (targetState == null) {
            throw new IllegalStateException("targetState not defined");
        }
        Guard guard = null;
        if (condition != null) {
            guard = new Guard(sourceState.getStatechart().getCallbackClass(), positiveCondition, condition);
        }
        TransitionAction[] transitionActions = new TransitionAction[actions.length];
        for (int i = 0; i < actions.length; i++) {
            final String action = actions[i];
            transitionActions[i] = new TransitionAction(sourceState.getStatechart().getCallbackClass(), action);

        }
        if (internal) {
            sourceState.getStatechart().addInternalTransition(sourceState, signal, guard, transitionActions);
        } else {
            sourceState.getStatechart().addTransition(sourceState, targetState, signal, guard, transitionActions);
        }
    }
}
