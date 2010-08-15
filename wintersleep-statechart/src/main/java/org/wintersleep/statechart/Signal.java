package org.wintersleep.statechart;

public class Signal implements Comparable<Signal> {

    public static final Signal INIT = new Signal("INIT");
    public static final Signal FINIT = new Signal("FINIT");

    private final String name;

    public Signal(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Signal signal = (Signal) o;

        if (name != null ? !name.equals(signal.name) : signal.name != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Signal{" +
                "name='" + name + '\'' +
                '}';
    }

    @Override
    public int compareTo(Signal o) {
        return this.name.compareTo(o.name);
    }
    
}
