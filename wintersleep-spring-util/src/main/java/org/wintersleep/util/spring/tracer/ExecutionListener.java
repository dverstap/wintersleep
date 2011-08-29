package org.wintersleep.util.spring.tracer;

import org.aspectj.lang.ProceedingJoinPoint;

public interface ExecutionListener {

    /**
     *
     * @param call The call that could be performance logged.
     * @return The Execution that will track the call. It may be null (for instance when tracing is disabled or
     * when it's not necessary to track any data about the call).
     */
    Execution start(ProceedingJoinPoint call);

    /**
     * Called back when a non-null Execution was returned by the start() method.
     *
     * @param execution The non-null Execution that was returned by the start() method.
     */
    void finished(Execution execution);

}
