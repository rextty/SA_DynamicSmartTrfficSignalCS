package Controller;

import POJO.Communication;
import POJO.Intersection.Road;
import POJO.Intersection.TrafficSignalPeriod;

import Model.*;
import Enum.*;


import POJO.TrafficPeriodData;
import POJO.Vehicle;
import Repository.EmergencyRepository;
import Repository.VehicleRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

public class DynamicSmartTrafficSignal extends WebSocketClient {
    private final ObjectMapper mapper;
    private Map<String, Road> roads;
    private final EmergencyRepository emergencyRepository;
    private final RoadScoreCalculator scoreCalculator;
    private final SignalTimingCalculator timingCalculator;
    private EMode solutionMode;
    private final ScanVehicle scanVehicle;

    private final TrafficSignal trafficSignal;

    private final ArrayList<String> CCTVImages;

    private final VehicleRepository vehicleRepository;

    private final int trafficJamCondition;

    public DynamicSmartTrafficSignal(URI serverURI) throws URISyntaxException, InterruptedException, JsonProcessingException {
        super(serverURI);
        this.connectBlocking();

        CCTVImages = new ArrayList<>();
        solutionMode = EMode.NORMAL;
        // TODO: 這邊是堵塞條件
        trafficJamCondition = 300;

        mapper = new ObjectMapper();
        scanVehicle = new ScanVehicle();
        trafficSignal = new TrafficSignal();
        scoreCalculator = new RoadScoreCalculator();
        timingCalculator = new SignalTimingCalculator();

        vehicleRepository = new VehicleRepository();
        emergencyRepository = new EmergencyRepository();

        initialization();

        Thread trafficJamThread = new Thread(checkTrafficJam);
        trafficJamThread.start();

//        Thread emergencyThread = new Thread(checkEmergency);
//        emergencyThread.start();

        // Update Traffic Status
        Communication communication = new Communication();
        while (true) {
            communication.setInstruction(EInstruction.REQUIRE_DATA_TRAFFIC_PERIOD.name());
            String jsonInString = mapper.writeValueAsString(communication);
            this.send(jsonInString);

            communication.setInstruction(EInstruction.REQUIRE_DATA_CURRENT_TRAFFIC_SIGNAL_STATE.name());
            this.send(mapper.writeValueAsString(communication));

            communication.setInstruction(EInstruction.REQUIRE_DATA_CURRENT_TRAFFIC_STATUS.name());
            this.send(mapper.writeValueAsString(communication));

            Thread.sleep(1000);
        }
    }

    public void test() {
        solutionMode = EMode.TRAFFIC_JAM;
        System.out.println(solutionMode);
        System.out.println(scanVehicle.getVehicles());
        System.out.println(CCTVImages);
        System.out.println(trafficSignal.getCurrentSignal());

    }

    private void initialization() {
        ArrayList<Vehicle> initVehicles = vehicleRepository.getAllVehicle();

        Road roadEast = new Road(EDirection.EAST);
        Road roadWest = new Road(EDirection.WEST);
        Road roadSouth = new Road(EDirection.SOUTH);
        Road roadNorth = new Road(EDirection.NORTH);

        roads = new HashMap<>();
        roads.put(roadEast.getDirection().name(), roadEast);
        roads.put(roadWest.getDirection().name(), roadWest);
        roads.put(roadSouth.getDirection().name(), roadSouth);
        roads.put(roadNorth.getDirection().name(), roadNorth);

        roads.forEach((s, road) -> {
            // Deep Copy...? too weird...
            ArrayList<Vehicle> tempVehicles = new ArrayList<>();
            for (Vehicle vehicle : initVehicles) {
                Vehicle copyVehicle = new Vehicle(vehicle.getName(), vehicle.getWeight());
                tempVehicles.add(copyVehicle);
            }
            road.setVehicles(tempVehicles);
        });
    }

