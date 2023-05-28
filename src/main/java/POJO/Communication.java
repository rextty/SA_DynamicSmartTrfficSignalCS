package POJO;

import com.fasterxml.jackson.annotation.JsonCreator;

public class Communication {

    private String instruction;
    private String data;

    public Communication(String instruction, String data) {
        this.instruction = instruction;
        this.data = data;
    }

    public Communication() {}

    public void setData(String data) {
        this.data = data;
    }

    public void setInstruction(String instruction) {
        this.instruction = instruction;
    }

    public String getData() {
        return data;
    }

    public String getInstruction() {
        return instruction;
    }
}
