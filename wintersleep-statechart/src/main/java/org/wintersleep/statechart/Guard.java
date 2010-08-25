package org.wintersleep.statechart;

public class Guard {

    private final GuardCallback callback;
    private final boolean positive;
    private final String name;

    public Guard(GuardCallback callback, boolean positive, String name) {
        this.callback = callback;
        this.positive = positive;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public boolean passes(Event event) {
        // TODO
//        boolean result = callback.test(event);
//        if (!positive) {
//            result = !result;
//        }
//        return result;
        return true;
    }

    public String getLabel() {
        if (positive) {
            return "[" + name + "]/";
        } else {
            return "[!" + name + "]/";
        }
    }
}
