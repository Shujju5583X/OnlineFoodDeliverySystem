package repository;

import config.DatabaseConfig;
import model.DeliveryPerson;
import java.sql.*;

public class JdbcDeliveryRepository implements DeliveryRepository {

    @Override
    public DeliveryPerson findAvailableDriver() {
        String query = "SELECT u.user_id, u.name, u.email, u.password, d.vehicle_number " +
                "FROM users u JOIN delivery_persons d ON u.user_id = d.user_id " +
                "WHERE d.is_available = TRUE LIMIT 1";

        try (Connection conn = DatabaseConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            if (rs.next()) {
                return new DeliveryPerson(rs.getInt("user_id"), rs.getString("name"),
                        rs.getString("email"), rs.getString("password"),
                        rs.getString("vehicle_number"));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    @Override
    public void assignDelivery(int orderId, int driverId) {
        String insertDelivery = "INSERT INTO deliveries (order_id, delivery_person_id) VALUES (?, ?)";
        String updateOrder = "UPDATE orders SET status = 'DISPATCHED' WHERE order_id = ?";
        String updateDriver = "UPDATE delivery_persons SET is_available = FALSE WHERE user_id = ?";

        try (Connection conn = DatabaseConfig.getConnection()) {
            conn.setAutoCommit(false);
            try (PreparedStatement dStmt = conn.prepareStatement(insertDelivery);
                 PreparedStatement oStmt = conn.prepareStatement(updateOrder);
                 PreparedStatement drStmt = conn.prepareStatement(updateDriver)) {

                dStmt.setInt(1, orderId); dStmt.setInt(2, driverId); dStmt.executeUpdate();
                oStmt.setInt(1, orderId); oStmt.executeUpdate();
                drStmt.setInt(1, driverId); drStmt.executeUpdate();
                conn.commit();
            } catch (SQLException e) { conn.rollback(); }
        } catch (SQLException e) { e.printStackTrace(); }
    }

    @Override
    public void completeDelivery(int driverId) {
        String query = "UPDATE deliveries d JOIN orders o ON d.order_id = o.order_id " +
                "JOIN delivery_persons dp ON d.delivery_person_id = dp.user_id " +
                "SET d.status = 'DELIVERED', d.delivered_at = CURRENT_TIMESTAMP, " +
                "o.status = 'DELIVERED', dp.is_available = TRUE " +
                "WHERE d.delivery_person_id = ? AND d.status = 'ASSIGNED'";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, driverId);
            int rows = stmt.executeUpdate();
            if (rows > 0) System.out.println("Delivery completed successfully!");
            else System.out.println("No active deliveries found.");
        } catch (SQLException e) { e.printStackTrace(); }
    }
}