package org.wintersleep.statechart;

public class TransitionBuilder {

    private final State sourceState;
    private final Signal signal;
    private String condition;
    private State targetState;
    private String[] actions = {};


    public TransitionBuilder(State sourceState, Signal signal) {
        this.sourceState = sourceState;
        this.signal = signal;
    }


    public TransitionBuilder when(String condition) {
        this.condition = condition;
        return this;
    }

    public TransitionBuilder transitionTo(State targetState) {
        this.targetState = targetState;
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
            guard = new Guard(null, true, condition);
        }
        TransitionAction[] transitionActions = new TransitionAction[actions.length];
        for (int i = 0; i < actions.length; i++) {
            final String action = actions[i];
            transitionActions[i] = new TransitionAction() {
                @Override
                public void run(Event event) {

                }

                @Override
                public String getName() {
                    return action;
                }
            };

        }
        sourceState.getStateMachine().addTransition(sourceState, targetState, signal, guard, transitionActions);
    }
}
