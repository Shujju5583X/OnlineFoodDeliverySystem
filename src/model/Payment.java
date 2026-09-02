package model;

public abstract class Payment {
    protected int paymentId;
    protected double amount;
    protected String status;

    public Payment(int paymentId, double amount) {
        this.paymentId = paymentId;
        this.amount = amount;
        this.status = "PENDING";
    }

    public abstract boolean processPayment();
}