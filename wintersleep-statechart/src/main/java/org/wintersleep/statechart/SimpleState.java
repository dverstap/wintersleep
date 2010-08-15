package org.wintersleep.statechart;

public class SimpleState extends State {


    public SimpleState(CompositeState parent, String name, EntryAction[] entryActions, ExitAction[] exitActions) {
        super(parent, name, entryActions, exitActions);
    }

    protected SimpleState(CompositeState parent, String name, EntryAction... entryActions) {
        super(parent, name, entryActions, ExitAction.NO_EXIT_ACTIONS);
    }

    protected SimpleState(CompositeState parent, String name, ExitAction... exitActions) {
        super(parent, name, EntryAction.NO_ENTRY_ACTIONS, exitActions);
    }

    protected SimpleState(CompositeState parent, String name) {
        super(parent, name, EntryAction.NO_ENTRY_ACTIONS, ExitAction.NO_EXIT_ACTIONS);
    }

}
