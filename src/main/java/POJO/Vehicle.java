package POJO;

public class Vehicle {
    private int score;
    private int quantity;
    private final String name;
    private final int weight;

    public Vehicle(String name, int weight) {
        this.name = name;
        this.weight = weight;
    }

    public void calScore() {
        score = quantity * weight;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getScore() {
        return score;
    }

    public String getName() {
        return name;
    }

    public int getWeight() {
        return weight;
    }

    public int getQuantity() {
        return quantity;
    }
}