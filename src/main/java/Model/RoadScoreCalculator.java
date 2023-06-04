package Model;

import POJO.Intersection.Road;

import Enum.EDirection;

import java.util.Map;

public class RoadScoreCalculator {
    private int WERoadScore;
    private int NSRoadScore;

    public RoadScoreCalculator() {
        WERoadScore = 0;
        NSRoadScore = 0;
    }

    public int getNSRoadScore() {
        return NSRoadScore;
    }

    public int getWERoadScore() {
        return WERoadScore;
    }

    public void calRoadScore(Map<String, Road> roads) {
        NSRoadScore = 0;
        WERoadScore = 0;

        roads.forEach((s, road) -> {
            if(road.getDirection() == EDirection.NORTH || road.getDirection() == EDirection.SOUTH) {
                road.calRoadScore();
                NSRoadScore += road.getScore();
            }
            if(road.getDirection() == EDirection.WEST || road.getDirection() == EDirection.EAST) {
                road.calRoadScore();
                WERoadScore += road.getScore();
            }
        });
    }
}