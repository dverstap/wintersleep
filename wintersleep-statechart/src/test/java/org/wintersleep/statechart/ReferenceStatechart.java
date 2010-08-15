package org.wintersleep.statechart;

// Figure 4.3 from "Practical Statecharts in C/C++"
class ReferenceStatechart extends Statechart {

    private final CompositeState s0 = new CompositeState(top, "S0", entryActions("enterS0"), exitActions("exitS0"));
    private final CompositeState s1 = new CompositeState(s0, "S1", entryActions("enterS1"), exitActions("exitS1"));
    private final SimpleState s11 = new SimpleState(s1, "S11", entryActions("enterS11"), exitActions("exitS11"));
    private final CompositeState s2 = new CompositeState(s0, "S2", entryActions("enterS2"), exitActions("exitS2"));
    private final CompositeState s21 = new CompositeState(s2, "S21", entryActions("enterS21"), exitActions("exitS21"));
    private final SimpleState s211 = new SimpleState(s21, "S211", entryActions("enterS211"), exitActions("exitS211"));

    private static final Signal A = new Signal("A");
    private static final Signal B = new Signal("B");
    private static final Signal C = new Signal("C");
    private static final Signal D = new Signal("D");
    private static final Signal E = new Signal("E");
    private static final Signal F = new Signal("F");
    private static final Signal G = new Signal("G");


    public ReferenceStatechart() {
        super(ReferenceStatechart.class.getSimpleName());
        top.onInit().transitionTo(s0).build();

        s0.onInit().transitionTo(s1).build();
        s0.on(E).transitionTo(s211).build();

        s1.onInit().transitionTo(s11).build();

        s2.onInit().transitionTo(s21).build();

        s21.onInit().transitionTo(s211).build();
        
    }


    public static void main(String[] args) {
        ReferenceStatechart machine = new ReferenceStatechart();
        System.out.println(machine);
    }
}
