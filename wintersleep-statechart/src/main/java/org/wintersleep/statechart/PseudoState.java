package org.wintersleep.statechart;

public class PseudoState extends State {

    public enum Type {
        INITIAL,
        FINAL
        //DEEP_HISTORY
    }

    private final Type type;

    public PseudoState(CompositeState parent, Type type) {
        super(parent, parent.getName() + "_" + type.name(), EntryAction.NO_ENTRY_ACTIONS, ExitAction.NO_EXIT_ACTIONS);
        this.type = type;
    }

    public Type getType() {
        return type;
    }

    public State executeInitialTransition() {
        Event event = new Event(Signal.INIT);

        Transition transition = findOutgoingTransition(event);
        assert (transition != null);

        transition.executeActions(event);

        State targetState = transition.getTargetState();
        targetState.executeEntryActions();
        CompositeState compositeState = CompositeState.dynamicCast(targetState);
        while (compositeState != null) {
            targetState = compositeState.executeInitialTransition();
            compositeState = CompositeState.dynamicCast(targetState);
        }

        assert (targetState != null);
        return targetState;
    }

}
