package org.wintersleep.statechart;

public class EnvironmentControllerStatechart extends Statechart {

    public static final Signal TOO_COLD = new Signal("TOO_COLD");
    public static final Signal COMFORTABLE = new Signal("COMFORTABLE");
    public static final Signal TOO_HOT = new Signal("TOO_HOT");

    private final State idle = new SimpleState(top, "IDLE");
    private final CompositeState blowing = new CompositeState(top, "BLOWING", entryActions("startBlower"), exitActions("stopBlower"));
    private final State heating = new SimpleState(blowing, "HEATING", entryActions("startHeater"), exitActions("stopHeater"));
    private final State onlyBlowing = new SimpleState(blowing, "ONLY_BLOWING");
    private final State cooling = new SimpleState(blowing, "COOLING", entryActions("startCooler"), exitActions("stopCooler"));


    public EnvironmentControllerStatechart(Class callbackClass) {
        super(callbackClass, "EnvironmentController");

        top.onInit().transitionTo(idle).build();

        idle.on(TOO_COLD).transitionTo(heating).build();
        idle.on(TOO_HOT).when("coolerHasBeenStoppedAtLeast3MinsAgo").transitionTo(cooling).build();
        idle.on(TOO_HOT).whenNot("coolerHasBeenStoppedAtLeast3MinsAgo").transitionTo(onlyBlowing).build();

        heating.on(COMFORTABLE).transitionTo(onlyBlowing).build();
        heating.on(TOO_HOT).when("coolerHasBeenStoppedAtLeast3MinsAgo").transitionTo(cooling).build();
        heating.on(TOO_HOT).whenNot("coolerHasBeenStoppedAtLeast3MinsAgo").transitionTo(onlyBlowing).build();
//        heating.on(TOO_COLD)

        cooling.on(COMFORTABLE).when("heaterHasBeenStoppedAtLeast5MinsAgo").transitionTo(idle).build();
        cooling.on(COMFORTABLE).when("heaterHasBeenStoppedAtLeast5MinsAgo").transitionTo(onlyBlowing).build();
        cooling.on(TOO_COLD).transitionTo(heating).build();

        onlyBlowing.on(COMFORTABLE).when("heaterHasBeenStoppedAtLeast5MinsAgo").transitionTo(idle).build();
        onlyBlowing.on(TOO_HOT).when("heaterHasBeenStoppedAtLeast5MinsAgo").transitionTo(cooling).build();
        onlyBlowing.on(TOO_COLD).when("heaterHasBeenStoppedAtLeast5MinsAgo").transitionTo(heating).build();

    }
}
