package org.wintersleep.statechart;

public class EnvironmentControllerStatechart extends Statechart {

    public static final Signal TOO_COLD = new Signal("TOO_COLD");
    public static final Signal COMFORTABLE = new Signal("COMFORTABLE");
    public static final Signal TOO_HOT = new Signal("TOO_HOT");

    private final State idle = new SimpleState(top, "IDLE");
    private final State heating = new SimpleState(top, "HEATING", entryActions("startHeater", "startBlower"), exitActions("stopHeater"));
    private final State postHeating = new SimpleState(top, "POST_HEATING", exitActions("stopBlower"));
    private final State cooling = new SimpleState(top, "COOLING", entryActions("startCooler", "startBlower"), exitActions("stopCooler", "stopBlower"));
    private final State postCooling = new SimpleState(top, "POST_COOLING");


    public EnvironmentControllerStatechart(Class callbackClass) {
        super(callbackClass, "EnvironmentController");

        top.onInit().transitionTo(idle).build();

        idle.on(TOO_COLD).transitionTo(heating).build();
        idle.on(TOO_HOT).transitionTo(cooling).build();

        heating.on(COMFORTABLE).transitionTo(postHeating).build();
        heating.on(TOO_HOT).transitionTo(cooling).build();

        postHeating.on(COMFORTABLE).when("heaterHasBeenStoppedAtLeast5MinsAgo").transitionTo(idle).build();
        postHeating.on(TOO_HOT).when("heaterHasBeenStoppedAtLeast5MinsAgo").transitionTo(cooling).build();
        postHeating.on(TOO_COLD).when("heaterHasBeenStoppedAtLeast5MinsAgo").transitionTo(heating).build();


        cooling.on(COMFORTABLE).transitionTo(postCooling).build();
        cooling.on(TOO_COLD).transitionTo(postCooling).build();

        postCooling.on(COMFORTABLE).when("coolerHasBeenStoppedAtLeast3MinsAgo").transitionTo(idle).build();
        postCooling.on(TOO_COLD).when("coolerHasBeenStoppedAtLeast3MinsAgo").transitionTo(heating).build();
    }
}
