package org.wintersleep.statechart;

public class EntryAction extends EntryExitAction {

    public static final EntryAction[] NO_ENTRY_ACTIONS = new EntryAction[0];

    public EntryAction(String name) {
        super(name);
    }

}
