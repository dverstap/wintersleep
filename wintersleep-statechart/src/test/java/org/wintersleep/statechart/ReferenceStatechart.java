package org.wintersleep.statechart;

// Figure 4.3 from "Practical Statecharts in C/C++"
class ReferenceStatechart extends Statechart {

    final CompositeState s0 = new CompositeState(top, "S0", entryActions("enterS0"), exitActions("exitS0"));
    final CompositeState s1 = new CompositeState(s0, "S1", entryActions("enterS1"), exitActions("exitS1"));
    final SimpleState s11 = new SimpleState(s1, "S11", entryActions("enterS11"), exitActions("exitS11"));
    final CompositeState s2 = new CompositeState(s0, "S2", entryActions("enterS2"), exitActions("exitS2"));
    final CompositeState s21 = new CompositeState(s2, "S21", entryActions("enterS21"), exitActions("exitS21"));
    final SimpleState s211 = new SimpleState(s21, "S211", entryActions("enterS211"), exitActions("exitS211"));

    static final Signal A = new Signal("A");
    static final Signal B = new Signal("B");
    static final Signal C = new Signal("C");
    static final Signal D = new Signal("D");
    static final Signal E = new Signal("E");
    static final Signal F = new Signal("F");
    static final Signal G = new Signal("G");
    static final Signal H = new Signal("H");


    public ReferenceStatechart() {
        super(ReferenceStatechartPojo.class, ReferenceStatechart.class.getSimpleName());

        // TODO the .build() at the end of every TransitionBuilder is very annoying and error prone.
        // if we just store the TransitionBuilder in the Statechart, we could build() them all at once

        top.onInit().transitionTo(s0).build();

        s0.onInit().transitionTo(s1).build();
        s0.on(E).transitionTo(s211).build();

        s1.onInit().transitionTo(s11).build();
        s1.on(A).transitionTo(s1).build();
        s1.on(B).transitionTo(s11).build();
        s1.on(C).transitionTo(s2).build();
        s1.on(D).transitionTo(s0).build();
        s1.on(F).transitionTo(s211).build();

        s11.on(G).transitionTo(s211).build();
        s11.on(H).when("isFoo").transitionInternally().execute("setFooFalse").build();

        s2.onInit().transitionTo(s21).build();
        s2.on(C).transitionTo(s1).build();
        s2.on(F).transitionTo(s11).build();
        s2.on(H).transitionInternally().whenNot("isFoo").execute("setFooTrue").build();

        s21.onInit().transitionTo(s211).build();
        s21.on(B).transitionTo(s211).build();

        s211.on(D).transitionTo(s21).build();
        s211.on(G).transitionTo(s0).build();
    }


    public static void main(String[] args) {
        ReferenceStatechart machine = new ReferenceStatechart();
        System.out.println(machine);
    }
}
