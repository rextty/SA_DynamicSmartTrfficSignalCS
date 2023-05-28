package Controller;

import POJO.Communication;
import POJO.Road;
import Model.*;
import Enum.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

// TODO: CentralController
public class DynamicSmartTrafficSignal extends WebSocketClient {

    private final ObjectMapper mapper;

    private JsonNode trafficPeriod;

    private JsonNode currentTrafficState;

    private int solutionMode;
    private ArrayList<Road> roads;

    private ScanVehicle scanVehicle;
    private TrafficSignal trafficSignal;
    private RoadScoreCalculator scoreCalculator;
    private SignalTimingCalculator timingCalculator;

    public DynamicSmartTrafficSignal(URI serverURI) throws URISyntaxException, InterruptedException, JsonProcessingException {
        super(serverURI);
        this.connectBlocking();

        mapper = new ObjectMapper();
        roads = new ArrayList<>();
        trafficSignal = new TrafficSignal();
        scoreCalculator = new RoadScoreCalculator();
        timingCalculator = new SignalTimingCalculator();

        Communication communication = new Communication();
        communication.setInstruction(EInstruction.REQUIRE_DATA_TRAFFIC_PERIOD.toString());
        String jsonInString = mapper.writeValueAsString(communication);
        this.send(jsonInString);

        communication.setInstruction(EInstruction.REQUIRE_DATA_CURRENT_TRAFFIC_STATE.toString());
        this.send(mapper.writeValueAsString(communication));
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

    @Override
    public void onOpen(ServerHandshake handShakeData) {
//        send("Hello, it is me. Mario :)");
//        System.out.println("opened connection");
        // if you plan to refuse connection based on ip or httpFields overload: onWebsocketHandshakeReceivedAsClient
    }

    @Override
    public void onMessage(String message) {
        try {
            JsonNode node = mapper.readTree(message);

            String instruction = node.path("instruction").asText();
            if (instruction.equals(EInstruction.SEND_DATA_TRAFFIC_PERIOD.name())) {
                trafficPeriod = node.path("data");
                System.out.println(trafficPeriod);
            } else if (instruction.equals(EInstruction.SEND_DATA_CURRENT_TRAFFIC_STATE.name())) {
                currentTrafficState = node.path("data");
                System.out.println(currentTrafficState);
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
