package org.wintersleep.util.spring.perflog;

import org.aspectj.lang.ProceedingJoinPoint;

public interface PerformanceLogger {

    /**
     *
     * @param call The call that could be performance logged.
     * @return The Execution that will track and time the call, may be null when performance logging is disabled for the target.
     */
    Execution start(ProceedingJoinPoint call);


}
