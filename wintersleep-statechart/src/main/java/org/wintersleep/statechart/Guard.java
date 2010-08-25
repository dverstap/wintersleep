package org.wintersleep.statechart;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Guard {

    private static final Logger log = LoggerFactory.getLogger(Guard.class);

    private final boolean positive;
    private final String name;
    private final Method method;

    public Guard(Class callbackClass, boolean positive, String name) {
        this.positive = positive;
        this.name = name;
        if (callbackClass == null) {
            method = null;
        } else {
            try {
                // TODO support more flexibility with respect to method parameters ...
                method = callbackClass.getMethod(name, Event.class);
            } catch (NoSuchMethodException e) {
                throw new IllegalArgumentException(e);
            }
        }
    }

    public String getName() {
        return name;
    }

    public boolean passes(Object pojo, Event event) {
        try {
            boolean result = (Boolean) method.invoke(pojo, event);
            if (!positive) {
                result = !result;
            }
            log.debug("Guard {} returns: {}", toString(), result);
            return result;
        } catch (IllegalAccessException e) {
            throw new IllegalStateException(e);
        } catch (InvocationTargetException e) {
            throw new IllegalStateException(e);
        }
    }

    public String getLabel() {
        if (positive) {
            return "[" + name + "]/";
        } else {
            return "[!" + name + "]/";
        }
    }

    @Override
    public String toString() {
        if (positive) {
            return name;
        } else {
            return "!" + name;
        }

    }

}
