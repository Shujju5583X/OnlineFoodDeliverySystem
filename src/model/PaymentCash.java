package model;

public class PaymentCash extends Payment {
    public PaymentCash(int paymentId, double amount) {
        super(paymentId, amount);
    }

    @Override
    public boolean processPayment() {
        System.out.println("Processing Cash on Delivery for amount: $" + amount);
        this.status = "COMPLETED";
        return true;
    }
}