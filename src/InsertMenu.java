import config.DatabaseConfig;
import java.sql.Connection;
import java.sql.Statement;

public class InsertMenu {
    public static void main(String[] args) {
        try (Connection conn = DatabaseConfig.getConnection();
             Statement stmt = conn.createStatement()) {
            
            stmt.executeUpdate("INSERT INTO food_items (name, price, is_available) VALUES ('Pizza', 12.99, TRUE)");
            stmt.executeUpdate("INSERT INTO food_items (name, price, is_available) VALUES ('Burger', 8.99, TRUE)");
            stmt.executeUpdate("INSERT INTO food_items (name, price, is_available) VALUES ('Pasta', 10.99, TRUE)");
            
            System.out.println("Food items inserted successfully!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
