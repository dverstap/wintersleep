package org.wintersleep.statechart;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public abstract class State {

    private static final Logger log = LoggerFactory.getLogger(State.class);

    private final Statechart statechart;
    private final State parent;
    private final String name;
    private final EntryAction[] entryActions;
    private final ExitAction[] exitActions;
    private final List<Transition> outgoingTransitions = new ArrayList<Transition>();

    protected State(State parent, String name, EntryAction[] entryActions, ExitAction[] exitActions) {
        this.statechart = parent.statechart;
        this.parent = parent;
        this.name = name;
        this.entryActions = entryActions;
        this.exitActions = exitActions;

        CompositeState compositeState = (CompositeState) parent;
        compositeState.addChild(this);
    }

    protected State(Statechart statechart, String name, EntryAction[] entryActions, ExitAction[] exitActions) {
        this.statechart = statechart;
        this.parent = null;
        this.name = name;
        this.entryActions = entryActions;
        this.exitActions = exitActions;
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

    public void executeEntryActions() {
        if (entryActions.length > 0) {
            String id = toString();
            for (EntryExitAction entryAction : entryActions) {
                log.debug("{}: executing entry action: {}", id, entryAction.getName());
                entryAction.execute();
            }
        }
    }

    public void executeExitActions() {
        if (exitActions.length > 0) {
            String id = toString();
            for (EntryExitAction exitAction : exitActions) {
                log.debug("{}: executing exit action: {}", id, exitAction.getName());
                exitAction.execute();
            }
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
            if (lca == null) {
                for (int i = targetToLCAPath.size(); lca == null && i != 0; i--) {
                    State s = targetToLCAPath.get(i - 1);
                    if (s == source.parent) {
                        lca = s;
                        resize(targetToLCAPath, i - 1, null);
                    }
                }
            }
            if (lca == null) {
                for (State s1 = source.parent;
                     lca == null && s1 != null; s1 = s1.parent) {
                    for (int i = targetToLCAPath.size();
                         lca == null && i != 0; i--) {
                        State s2 = targetToLCAPath.get(i - 1);
                        if (s1 == s2) {
                            lca = s1;
                            resize(targetToLCAPath, i - 1, null);
                        }
                    }
                }
            }

        }
        assert (lca != null);
        return lca;
    }

    public List<State> findPathFromParent(State parent) {
        List<State> result = new ArrayList<State>();
        State state = this;
        while (state.parent != parent) {
            assert (state.parent != null);
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

    // is this state a (recursive) child of the presumed parent?
    public boolean isChildOf(State presumedParent) {
        State p = parent;
        while (p != null) {
            if (p == presumedParent) {
                return true;
            }
            p = p.parent;
        }
        return false;
    }

    public String getGraphVizNodeId() {
        return getName();
    }

    public String getGraphVizLabel() {
        StringBuilder result = new StringBuilder(getName());
        for (EntryExitAction entryAction : entryActions) {
            result.append("\\nentry/").append(entryAction.getName());
        }
        for (ExitAction exitAction : exitActions) {
            result.append("\\nexit/").append(exitAction.getName());
        }
        return result.toString();
    }

    private static <T> void resize(final List<T> list, final int newSize, final T fillValue) {
        if (list.size() < newSize) {
            throw new IllegalStateException("Does not make sense to make the LCA path longer. ");
//            do {
//                list.add(fillValue);
//            } while (list.size() < newSize);
        } else if (newSize < list.size()) {
            list.subList(newSize, list.size()).clear();
        }
    }

    @Override
    public String toString() {
        return statechart.getName() + ":" + name;
    }
}
