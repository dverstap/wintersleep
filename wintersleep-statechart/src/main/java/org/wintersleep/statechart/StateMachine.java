package org.wintersleep.statechart;

import java.util.LinkedList;
import java.util.Queue;

public class StateMachine {

    private final String name;
    protected final CompositeState top;

    //private final List<Transition> transitions = new ArrayList<Transition>();
    private final Queue<Event> waitingEvents = new LinkedList<Event>();

    private State currentState;

    public StateMachine(String name) {
        this.name = name;
        top = new CompositeState(this, "TOP");
        new PseudoState(top, PseudoState.Type.INITIAL);
    }

    public String getName() {
        return name;
    }

    public CompositeState getTop() {
        return top;
    }

    public State getCurrentState() {
        return currentState;
    }

    public Transition addTransition(State from, State to, Signal signal, Guard guard, TransitionAction... actions) {
        return new Transition(this, from, to, signal, guard, false, actions);
    }


    public EntryAction[] entryActions(String... names) {
        EntryAction[] result = new EntryAction[names.length];
        for (int i = 0; i < names.length; i++) {
            result[i] = new EntryAction(names[i]);
        }
        return result;
    }

    public ExitAction[] exitActions(String... names) {
        ExitAction[] result = new ExitAction[names.length];
        for (int i = 0; i < names.length; i++) {
            result[i] = new ExitAction(names[i]);
        }
        return result;

    }

}
