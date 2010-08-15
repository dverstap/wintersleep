package org.wintersleep.statechart;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CompositeState extends State implements Iterable<State> {

    private final List<State> children = new ArrayList<State>();
    private PseudoState initialState;
    private PseudoState deepHistoryState;

    protected CompositeState(CompositeState parent, String name, EntryAction[] entryActions, ExitAction[] exitActions) {
        super(parent, name, entryActions, exitActions);
    }

    protected CompositeState(CompositeState parent, String name, EntryAction... entryActions) {
        super(parent, name, entryActions, ExitAction.NO_EXIT_ACTIONS);
    }

    protected CompositeState(CompositeState parent, String name, ExitAction... exitActions) {
        super(parent, name, EntryAction.NO_ENTRY_ACTIONS, exitActions);
    }

    protected CompositeState(CompositeState parent, String name) {
        super(parent, name, EntryAction.NO_ENTRY_ACTIONS, ExitAction.NO_EXIT_ACTIONS);
    }

    protected CompositeState(Statechart statechart, String name, EntryAction[] entryActions, ExitAction[] exitActions) {
        super(statechart, name, entryActions, exitActions);
    }

    protected CompositeState(Statechart statechart, String name, EntryAction... entryActions) {
        super(statechart, name, entryActions, ExitAction.NO_EXIT_ACTIONS);
    }

    protected CompositeState(Statechart statechart, String name, ExitAction... exitActions) {
        super(statechart, name, EntryAction.NO_ENTRY_ACTIONS, exitActions);
    }

    protected CompositeState(Statechart statechart, String name) {
        super(statechart, name, EntryAction.NO_ENTRY_ACTIONS, ExitAction.NO_EXIT_ACTIONS);
    }

    protected void addChild(State child) {
        children.add(child);
        if (child instanceof PseudoState) {
            PseudoState pseudoState = (PseudoState) child;
            if (pseudoState.getType() == PseudoState.Type.INITIAL) {
                assert(this.initialState == null);
                initialState = pseudoState;
            }
        }
    }

    @Override
    public State findState(String name) {
        State result = super.findState(name);
        if (result != null) {
            return result;
        }
        for (State child : children) {
            result = child.findState(name);
            if (result != null) {
                return result;
            }
        }
        return null;
    }



    @Override
    public void executeExitActions() {
        // TODO
//        if (deepHistoryState != null) {
//        }
        super.executeExitActions();
    }

    public State executeInitialTransition() {
        return initialState.executeInitialTransition();
    }

    public static CompositeState dynamicCast(State state) {
        if (state instanceof CompositeState) {
            return (CompositeState) state;
        }
        return null;
    }

    @Override
    public Iterator<State> iterator() {
        return children.iterator();
    }
}
