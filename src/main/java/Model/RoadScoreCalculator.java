package Model;

import POJO.Intersection.Road;

import Enum.EDirection;

import java.util.Map;

// 有懂java的人看到，歡迎修改，我的內容僅提供自己的思路以供參考
public class RoadScoreCalculator {
    // 照循序圖，計算道路分數時有區分成NS與WE兩種，故拆開
    // 不太確定若一開始便設為零可不可行
    private int WERoadScore;
    private int NSRoadScore;

    public RoadScoreCalculator() {
        WERoadScore = 0;
        NSRoadScore = 0;
    }

    // 讓controller可以利用getWERoadScore()、getNSRoadScore()去得到分數，以判斷是否壅塞
    // calSignalTime(int NS, int WE)需要傳入兩邊的分數以進行後續的計算，RoadScoreCalculator.getWERoadScore()直接作為參數不確定可不可行；
    // 或是controller那邊需要額外設立屬性先去儲存回傳值，再作為參數傳入calSignalTime

    public int getNSRoadScore() {
        return NSRoadScore;
    }

    public int getWERoadScore() {
        return WERoadScore;
    }

    // 先判斷road是哪個方向，再去做NS或WE的加總
    public void calRoadScore(Map<String, Road> roads) {
        NSRoadScore = 0;
        WERoadScore = 0;

        for (Map.Entry<String, Road> entry : roads.entrySet()) {
            Road road = entry.getValue();

            if(road.getDirection() == EDirection.NORTH || road.getDirection() == EDirection.SOUTH) {
                road.calScore();
                NSRoadScore += road.getScore();
            }
            if(road.getDirection() == EDirection.WEST || road.getDirection() == EDirection.EAST) {
                road.calScore();
                WERoadScore += road.getScore();
            }
        }
    }
}