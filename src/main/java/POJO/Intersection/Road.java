package POJO.Intersection;

import Model.TrafficSignal;

import POJO.Vehicle;

import Enum.EDirection;

import java.util.ArrayList;

// 如果有懂java的人看到，想修改就修，我提供自己的思路作為參考而已
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

    public Road(EDirection direction, ArrayList<Vehicle> vehicles) {
        score = 0;
        roadID = 0;
        roadName = "";
        this.vehicles = vehicles;
        this.direction = direction;
        trafficSignal = new TrafficSignal();
    }

    public void calRoadScore() {
        score = 0;
        for (Vehicle vehicle : vehicles) {
            vehicle.calScore();
            score += vehicle.getScore();
        }
    }

    public void setVehicles(ArrayList<Vehicle> vehicles) {
        this.vehicles = vehicles;
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

    public int getScore() {
        return score;
    }

    public int getRoadID() {
        return roadID;
    }

    public String getRoadName() {
        return roadName;
    }

    // RoadScoreCalculator 會需要知道道路的方向，以進行NS或WE向的道路分數加總
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
