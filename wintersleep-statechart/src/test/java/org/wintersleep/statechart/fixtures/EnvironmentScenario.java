package org.wintersleep.statechart.fixtures;

import org.wintersleep.statechart.EnvironmentController;
import org.wintersleep.statechart.EnvironmentControllerStatechart;

public class EnvironmentScenario {

    private final String name;

    private int minute;
    private int temperature;
    private EnvironmentControllerStatechart sc;
    private EnvironmentController controller;


    public EnvironmentScenario(String name) {
        this.name = name;
        sc = new EnvironmentControllerStatechart(EnvironmentController.class);
        controller = new EnvironmentController();
        sc.start(controller);
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public void setTemp(int temperature) {
        this.temperature = temperature;
    }

    public void execute() {
        controller.tick(sc, temperature);
    }

    public String Heater() {
        return state(controller.getHeater());
    }

    public String Blower() {
        return state(controller.getBlower());
    }

    public String Cooler() {
        return state(controller.getCooler());
    }

    private String state(char ch) {
        return Character.isUpperCase(ch) ? "X" : "-";
    }

}
