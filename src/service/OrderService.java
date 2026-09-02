package service;
import model.*;
import java.util.ArrayList;

public class OrderService {
    private int orderCounter = 1;
    private PaymentService paymentService;

    public OrderService(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    public Order placeOrder(Customer customer, Restaurant restaurant, Cart cart, Payment payment) {
        if (cart.getItems().isEmpty()) {
            System.out.println("Cart is empty!");
            return null;
        }

        double total = cart.calculateTotal();
        boolean isPaid = paymentService.executePayment(payment);

        if (isPaid) {
            Order order = new Order(orderCounter++, customer, restaurant, new ArrayList<>(cart.getItems()), total);
            cart.clearCart();
            System.out.println("Order " + order.getOrderId() + " placed successfully!");
            return order;
        }

        System.out.println("Payment failed. Order not placed.");
        return null;
    }
}