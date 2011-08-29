package org.wintersleep.util.spring.tracer;

public class SystemCurrentTimeMillisProvider implements CurrentTimeProvider {

    @Override
    public long get() {
        return System.currentTimeMillis();
    }

}
