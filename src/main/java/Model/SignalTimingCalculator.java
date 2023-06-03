package Model;

import POJO.Intersection.TrafficSignalPeriod;

public class SignalTimingCalculator {

    // private int signalTime;
    // public int getSignalTime() {return 0;}

    // 照循序圖，調整過後的秒數（綠燈）我們是分成NS和WE兩個
    private TrafficSignalPeriod trafficJamSignalPeriod;

    private TrafficSignalPeriod nonTrafficJamSignalPeriod;

    private final int trafficJamCondition = 300;

    public SignalTimingCalculator() {
        trafficJamSignalPeriod = new TrafficSignalPeriod();
        nonTrafficJamSignalPeriod = new TrafficSignalPeriod();
    }

    public void calSignalTime(int nsScore, int weScore) {
        if (nsScore >= weScore) {
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
}