package model;
import java.util.List;

public class Order {
    private int orderId;
    private Customer customer;
    private Restaurant restaurant;
    private List<OrderItem> items;
    private double totalAmount;
    private String status; // PENDING, PREPARING, DISPATCHED, DELIVERED

    public Order(int orderId, Customer customer, Restaurant restaurant, List<OrderItem> items, double totalAmount) {
        this.orderId = orderId;
        this.customer = customer;
        this.restaurant = restaurant;
        this.items = items;
        this.totalAmount = totalAmount;
        this.status = "PENDING";
    }

    public void setStatus(String status) { this.status = status; }
    public int getOrderId() { return orderId; }
    public Customer getCustomer() { return customer; }

    public String getStatus() { return status; }
}