package org.wintersleep.statechart;

// Figure 4.3 from "Practical Statecharts in C/C++"
class ReferenceStateMachine extends StateMachine {

    private final CompositeState s0 = new CompositeState(top, "S0", entryActions("enterS0"), exitActions("exitS0"));

    private static final Signal A = new Signal("A");


    public ReferenceStateMachine() {
        super(ReferenceStateMachine.class.getSimpleName());
    }


    public static void main(String[] args) {
        ReferenceStateMachine machine = new ReferenceStateMachine();
        System.out.println(machine);
    }
}
