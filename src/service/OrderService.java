package service;
import model.*;
import repository.OrderRepository;
import java.util.List;
import java.util.Map;

public class OrderService {
    private OrderRepository orderRepository;
    private PaymentService paymentService;

    public OrderService(OrderRepository orderRepository, PaymentService paymentService) {
        this.orderRepository = orderRepository;
        this.paymentService = paymentService;
    }

    public List<FoodItem> getMenu() {
        return orderRepository.getMenu();
    }

    public List<FoodItem> getAllMenuItems() {
        return orderRepository.getAllMenuItems();
    }

    public boolean addMenuItem(String name, double price, String description) {
        return orderRepository.addMenuItem(name, price, description);
    }

    public boolean updateMenuItem(int itemId, String newName, double newPrice, String newDescription, boolean isAvailable) {
        return orderRepository.updateMenuItem(itemId, newName, newPrice, newDescription, isAvailable);
    }

    public boolean deleteMenuItem(int itemId) {
        return orderRepository.deleteMenuItem(itemId);
    }

    public int placeOrder(Customer customer, Map<FoodItem, Integer> cart, double total, Payment payment) {
        if (cart.isEmpty()) {
            System.out.println("Cart is empty!");
            return -1;
        }

        boolean isPaid = paymentService.executePayment(payment);

        if (isPaid) {
            return orderRepository.placeOrder(customer, cart, total);
        }

        System.out.println("Payment failed. Order not placed.");
        return -1;
    }
}