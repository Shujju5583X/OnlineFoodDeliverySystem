package repository;
import model.Customer;
import model.FoodItem;
import java.util.List;
import java.util.Map;

public interface OrderRepository {
    List<FoodItem> getMenu();
    int placeOrder(Customer customer, Map<FoodItem, Integer> cart, double totalAmount);
}