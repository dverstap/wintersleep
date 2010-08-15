package org.wintersleep.statechart;

public class Event {
    private final Signal signal;

    public Event(Signal signal) {
        this.signal = signal;
    }

    public Signal getSignal() {
        return signal;
    }

    
}
