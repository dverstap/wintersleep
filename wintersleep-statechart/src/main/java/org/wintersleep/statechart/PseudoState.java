package org.wintersleep.statechart;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PseudoState extends State {

    private static final Logger log = LoggerFactory.getLogger(PseudoState.class);

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

    public State executeInitialTransition(Object pojo) {
        Event event = new Event(Signal.INIT);

        Transition transition = findOutgoingTransition(pojo, event);
        assert (transition != null);
        log.debug("Executing initial transaction from '{}' to '{}'.", transition.getStartState(), transition.getTargetState().getName());

        transition.executeActions(pojo, event);

        State targetState = transition.getTargetState();
        targetState.executeEntryActions(pojo);
        CompositeState compositeState = CompositeState.dynamicCast(targetState);
        while (compositeState != null) {
            targetState = compositeState.executeInitialTransition(pojo);
            compositeState = CompositeState.dynamicCast(targetState);
        }

        assert (targetState != null);
        return targetState;
    }

}
