package Model;

import POJO.Intersection.TrafficSignalPeriod;

public class TrafficSignal {

    private String signal;
    private TrafficSignalPeriod signalPeriod;

    public TrafficSignal() {
        signalPeriod = new TrafficSignalPeriod();
    }

    public void setSignalPeriod(TrafficSignalPeriod signalPeriod) {
        this.signalPeriod = signalPeriod;
    }

    public TrafficSignalPeriod getSignalPeriod() {
        return signalPeriod;
    }

    public void setSignal(String signal) {
        this.signal = signal;
    }

    public String getSignal() {
        return signal;
    }

    public void changeSignal(int roadID, int time, int signalState) {}
}
