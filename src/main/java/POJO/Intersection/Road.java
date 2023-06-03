package POJO.Intersection;

import Model.TrafficSignal;

import POJO.Vehicle.Vehicle;

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

    public void calScore() {
        // 各車種自己的分數計算是可以這樣直接呼叫去計算的嗎
        score = 0;
        for (Vehicle vehicle : vehicles) {
            // 呼叫calScore()函數後當前車車物件score就有值
            vehicle.calScore();
            // 呼叫getScore()函數獲得當前車車score
            score += vehicle.getScore();
        }
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setRoadID(int roadID) {
        this.roadID = roadID;
    }

    public void setRoadName(String roadName) {
        this.roadName = roadName;
    }

    // RoadScoreCalculator 會需要知道道路的方向，以進行NS或WE向的道路分數加總
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
