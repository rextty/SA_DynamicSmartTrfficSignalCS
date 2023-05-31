package POJO;

import com.fasterxml.jackson.annotation.JsonCreator;

public class Communication {

    private String instruction;
    private TrafficPeriodData data;

    public Communication(String instruction, TrafficPeriodData data) {
        this.instruction = instruction;
        this.data = data;
    }

    public Communication() {}

    public void setData(TrafficPeriodData data) {
        this.data = data;
    }

    public void setInstruction(String instruction) {
        this.instruction = instruction;
    }

    public TrafficPeriodData getData() {
        return data;
    }

    public String getInstruction() {
        return instruction;
    }
}
