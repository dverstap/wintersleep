package org.wintersleep.util.spring.perflog;

import org.aspectj.lang.ProceedingJoinPoint;

public class PerformanceLoggingAspect {

    private final PerformanceLogger performanceLogger;

    public PerformanceLoggingAspect(PerformanceLogger performanceLogger) {
        this.performanceLogger = performanceLogger;
    }

    public Object profile(ProceedingJoinPoint call) throws Throwable {
        Execution execution = performanceLogger.start(call);
        try {
            return call.proceed();
        } finally {
            if (execution != null) {
                execution.finished();
            }
        }
    }


}
