package POJO.Intersection;

import Model.TrafficSignal;

import POJO.Vehicle.Vehicle;

import Enum.EDirection;

import java.util.ArrayList;

public class Road {
    private int score;
    private int roadID;
    private String roadName;
    private EDirection direction;
    private TrafficSignal trafficSignal;
    private ArrayList<Vehicle> vehicles;

    public Road() {
        score = 0;
        roadID = 0;
        roadName = "";
        direction = null;
        vehicles = new ArrayList<>();
        trafficSignal = new TrafficSignal();
    }

    public Road(EDirection direction) {
        score = 0;
        roadID = 0;
        roadName = "";
        this.direction = direction;
        vehicles = new ArrayList<>();
        trafficSignal = new TrafficSignal();
    }

    public void calScore() {}

    public void setScore(int score) {
        this.score = score;
    }

    public void setRoadID(int roadID) {
        this.roadID = roadID;
    }

    public void setRoadName(String roadName) {
        this.roadName = roadName;
    }

    public void setDirection(EDirection direction) {
        this.direction = direction;
    }

    public void setTrafficSignal(TrafficSignal trafficSignal) {
        this.trafficSignal = trafficSignal;
    }

    public void setVehicles(ArrayList<Vehicle> vehicles) {
        this.vehicles = vehicles;
    }

    public int getScore() {
        return score;
    }

    public int getRoadID() {
        return roadID;
    }

    public String getRoadName() {
        return roadName;
    }

    public EDirection getDirection() {
        return direction;
    }

    public TrafficSignal getTrafficSignal() {
        return trafficSignal;
    }

    public ArrayList<Vehicle> getVehicles() {
        return vehicles;
    }
}
