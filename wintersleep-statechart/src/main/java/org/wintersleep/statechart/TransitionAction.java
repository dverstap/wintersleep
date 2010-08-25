package org.wintersleep.statechart;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class TransitionAction {

    private final String name;
    private final Method method;

    public TransitionAction(Class callbackClass, String name) {
        this.name = name;
        if (callbackClass == null) {
            method = null;
        } else {
            try {
                method = callbackClass.getMethod(name, Event.class);
            } catch (NoSuchMethodException e) {
                throw new IllegalArgumentException(e);
            }
        }
    }


    public void run(Object pojo, Event event) {
        try {
            method.invoke(pojo, event);
        } catch (IllegalAccessException e) {
            throw new IllegalStateException(e);
        } catch (InvocationTargetException e) {
            throw new IllegalStateException(e);
        }
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }
    
}
