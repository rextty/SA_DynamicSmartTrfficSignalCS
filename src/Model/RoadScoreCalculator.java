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

    // 讓controller可以利用getWERoadScore()、getNSRoadScore()去得到分數，以判斷是否壅塞
    // calSignalTime(int NS, int WE)需要傳入兩邊的分數以進行後續的計算，RoadScoreCalculator.getWERoadScore()直接作為參數不確定可不可行；
    // 或是controller那邊需要額外設立屬性先去儲存回傳值，再作為參數傳入calSignalTime
    public int getWERoadScore() {return WEroadScore;}
    public int getNSRoadScore() {return NSroadScore;}

    // 先判斷road是哪個方向，再去做NS或WE的加總
    public void calRoadScore(ArrayList<Road> roads) {
        for (Road road: roads) {
	    // direction是int，具體數值是？（暫以字元表示，較好理解）
	    if(road.getDirection() == 'N' || road.getDirection() == 'S'){
	        NSroadScore += road.getScore();
	    }
	    if(road.getDirection() == 'W' || road.getDirection() == 'E'){
	        WEroadScore += road.getScore();
	    }
        }
    }
}
