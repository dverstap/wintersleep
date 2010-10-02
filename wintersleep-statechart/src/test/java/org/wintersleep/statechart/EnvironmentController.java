package org.wintersleep.statechart;

public class EnvironmentController {

    private static final int TARGET = 70;
    private static final int TOO_COLD_THRESHOLD = 65;
    private static final int TOO_HOT_THRESHOLD = 75;

    private char heater = 'h';
    private char cooler = 'c';
    private char blower = 'b';

    private int ticksSinceHeaterStopped;
    private int ticksSinceCoolerStopped;


    public void tick(EnvironmentControllerStatechart statechart, int currentTemperature) {
        ticksSinceHeaterStopped++;
        if (currentTemperature <= TOO_COLD_THRESHOLD) {
            statechart.processEvent(this, new Event(EnvironmentControllerStatechart.TOO_COLD));
        } else if (TOO_HOT_THRESHOLD <= currentTemperature) {
            statechart.processEvent(this, new Event(EnvironmentControllerStatechart.TOO_HOT));
        } else {
            statechart.processEvent(this, new Event(EnvironmentControllerStatechart.COMFORTABLE));
        }
    }

    public void startHeater() {
        heater = 'H';
    }

    public void stopHeater() {
        ticksSinceHeaterStopped = 0;
        heater = 'h';
    }

    public void startCooler() {
        cooler = 'C';
    }

    public void stopCooler() {
        ticksSinceCoolerStopped = 0;
        cooler = 'c';
    }

    public void startBlower() {
        blower = 'B';
    }

    public void stopBlower() {
        blower = 'b';
    }

    public boolean heaterHasBeenStoppedAtLeast5MinsAgo(Event event) {
        return ticksSinceHeaterStopped >= 5;
    }

    public boolean coolerHasBeenStoppedAtLeast3MinsAgo(Event event) {
        return ticksSinceCoolerStopped >= 3;
    }

    public String getState() {
        return new StringBuilder().append(heater).append(cooler).append(blower).toString();
    }

    public char getHeater() {
        return heater;
    }

    public char getCooler() {
        return cooler;
    }

    public char getBlower() {
        return blower;
    }

    //
//    public void start() {
//
//    }
//
//    public void stop() {
//
//    }
//
//    public void start() {
//
//    }
//
//    public void stop() {
//
//    }

}
