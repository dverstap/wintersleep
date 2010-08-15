package org.wintersleep.statechart;

public abstract class EntryExitAction {

    private final String name;

    public EntryExitAction(String name) {
        this.name = name;
    }

    public void execute() {

    }

    public String getName() {
        return name;
    }

}
