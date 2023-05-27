package DAO;

public abstract class Vehicle {
    private final int amount;
    protected int weight;
    private int score;

    public Vehicle(int amount) {
        this.amount = amount;
    }

    public void calScore() {
        score = amount * weight;
    }

    public int getAmount() {
        return amount;
    }

    public int getWeight() {
        return weight;
    }

    public int getScore() {
        return score;
    }
}