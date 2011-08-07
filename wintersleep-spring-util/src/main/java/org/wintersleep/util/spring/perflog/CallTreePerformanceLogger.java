package org.wintersleep.util.spring.perflog;

import org.aspectj.lang.ProceedingJoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class CallTreePerformanceLogger implements PerformanceLogger {

    private final ThreadLocal<CallTreeExecution> current = new ThreadLocal<CallTreeExecution>();

    private final Logger logger;

    public CallTreePerformanceLogger() {
        this(LoggerFactory.getLogger("PERF_LOG"));
    }

    public CallTreePerformanceLogger(Logger logger) {
        this.logger = logger;
    }

    public void start(String message) {
        CallTreeExecution execution = current.get();
        if (execution != null) {
            throw new IllegalStateException("Already started");
        }
        if (isEnabled()) {
            current.set(new CallTreeExecution(execution, message, System.currentTimeMillis()));
        }
    }

    public void stop() {
        CallTreeExecution execution = current.get();
        if (execution == null) {
            return; // simply not started
        }
        if (!execution.isRoot()) {
            throw new IllegalStateException("Trying to stop before reaching in the root of the call tree.");
        }
        execution.finished();
        execution.log(logger);
        current.remove();
    }

    @Override
    public Execution start(ProceedingJoinPoint call) {
        CallTreeExecution parent = current.get();
        if (parent != null) {
            String msg = buildMessage(call);
            return new CallTreeExecution(parent, msg, System.currentTimeMillis());
        } else {
            return null;
        }
    }

    protected String buildMessage(ProceedingJoinPoint call) {
        StringBuilder sb = new StringBuilder(call.getSignature().toString());
        sb.append(" (").append(call.getTarget().getClass().getName()).append(")");
        return sb.toString();
    }

    protected boolean isEnabled() {
        return logger.isInfoEnabled();
    }
}
