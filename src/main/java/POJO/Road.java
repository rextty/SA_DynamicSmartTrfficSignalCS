package POJO;

import java.util.ArrayList;

// TODO: Road
public class Road {
    private int score;
    private int roadID;
    private int direction;
    private String roadName;
    private ArrayList<Vehicle> vehicles;

    public Road() {

    }

    public void calScore() {}

    public void setScore(int score) {
        this.score = score;
    }

    public void setRoadID(int roadID) {
        this.roadID = roadID;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public void setRoadName(String roadName) {
        this.roadName = roadName;
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

    public int getDirection() {
        return direction;
    }

    public String getRoadName() {
        return roadName;
    }

    public ArrayList<Vehicle> getVehicles() {
        return vehicles;
    }
}
