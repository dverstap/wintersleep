package org.wintersleep.statechart;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public abstract class EntryExitAction {

    private final String name;
    private final Method method;

    public EntryExitAction(Class clazz, String name) {
        this.name = name;
        if (clazz == null) {
            method = null;
        } else {
            try {
                method = clazz.getMethod(name);
            } catch (NoSuchMethodException e) {
                throw new IllegalArgumentException(e);
            }
        }
    }

    public void execute(Object pojo) {
        if (method != null) {
            try {
                method.invoke(pojo);
            } catch (IllegalAccessException e) {
                throw new IllegalStateException(e);
            } catch (InvocationTargetException e) {
                throw new IllegalStateException(e);
            }
        }
    }

    public String getName() {
        return name;
    }

}
