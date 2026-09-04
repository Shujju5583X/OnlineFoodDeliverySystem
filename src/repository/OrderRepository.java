package repository;
import model.Customer;
import model.FoodItem;
import java.util.List;
import java.util.Map;

public interface OrderRepository {
    List<FoodItem> getMenu();
    List<FoodItem> getAllMenuItems(); // includes unavailable items
    int placeOrder(Customer customer, Map<FoodItem, Integer> cart, double totalAmount);

    // Menu management (restaurant owner)
    boolean addMenuItem(String name, double price, String description);
    boolean updateMenuItem(int itemId, String newName, double newPrice, String newDescription, boolean isAvailable);
    boolean deleteMenuItem(int itemId);
}