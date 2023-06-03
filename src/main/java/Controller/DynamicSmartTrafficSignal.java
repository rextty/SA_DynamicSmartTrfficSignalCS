package Controller;

import POJO.Communication;
import POJO.Intersection.Road;
import POJO.Intersection.TrafficSignalPeriod;

import Model.*;
import Enum.*;


import POJO.TrafficPeriodData;
import POJO.Vehicle.Car;
import POJO.Vehicle.HeavyVehicle;
import POJO.Vehicle.Motorcycle;
import POJO.Vehicle.Vehicle;
import Repository.CCTVRepository;
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

    private int solutionMode;
    private Map<String, Road> roads;
    private ScanVehicle scanVehicle;

    private ArrayList<String> CCTVImages;
    private CCTVRepository cctvRepository;
    private TrafficSignal trafficSignal;
    private RoadScoreCalculator scoreCalculator;
    private SignalTimingCalculator timingCalculator;

    private int trafficJamCondition;

    public DynamicSmartTrafficSignal(URI serverURI) throws URISyntaxException, InterruptedException, JsonProcessingException {
        super(serverURI);
        this.connectBlocking();

        trafficJamCondition = 300;

        mapper = new ObjectMapper();
        scanVehicle = new ScanVehicle();
        trafficSignal = new TrafficSignal();
        cctvRepository = new CCTVRepository();
        scoreCalculator = new RoadScoreCalculator();
        timingCalculator = new SignalTimingCalculator();

        initialization();

        Thread trafficJamThread = new Thread(checkTrafficJam);
        trafficJamThread.start();

        Thread emergencyThread = new Thread(checkEmergency);
        emergencyThread.start();

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
        // TODO: 兩種方式取得紅綠燈資訊, 1. 定期請求資料 2. 有需要時我再去取得資料 目前是1
    }

    private void initialization() {
        Road roadEast = new Road(EDirection.EAST);
        Road roadWest = new Road(EDirection.WEST);
        Road roadSouth = new Road(EDirection.SOUTH);
        Road roadNorth = new Road(EDirection.NORTH);

        roads = new HashMap<>();
        roads.put(roadEast.getDirection().name(), roadEast);
        roads.put(roadWest.getDirection().name(), roadWest);
        roads.put(roadSouth.getDirection().name(), roadSouth);
        roads.put(roadNorth.getDirection().name(), roadNorth);
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
            // TODO: get database data...
        }
    };

    private Runnable checkTrafficJam = new Runnable() {
        @Override
        public void run() {
            while (true) {
                scoreCalculator.calRoadScore(roads);
                System.out.println("NS: " + scoreCalculator.getNSRoadScore());
                System.out.println("WE: " + scoreCalculator.getWERoadScore());

                if (Objects.equals(roads.get(EDirection.EAST.name()).getTrafficSignal().getSignal(), ESignal.YELLOW.name())) {
                    scoreCalculator.calRoadScore(roads);
                    int nsScore = scoreCalculator.getNSRoadScore();
                    int weScore = scoreCalculator.getWERoadScore();

                    ArrayList<String> trafficJamRoads = new ArrayList<>();

                    // TODO: 先不判斷兩邊都塞...
                    if (nsScore >= trafficJamCondition) {
                        int x = (nsScore - trafficJamCondition) / 5;
                        int greenSecond = roads.get(EDirection.NORTH.name()).getTrafficSignal().getSignalPeriod().getGreenSecond();
                        int yellowSecond = roads.get(EDirection.NORTH.name()).getTrafficSignal().getSignalPeriod().getYellowSecond();
                        int radSecond = roads.get(EDirection.NORTH.name()).getTrafficSignal().getSignalPeriod().getRedSecond();

                        trafficJamRoads.add(EDirection.NORTH.name());
                        trafficJamRoads.add(EDirection.SOUTH.name());

                        TrafficSignalPeriod trafficJamSignalPeriod = new TrafficSignalPeriod(greenSecond + x, yellowSecond, radSecond - x);
                        TrafficSignalPeriod nonTrafficJamSignalPeriod = new TrafficSignalPeriod(greenSecond - x, yellowSecond, radSecond + x);

                        switchMode(EMode.TRAFFIC_JAM, trafficJamRoads, trafficJamSignalPeriod, nonTrafficJamSignalPeriod);

                    } else if (weScore >= trafficJamCondition) {
                        int x = (weScore - trafficJamCondition) / 5;

                        TrafficSignalPeriod currentSignalPeriod = roads.get(EDirection.WEST.name()).getTrafficSignal().getSignalPeriod();

                        int greenSecond = currentSignalPeriod.getGreenSecond();
                        int yellowSecond = currentSignalPeriod.getYellowSecond();
                        int radSecond = currentSignalPeriod.getRedSecond();

                        trafficJamRoads.add(EDirection.NORTH.name());
                        trafficJamRoads.add(EDirection.SOUTH.name());

                        TrafficSignalPeriod trafficJamSignalPeriod = new TrafficSignalPeriod(greenSecond + x, yellowSecond, radSecond - x);
                        TrafficSignalPeriod nonTrafficJamSignalPeriod = new TrafficSignalPeriod(greenSecond - x, yellowSecond, radSecond + x);

                        switchMode(EMode.TRAFFIC_JAM, trafficJamRoads, trafficJamSignalPeriod, nonTrafficJamSignalPeriod);
                    }else {
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
                    roads.get(entry.getKey()).getTrafficSignal().setSignal(entry.getValue().asText());
                }
            } else if (instruction.equals(EInstruction.SEND_DATA_CURRENT_TRAFFIC_STATUS.name())) {
                Iterator<Map.Entry<String, JsonNode>> nodes = root.get("data").fields();
                while (nodes.hasNext()) {
                    Map.Entry<String, JsonNode> entry = nodes.next();

                    ArrayList<Vehicle> vehicles = new ArrayList<>();

                    vehicles.add(new Motorcycle(entry.getValue().get(EVehicle.BIKE.name()).asInt()));
                    vehicles.add(new Car(entry.getValue().get(EVehicle.CAR.name()).asInt()));
                    vehicles.add(new HeavyVehicle(entry.getValue().get(EVehicle.BUS.name()).asInt() + entry.getValue().get(EVehicle.TRUCK.name()).asInt()));

                    roads.get(entry.getKey()).setVehicles(vehicles);

//                    int a = 0;
//                    for (Vehicle v : roads.get(entry.getKey()).getVehicles()) {
//                        a += v.getAmount();
//                    }
//                    System.out.printf("%s: %d\n", entry.getKey(), a);
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
