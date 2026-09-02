package repository;

import config.DatabaseConfig;
import model.Customer;
import model.FoodItem;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class JdbcOrderRepository implements OrderRepository {

    @Override
    public List<FoodItem> getMenu() {
        List<FoodItem> menu = new ArrayList<>();
        String query = "SELECT item_id, name, price FROM food_items WHERE is_available = TRUE";

        try (Connection conn = DatabaseConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                menu.add(new FoodItem(rs.getInt("item_id"), rs.getString("name"), rs.getDouble("price")));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return menu;
    }

    @Override
    public int placeOrder(Customer customer, Map<FoodItem, Integer> cart, double totalAmount) {
        String insertOrder = "INSERT INTO orders (customer_id, restaurant_id, total_amount, status) VALUES (?, 1, ?, 'PENDING')";
        String insertOrderItem = "INSERT INTO order_items (order_id, item_id, quantity, price_at_purchase) VALUES (?, ?, ?, ?)";
        int generatedOrderId = -1;

        try (Connection conn = DatabaseConfig.getConnection()) {
            conn.setAutoCommit(false);

            try (PreparedStatement orderStmt = conn.prepareStatement(insertOrder, Statement.RETURN_GENERATED_KEYS)) {
                orderStmt.setInt(1, customer.getId());
                orderStmt.setDouble(2, totalAmount);
                orderStmt.executeUpdate();

                ResultSet keys = orderStmt.getGeneratedKeys();
                if (keys.next()) {
                    generatedOrderId = keys.getInt(1);

                    try (PreparedStatement itemStmt = conn.prepareStatement(insertOrderItem)) {
                        for (Map.Entry<FoodItem, Integer> entry : cart.entrySet()) {
                            itemStmt.setInt(1, generatedOrderId);
                            itemStmt.setInt(2, entry.getKey().getId());
                            itemStmt.setInt(3, entry.getValue());
                            itemStmt.setDouble(4, entry.getKey().getPrice());
                            itemStmt.addBatch();
                        }
                        itemStmt.executeBatch();
                    }
                }
                conn.commit();
                System.out.println("Order placed successfully! Total Paid: $" + totalAmount);
                return generatedOrderId;
            } catch (SQLException e) {
                conn.rollback();
                System.out.println("Failed to place order.");
            }
        } catch (SQLException e) { e.printStackTrace(); }

        return generatedOrderId;
    }
}