package model;

public class PaymentCard extends Payment {
    private String cardNumber;

    public PaymentCard(int paymentId, double amount, String cardNumber) {
        super(paymentId, amount);
        this.cardNumber = cardNumber;
    }

    @Override
    public boolean processPayment() {
        System.out.println("Processing Card Payment for amount: $" + amount + " using card " + cardNumber);
        this.status = "COMPLETED";
        return true;
    }
}