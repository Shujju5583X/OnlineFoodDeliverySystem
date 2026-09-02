package model;

public class PaymentUPI extends Payment {
    private String upiId;

    public PaymentUPI(int paymentId, double amount, String upiId) {
        super(paymentId, amount);
        this.upiId = upiId;
    }

    @Override
    public boolean processPayment() {
        System.out.println("Processing UPI Payment for amount: $" + amount + " via " + upiId);
        this.status = "COMPLETED";
        return true;
    }
}