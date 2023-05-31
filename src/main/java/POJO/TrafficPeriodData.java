package POJO;

import Enum.EMode;
import POJO.Intersection.TrafficSignalPeriod;

public class TrafficPeriodData {

    private EMode trafficMode;
    private TrafficSignalPeriod trafficJam;
    private TrafficSignalPeriod nonTrafficJam;

    public TrafficPeriodData() {}

    public TrafficPeriodData(EMode trafficMode, TrafficSignalPeriod trafficJam, TrafficSignalPeriod nonTrafficJam) {
        this.trafficMode = trafficMode;
        this.trafficJam = trafficJam;
        this.nonTrafficJam = nonTrafficJam;
    }

    public void setTrafficMode(EMode trafficMode) {
        this.trafficMode = trafficMode;
    }

    public void setTrafficJam(TrafficSignalPeriod trafficJam) {
        this.trafficJam = trafficJam;
    }

    public void setNonTrafficJam(TrafficSignalPeriod nonTrafficJam) {
        this.nonTrafficJam = nonTrafficJam;
    }

    public EMode getTrafficMode() {
        return trafficMode;
    }

    public TrafficSignalPeriod getNonTrafficJam() {
        return nonTrafficJam;
    }

    public TrafficSignalPeriod getTrafficJam() {
        return trafficJam;
    }
}
