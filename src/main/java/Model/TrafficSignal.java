package Model;

import Enum.ESignal;

import POJO.Intersection.TrafficSignalPeriod;

import java.util.Objects;

public class TrafficSignal {
    // private int countTime;
    private String previousSignal;
    private int direction;
    private String currentSignal;
    private TrafficSignalPeriod signalPeriod;

    public TrafficSignal() {
        signalPeriod = new TrafficSignalPeriod();
        currentSignal = ESignal.GREEN.name();
    }

    public void setCurrentSignal(String currentSignal) {
        previousSignal = currentSignal;
        this.currentSignal = currentSignal;
    }

    public void setSignalPeriod(TrafficSignalPeriod signalPeriod) {
        this.signalPeriod = signalPeriod;
    }

    public TrafficSignalPeriod getSignalPeriod() {
        return signalPeriod;
    }

    public String getCurrentSignal() {
        return currentSignal;
    }

    public void transitionToNextState() {
        if (Objects.equals(currentSignal, ESignal.GREEN.name())) {
            currentSignal = ESignal.YELLOW.name();
        } else if (Objects.equals(currentSignal, ESignal.YELLOW.name()) && Objects.equals(previousSignal, ESignal.GREEN.name())) {
            currentSignal = ESignal.RED.name();
            previousSignal = ESignal.RED.name();
        } else if (Objects.equals(currentSignal, ESignal.YELLOW.name()) && Objects.equals(previousSignal, ESignal.RED.name())) {
            currentSignal = ESignal.GREEN.name();
            previousSignal = ESignal.GREEN.name();
        } else if (Objects.equals(currentSignal, ESignal.RED.name())) {
            currentSignal = ESignal.YELLOW.name();
        }
    }

    public void changeSignal(int roadID, int time, int signalState) {
        // TODO:
    }

    public void setDefaultDuration() {
        // TODO:
    }

    public void sendAdjustedTiming() {
        // TODO:
    }

    public void decreasingReaLightForEmergency() {
        // TODO:
    }

    public void decreasingNonEmergencyGreenLight() {
        // TODO:
    }
}