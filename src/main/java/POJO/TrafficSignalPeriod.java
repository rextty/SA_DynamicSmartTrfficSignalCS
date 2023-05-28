package POJO;

public class TrafficSignalPeriod {
    private int greenSecond;
    private int yellowSecond;
    private int redSecond;

    public TrafficSignalPeriod() {}

    public TrafficSignalPeriod(int greenSecond, int yellowSecond, int redSecond) {
        this.greenSecond = greenSecond;
        this.yellowSecond = yellowSecond;
        this.redSecond = redSecond;
    }

    public void setGreenSecond(int greenSecond) {
        this.greenSecond = greenSecond;
    }

    public void setRedSecond(int redSecond) {
        this.redSecond = redSecond;
    }

    public void setYellowSecond(int yellowSecond) {
        this.yellowSecond = yellowSecond;
    }

    public int getGreenSecond() {
        return greenSecond;
    }

    public int getRedSecond() {
        return redSecond;
    }

    public int getYellowSecond() {
        return yellowSecond;
    }
}
