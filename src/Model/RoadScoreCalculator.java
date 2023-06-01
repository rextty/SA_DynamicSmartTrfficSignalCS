package Model;

import DAO.Road;

import java.util.ArrayList;

// 有懂java的人看到，歡迎修改，我的內容僅提供自己的思路以供參考
public class RoadScoreCalculator {

    // 照循序圖，計算道路分數時有區分成NS與WE兩種，故拆開
    // 不太確定若一開始便設為零可不可行
    private int WEroadScore = 0;
    private int NSroadScore = 0;

    public RoadScoreCalculator() {}

    public int getWERoadScore() {return WEroadScore;}
    public int getNSRoadScore() {return NSroadScore;}

    // 先判斷road是哪個方向，再去做NS或WE的加總
    public void calRoadScore(ArrayList<Road> roads) {
        for (Road road: roads) {
	    // direction是int，具體數值是?
	    if(road.direction == 'N' || road.direction == 'S'){
	        NSroadScore += road.getScore();
	    }
	    if(road.direction == 'W' || road.direction == 'E'){
	        WEroadScore += road.getScore();
	    }
        }
    }
}
