package org.wintersleep.statechart;

public interface GuardCallback {

    boolean test(Event event);

}
