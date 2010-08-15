package org.wintersleep.statechart;

public interface TransitionAction {

    void run(Event event);

    String getName();
    
}