    private void switchMode(EMode mode, ArrayList<String> trafficJamRoad, TrafficSignalPeriod trafficJam, TrafficSignalPeriod nonTrafficJam) {
        Communication communication = new Communication();

        switch (mode) {
            case NORMAL -> {
                TrafficPeriodData nData = new TrafficPeriodData(EMode.NORMAL, trafficJamRoad, trafficJam, nonTrafficJam);
                communication.setInstruction(EInstruction.SWITCH_MODE.name());
                communication.setData(nData);
            }
            case TRAFFIC_JAM -> {
                TrafficPeriodData tData = new TrafficPeriodData(EMode.TRAFFIC_JAM, trafficJamRoad, trafficJam, nonTrafficJam);
                communication.setInstruction(EInstruction.SWITCH_MODE.name());
                communication.setData(tData);
            }
            default -> {
                return;
            }
        }

        try {
            this.send(mapper.writeValueAsString(communication));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private Runnable checkEmergency = new Runnable() {
        @Override
        public void run() {
            while (true) {
                if (emergencyRepository.checkEmergency()) {
                    System.out.println("Emergency Vehicle!");
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    };

    private final Runnable checkTrafficJam = new Runnable() {
        @Override
        public void run() {
            while (true) {
                scoreCalculator.calRoadScore(roads);
                System.out.println("NS: " + scoreCalculator.getNSRoadScore());
                System.out.println("WE: " + scoreCalculator.getWERoadScore());

                if (Objects.equals(roads.get(EDirection.EAST.name()).getTrafficSignal().getCurrentSignal(), ESignal.YELLOW.name())) {
                    scoreCalculator.calRoadScore(roads);
                    int nsScore = scoreCalculator.getNSRoadScore();
                    int weScore = scoreCalculator.getWERoadScore();

                    if (nsScore >= trafficJamCondition || weScore >= trafficJamCondition) {
                        timingCalculator.calSignalTime(nsScore, weScore);
                        switchMode(
                                EMode.TRAFFIC_JAM, timingCalculator.getTrafficJamRoads(),
                                timingCalculator.getTrafficJamSignalPeriod(),
                                timingCalculator.getNonTrafficJamSignalPeriod()
                        );
                    } else {
                        switchMode(EMode.NORMAL, null, null, null);
                    }
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    };

    @Override
    public void onOpen(ServerHandshake handShakeData) {
//        send("Hello, it is me. Mario :)");
//        System.out.println("opened connection");
        // if you plan to refuse connection based on ip or httpFields overload: onWebsocketHandshakeReceivedAsClient
    }

    @Override
    public void onMessage(String message) {
        try {
            JsonNode root = mapper.readTree(message);
            String instruction = root.path("instruction").asText();
            if (instruction.equals(EInstruction.SEND_DATA_TRAFFIC_PERIOD.name())) {
                Iterator<Map.Entry<String, JsonNode>> nodes = root.get("data").fields();
                while (nodes.hasNext()) {
                    Map.Entry<String, JsonNode> entry = nodes.next();
                    TrafficSignalPeriod period = new TrafficSignalPeriod();
                    period.setGreenSecond(entry.getValue().get(ESignal.GREEN.name()).asInt());
                    period.setYellowSecond(entry.getValue().get(ESignal.YELLOW.name()).asInt());
                    period.setRedSecond(entry.getValue().get(ESignal.RED.name()).asInt());
                    roads.get(entry.getKey()).getTrafficSignal().setSignalPeriod(period);
                }

            } else if (instruction.equals(EInstruction.SEND_DATA_CURRENT_TRAFFIC_SIGNAL_STATE.name())) {
                Iterator<Map.Entry<String, JsonNode>> nodes = root.get("data").fields();
                while (nodes.hasNext()) {
                    Map.Entry<String, JsonNode> entry = nodes.next();
                    roads.get(entry.getKey()).getTrafficSignal().setCurrentSignal(entry.getValue().asText());
                }
            } else if (instruction.equals(EInstruction.SEND_DATA_CURRENT_TRAFFIC_STATUS.name())) {
                Iterator<Map.Entry<String, JsonNode>> nodes = root.get("data").fields();
                while (nodes.hasNext()) {
                    Map.Entry<String, JsonNode> entry = nodes.next();

                    ArrayList<Vehicle> vehicles = roads.get(entry.getKey()).getVehicles();
                    for (Vehicle vehicle : vehicles) {
                        int vehicleQuantity = entry.getValue().get(vehicle.getName().toUpperCase()).asInt();
                        vehicle.setQuantity(vehicleQuantity);
                    }
                }
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        // The close codes are documented in class org.java_websocket.framing.CloseFrame
//        System.out.println( "Connection closed by " + (remote ? "remote peer" : "us") + " Code: " + code + " Reason: " + reason);
    }

    @Override
    public void onError(Exception ex) {
        ex.printStackTrace();
        // if the error is fatal then onClose will be called additionally
    }

    public static void main(String[] args) throws URISyntaxException, InterruptedException, JsonProcessingException {
        DynamicSmartTrafficSignal controller = new DynamicSmartTrafficSignal(new URI("ws://localhost:5587"));
    }
}
