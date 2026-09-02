package service;
import model.Payment;

public class PaymentService {
    public boolean executePayment(Payment payment) {
        return payment.processPayment();
    }
}