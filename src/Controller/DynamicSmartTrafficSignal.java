package Controller;

import DAO.Road;
import Model.ScanVehicle;
import Model.RoadScoreCalculator;
import Model.SignalTimingCalculator;
import Model.TrafficSignal;

import java.util.ArrayList;

// TODO: CentralController
public class DynamicSmartTrafficSignal {

    private int solutionMode;
    private ArrayList<Road> roads;

    private ScanVehicle scanVehicle;
    private TrafficSignal trafficSignal;
    private RoadScoreCalculator scoreCalculator;
    private SignalTimingCalculator timingCalculator;

    DynamicSmartTrafficSignal() {
        roads = new ArrayList<>();
        trafficSignal = new TrafficSignal();
        scoreCalculator = new RoadScoreCalculator();
        timingCalculator = new SignalTimingCalculator();
    }

    private void initialization() {}

    private void switchMode(int mode) {}

    private Runnable checkEmergency = new Runnable() {
        @Override
        public void run() {

        }
    };

    private Runnable checkTrafficGam = new Runnable() {
        @Override
        public void run() {

        }
    };
}
