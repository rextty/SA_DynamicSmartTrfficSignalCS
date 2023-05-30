package Controller;

import POJO.Communication;
import POJO.Intersection.Road;
import POJO.Intersection.TrafficSignalPeriod;

import Model.*;
import Enum.ESignal;
import Enum.EDirection;
import Enum.EInstruction;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class DynamicSmartTrafficSignal extends WebSocketClient {

    private final ObjectMapper mapper;

    private int solutionMode;
    private Map<String, Road> roads;

    private ScanVehicle scanVehicle;
    private TrafficSignal trafficSignal;
    private RoadScoreCalculator scoreCalculator;
    private SignalTimingCalculator timingCalculator;

    public DynamicSmartTrafficSignal(URI serverURI) throws URISyntaxException, InterruptedException, JsonProcessingException {
        super(serverURI);
        this.connectBlocking();

        mapper = new ObjectMapper();

        Road roadEast = new Road(EDirection.EAST);
        Road roadWest = new Road(EDirection.WEST);
        Road roadSouth = new Road(EDirection.SOUTH);
        Road roadNorth = new Road(EDirection.NORTH);

        roads = new HashMap<>();
        roads.put(roadEast.getDirection().name(), roadEast);
        roads.put(roadWest.getDirection().name(), roadWest);
        roads.put(roadSouth.getDirection().name(), roadSouth);
        roads.put(roadNorth.getDirection().name(), roadNorth);

        trafficSignal = new TrafficSignal();
        scoreCalculator = new RoadScoreCalculator();
        timingCalculator = new SignalTimingCalculator();

        // Update Traffic Status
        Communication communication = new Communication();
        while (true) {
            communication.setInstruction(EInstruction.REQUIRE_DATA_TRAFFIC_PERIOD.toString());
            String jsonInString = mapper.writeValueAsString(communication);
            this.send(jsonInString);

            communication.setInstruction(EInstruction.REQUIRE_DATA_CURRENT_TRAFFIC_STATE.toString());
            this.send(mapper.writeValueAsString(communication));
            Thread.sleep(2000);
        }
        // TODO: 兩種方式取得紅綠燈資訊, 1. 定期請求資料 2. 有需要時我再去取得資料 目前是1
    }

    private void initialization() {}

    private void switchMode(int mode) {}

    private Runnable updateTrafficStatus = new Runnable() {
        @Override
        public void run() {

        }
    };

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
                TrafficSignalPeriod period = new TrafficSignalPeriod();
                while (nodes.hasNext()) {
                    Map.Entry<String, JsonNode> entry = nodes.next();
                    period.setGreenSecond(entry.getValue().get(ESignal.GREEN.name()).asInt());
                    period.setYellowSecond(entry.getValue().get(ESignal.YELLOW.name()).asInt());
                    period.setRedSecond(entry.getValue().get(ESignal.RED.name()).asInt());
                    roads.get(entry.getKey()).getTrafficSignal().setSignalPeriod(period);
                }
            } else if (instruction.equals(EInstruction.SEND_DATA_CURRENT_TRAFFIC_STATE.name())) {
                Iterator<Map.Entry<String, JsonNode>> nodes = root.get("data").fields();
                while (nodes.hasNext()) {
                    Map.Entry<String, JsonNode> entry = nodes.next();
                    roads.get(entry.getKey()).getTrafficSignal().setSignal(entry.getValue().asText());
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
