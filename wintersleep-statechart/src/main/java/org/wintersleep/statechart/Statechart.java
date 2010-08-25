package org.wintersleep.statechart;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Statechart {

    private final static Logger log = LoggerFactory.getLogger(Statechart.class);

    private final String name;
    protected final CompositeState top;

    private final Queue<Event> waitingEvents = new LinkedList<Event>();

    private State currentState;
    private final List<Transition> transitions = new ArrayList<Transition>();

    public Statechart(String name) {
        this.name = name;
        top = new CompositeState(this, "TOP");
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
        Transition transition = new Transition(this, from, to, signal, guard, false, actions);
        transitions.add(transition);
        return transition;
    }

    public Transition addInternalTransition(State from, Signal signal, Guard guard, TransitionAction... actions) {
        Transition transition = new Transition(this, from, from, signal, guard, true, actions);
        transitions.add(transition);
        return transition;
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

    public Transition[] getTransitions() {
        return transitions.toArray(new Transition[transitions.size()]);
    }

    public void start() {
        if (currentState != null) {
            throw new IllegalStateException("statechart already started.");
        }
        currentState = top.getInitialState().executeInitialTransition();
    }


    public void processEvent(Event newEvent) {
        waitingEvents.add(newEvent);
        if (waitingEvents.size() == 1) {
            while (waitingEvents.size() > 0) {
                processNextEvent();
            }
        }
    }

    protected void processNextEvent() {
        assert (waitingEvents.size() > 0);

        Event event = waitingEvents.remove();

        log.debug("Received event '{}'", event.getSignal().getName());

        Transition transition
                = currentState.findOutgoingTransition(event);
        if (transition != null) {
            if (transition.isInternal()) {
                transition.executeActions(event);
            } else {
                log.debug("Found transition via '{}' to '{}'.",
                        transition.getStartState().getName(),
                        transition.getTargetState().getName());

                State origState = currentState;
                exitToState(transition.getStartState(), origState);
                exitToState(transition.getLCAState(), origState);

                transition.executeActions(event);

                enterToState(transition.getTargetState(),
                        transition.getTargetToLCAPath());
                assert (currentState == transition.getTargetState());

                if (currentState instanceof CompositeState) {
                    CompositeState compositeState = (CompositeState) currentState;
                    currentState = compositeState.getInitialState().executeInitialTransition();
                }

                // TODO deep history
//                if (currentState instanceof PseudoState) {
//                    PseudoState pseudoState = (PseudoState) currentState;
//                    if (pseudoState.getType() == PseudoState.DEEPHISTORY) {
//                        currentState = pseudoState.executeDeepHistoryTransition();
//                    }
//                }
                log.debug("Transition executed: now in state '{}'.", currentState.getName());

                //savePersistency();
            }
        } else {
            log.debug("No outgoing transition found. Ignoring event.");
        }
    }

    void exitToState(State target, State origState) {
        while (currentState != target) {
            // TODO deep history currentState.executeExitActions(origState);
            currentState.executeExitActions();
            currentState = currentState.getParent();
        }
    }

    void enterToState(State target, List<State> targetToAncestorPath) {
        if (currentState != target) {
            for (int i = targetToAncestorPath.size(); i != 0; i--) {
                currentState = targetToAncestorPath.get(i - 1);
                currentState.executeEntryActions();
            }
            assert (currentState == target.getParent());
            currentState = target;
            currentState.executeEntryActions();
        }
    }

}
