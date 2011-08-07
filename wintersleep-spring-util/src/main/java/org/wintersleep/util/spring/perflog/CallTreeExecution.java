package org.wintersleep.util.spring.perflog;

import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;


public class CallTreeExecution implements Execution {

    private final CallTreeExecution parent;
    private final String name;
    private final long startTime;

    private List<CallTreeExecution> children;
    private long stopTime;

    public CallTreeExecution(CallTreeExecution parent, String name, long startTime) {
        this.parent = parent;
        this.name = name;
        this.startTime = startTime;

        if (parent != null) {
            parent.addChild(this);
        }
    }

    private void addChild(CallTreeExecution child) {
        if (children == null) {
            // instantiate child lists on-demand, to avoid wasting memory for the many leaf calls
            children = new ArrayList<CallTreeExecution>();
        }
        children.add(child);
    }

    public boolean isRoot() {
        return parent == null;
    }

    @Override
    public void finished() {
        this.stopTime = System.currentTimeMillis();
    }

    public long getDuration() {
        return stopTime - startTime;
    }

    public void log(Logger logger) {
        StringBuilder sb = new StringBuilder();
        format(sb, "");
        logger.info(sb.toString());
    }

    protected void format(StringBuilder sb, String prefix) {
        sb.append(prefix).append(name).append(": ").append(getDuration()).append("\n");
        if (children != null) {
            for (CallTreeExecution child : children) {
                child.format(sb, prefix + "  ");
            }
        }
    }


}
