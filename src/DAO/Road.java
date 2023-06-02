package DAO;

import java.util.ArrayList;

// TODO: Road
// 如果有懂java的人看到，想修改就修，我提供自己的思路作為參考而已
public class Road {
    private int score;
    private int roadID;
    private int direction;
    private String roadName;
    private ArrayList<Vehicle> vehicles;

    // TODO: getter & setter

    public Road(int roadID, String roadName, int direction) {
        this.roadID = roadID;
        this.roadName = roadName;
        this.direction = direction;
        this.vehicles = new ArrayList<>();
    }
    
    // 在我的預想中，可能透過識別影像的工具辨別車種後，會需要一個函數將各式車車加入車車陣列中
    public void addVehicle(Vehicle vehicle) {
        vehicles.add(vehicle);
    }
    
    public void calScore() {
	int totalScore = 0;

	// 各車種自己的分數計算是可以這樣直接呼叫去計算的嗎
        for (Vehicle vehicle : vehicles) {
	    vehicle.calScore(); // 呼叫calScore()函數後當前車車物件score就有值
            totalScore += vehicle.getScore(); // 呼叫getScore()函數獲得當前車車score
        }
        score = totalScore; // 算完就直接賦值了
    }
    
    public int getScore() {
	return score;
    }

    // RoadScoreCalculator 會需要知道道路的方向，以進行NS或WE向的道路分數加總
    public int getDirection() {
        return direction;
    }
}
