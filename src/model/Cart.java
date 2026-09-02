package model;
import java.util.ArrayList;
import java.util.List;

public class Cart {
    private List<OrderItem> items;

    public Cart() {
        this.items = new ArrayList<>();
    }

    public void addItem(OrderItem item) {
        items.add(item);
    }

    public void clearCart() {
        items.clear();
    }

    public List<OrderItem> getItems() { return items; }

    public double calculateTotal() {
        double total = 0;
        for (OrderItem item : items) {
            total += item.getSubtotal();
        }
        return total;
    }
}