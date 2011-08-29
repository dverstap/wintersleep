package org.wintersleep.util.spring.tracer;

import org.aspectj.lang.ProceedingJoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class CallTreeExecutionListener implements ExecutionListener {

    private final ThreadLocal<CallTreeExecution> current = new ThreadLocal<CallTreeExecution>();

    private final CurrentTimeProvider currentTimeProvider;
    private final Logger logger;

    public CallTreeExecutionListener() {
        this(new SystemCurrentTimeMillisProvider(), LoggerFactory.getLogger("PERF_LOG"));
    }

    public CallTreeExecutionListener(CurrentTimeProvider currentTimeProvider, Logger logger) {
        this.currentTimeProvider = currentTimeProvider;
        this.logger = logger;
    }

    public void start(String message) {
        CallTreeExecution execution = current.get();
        if (execution != null) {
            throw new IllegalStateException("Already started");
        }
        if (isEnabled()) {
            current.set(new CallTreeExecution(execution, message, currentTimeProvider.get()));
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
        execution.finished(currentTimeProvider.get());
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

    @Override
    public void finished(Execution execution) {
        CallTreeExecution callTreeExecution = (CallTreeExecution) execution;
        callTreeExecution.finished(currentTimeProvider.get());
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
