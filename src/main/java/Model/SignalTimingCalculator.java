package Model;

import POJO.Intersection.TrafficSignalPeriod;

import Enum.EDirection;

import java.util.ArrayList;

public class SignalTimingCalculator {

    // private int signalTime;
    // public int getSignalTime() {return 0;}

    // 照循序圖，調整過後的秒數（綠燈）我們是分成NS和WE兩個
    private TrafficSignalPeriod trafficJamSignalPeriod;

    private ArrayList<String> trafficJamRoads;

    private TrafficSignalPeriod nonTrafficJamSignalPeriod;

    public SignalTimingCalculator() {
        trafficJamSignalPeriod = new TrafficSignalPeriod();
        nonTrafficJamSignalPeriod = new TrafficSignalPeriod();
    }

    public void calSignalTime(int nsScore, int weScore) {
        int trafficJamCondition = 300;
        trafficJamRoads = new ArrayList<>();

        if (nsScore >= weScore) {

            trafficJamRoads.add(EDirection.NORTH.name());
            trafficJamRoads.add(EDirection.SOUTH.name());

            int time = (nsScore - trafficJamCondition) / 5;
            int greenSecond = 30;
            int yellowSecond = 5;
            int radSecond = 30;

            trafficJamSignalPeriod = new TrafficSignalPeriod(greenSecond + time, yellowSecond, radSecond - time);
            nonTrafficJamSignalPeriod = new TrafficSignalPeriod(greenSecond - time, yellowSecond, radSecond + time);
        } else {
            int x = (weScore - trafficJamCondition) / 5;

            int greenSecond = 30;
            int yellowSecond = 5;
            int radSecond = 30;

            trafficJamRoads.add(EDirection.NORTH.name());
            trafficJamRoads.add(EDirection.SOUTH.name());

            trafficJamSignalPeriod = new TrafficSignalPeriod(greenSecond + x, yellowSecond, radSecond - x);
            nonTrafficJamSignalPeriod = new TrafficSignalPeriod(greenSecond - x, yellowSecond, radSecond + x);
        }
    }

    public TrafficSignalPeriod getNonTrafficJamSignalPeriod() {
        return nonTrafficJamSignalPeriod;
    }

    public TrafficSignalPeriod getTrafficJamSignalPeriod() {
        return trafficJamSignalPeriod;
    }

    public ArrayList<String> getTrafficJamRoads() {
        return trafficJamRoads;
    }
}