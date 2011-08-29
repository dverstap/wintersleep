package org.wintersleep.util.spring.tracer;

import org.aspectj.lang.ProceedingJoinPoint;

public class ExecutionTracerAspect {

    private final ExecutionListener executionListener;

    public ExecutionTracerAspect(ExecutionListener executionListener) {
        this.executionListener = executionListener;
    }

    public Object profile(ProceedingJoinPoint call) throws Throwable {
        Execution execution = executionListener.start(call);
        try {
            return call.proceed();
        } finally {
            if (execution != null) {
                executionListener.finished(execution);
            }
        }
    }


}
