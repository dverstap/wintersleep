package org.wintersleep.statechart;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CompositeState extends State implements Iterable<State> {

    private final List<State> children = new ArrayList<State>();
    private final PseudoState initialState;
    private PseudoState deepHistoryState;

    protected CompositeState(CompositeState parent, String name, EntryAction[] entryActions, ExitAction[] exitActions) {
        super(parent, name, entryActions, exitActions);
        initialState = new PseudoState(this, PseudoState.Type.INITIAL);
    }

    protected CompositeState(CompositeState parent, String name, EntryAction... entryActions) {
        this(parent, name, entryActions, ExitAction.NO_EXIT_ACTIONS);
    }

    protected CompositeState(CompositeState parent, String name, ExitAction... exitActions) {
        this(parent, name, EntryAction.NO_ENTRY_ACTIONS, exitActions);
    }

    protected CompositeState(CompositeState parent, String name) {
        this(parent, name, EntryAction.NO_ENTRY_ACTIONS, ExitAction.NO_EXIT_ACTIONS);
    }

    protected CompositeState(Statechart statechart, String name, EntryAction[] entryActions, ExitAction[] exitActions) {
        super(statechart, name, entryActions, exitActions);
        initialState = new PseudoState(this, PseudoState.Type.INITIAL);
    }

    protected CompositeState(Statechart statechart, String name, EntryAction... entryActions) {
        this(statechart, name, entryActions, ExitAction.NO_EXIT_ACTIONS);
    }

    protected CompositeState(Statechart statechart, String name, ExitAction... exitActions) {
        this(statechart, name, EntryAction.NO_ENTRY_ACTIONS, exitActions);
    }

    protected CompositeState(Statechart statechart, String name) {
        this(statechart, name, EntryAction.NO_ENTRY_ACTIONS, ExitAction.NO_EXIT_ACTIONS);
    }

    public PseudoState getInitialState() {
        return initialState;
    }

    protected void addChild(State child) {
        children.add(child);
//        if (child instanceof PseudoState) {
//            PseudoState pseudoState = (PseudoState) child;
//            if (pseudoState.getType() == PseudoState.Type.INITIAL) {
//                assert(this.initialState == null);
//                initialState = pseudoState;
//            }
//        }
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

    public TransitionBuilder onInit() {
        return new TransitionBuilder(this.getInitialState(), Signal.INIT);
    }

    public void print(PrintWriter w, String indent) {
        w.println(indent + getName() + ":");
        for (State child : children) {
            child.print(w, indent + "  ");
        }
    }

}
