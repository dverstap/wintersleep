package org.wintersleep.statechart;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public abstract class State {

    private final Statechart statechart;
    private final State parent;
    private final String name;
    private final List<EntryExitAction> entryActions = new ArrayList<EntryExitAction>();
    private final List<EntryExitAction> exitActions = new ArrayList<EntryExitAction>();
    private final List<Transition> outgoingTransitions = new ArrayList<Transition>();

    protected State(State parent, String name, EntryAction[] entryActions, ExitAction[] exitActions) {
        this.statechart = parent.statechart;
        this.parent = parent;
        this.name = name;
        CompositeState compositeState = (CompositeState) parent;
        compositeState.addChild(this);
    }

    protected State(Statechart statechart, String name, EntryAction[] entryActions, ExitAction[] exitActions) {
        this.statechart = statechart;
        this.parent = null;
        this.name = name;
    }

    public Statechart getStateMachine() {
        return statechart;
    }

    public State getParent() {
        return parent;
    }

    public String getName() {
        return name;
    }

    public boolean isTopState() {
        return parent == null;
    }

    public void addOutgoingTransition(Transition transition) {
        outgoingTransitions.add(transition);
    }

    public Transition findOutgoingTransition(Event event) {
        for (Transition transition : outgoingTransitions) {
            if (transition.getTriggerSignal().equals(event.getSignal())) {
                if (transition.passesGuard(event)) {
                    return transition;
                }
            }
        }
        if (parent != null) {
            return parent.findOutgoingTransition(event);
        }
        return null;
    }

    public State findState(String name) {
        if (this.name.equals(name)) {
            return this;
        }
        return null;
    }

    public void addEntryAction(EntryExitAction action) {
        entryActions.add(action);
    }

    public void addExitAction(EntryExitAction action) {
        exitActions.add(action);
    }

    public void executeEntryActions() {
        for (EntryExitAction entryAction : entryActions) {
            entryAction.execute();
        }
    }

    public void executeExitActions() {
        for (EntryExitAction exitAction : exitActions) {
            exitAction.execute();
        }
    }

    public static State findLCA(State source, State target, List<State> targetToLCAPath) {
        assert (source.parent != null);
        assert (target.parent != null);

        State lca = null;
        if (source == target) {
            lca = source.parent;
        } else if (source == target.parent) {
            lca = source;
        } else if (source.parent == target.parent) {
            lca = source.parent;
        } else if (source.parent == target) {
            lca = target;
        } else {
            for (State s = target.parent; lca == null && s != null; s = s.parent) {
                if (source == s) {
                    lca = s;
                } else {
                    targetToLCAPath.add(s);
                }
            }
            // TODO:
//            if (lca == null) {
//                for (int i = targetToLCAPath.size();
//                     lca == null && i != 0; i--) {
//                    State s = targetToLCAPath.get(i - 1);
//                    if (s == source.parent) {
//                        lca = s;
//                        targetToLCAPath.resize(i - 1, null);
//                    }
//                }
//            }
//            if (lca == null) {
//                for (State s1 = source.parent;
//                     lca == null && s1 != null; s1 = s1.parent) {
//                    for (int i = targetToLCAPath.size();
//                         lca == null && i != 0; i--) {
//                        State s2 = targetToLCAPath.get(i - 1);
//                        if (s1 == s2) {
//                            lca = s1;
//                            targetToLCAPath.resize(i - 1, null);
//                        }
//                    }
//                }
//            }

        }
        assert (lca != null);
        return lca;
    }

    public List<State> findPathFromParent(State parent) {
        List<State> result = new ArrayList<State>();
        State state = this;
        while (state.parent != parent) {
            assert(state.parent != null);
            result.add(state);
            state = state.parent;
        }
        return result;
    }

    public TransitionBuilder on(Signal signal) {
        return new TransitionBuilder(this, signal);
    }

    public void print(PrintWriter w, String indent) {
        w.println(indent + getName());
    }

}
