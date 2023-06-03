package POJO;

import Enum.EMode;
import POJO.Intersection.Road;
import POJO.Intersection.TrafficSignalPeriod;

import java.util.ArrayList;

public class TrafficPeriodData {

    private EMode trafficMode;

    private ArrayList<String> trafficJamRoad;
    private TrafficSignalPeriod trafficJam;
    private TrafficSignalPeriod nonTrafficJam;

    public TrafficPeriodData() {
        trafficJamRoad = new ArrayList<>();
    }

    public TrafficPeriodData(EMode trafficMode, ArrayList<String> trafficJamRoads, TrafficSignalPeriod trafficJam, TrafficSignalPeriod nonTrafficJam) {
        this.trafficJamRoad = trafficJamRoads;
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

    public void setTrafficJamRoad(ArrayList<String> trafficJamRoad) {
        this.trafficJamRoad = trafficJamRoad;
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

    public ArrayList<String> getTrafficJamRoad() {
        return trafficJamRoad;
    }
}
