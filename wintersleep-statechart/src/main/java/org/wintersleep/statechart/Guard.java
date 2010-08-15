package org.wintersleep.statechart;

public class Guard {

    private final GuardCallback callback;
    private final boolean isPositive;
    private final String desc;

    public Guard(GuardCallback callback, boolean positive, String desc) {
        this.callback = callback;
        isPositive = positive;
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }

    public boolean passes(Event event) {
        boolean result = callback.test(event);
        if (!isPositive) {
            result = !result;
        }
        return result;
    }
}
