package repository;

import config.DatabaseConfig;
import model.Customer;
import model.DeliveryPerson;
import model.User;
import java.sql.*;

public class JdbcUserRepository implements UserRepository {

    @Override
    public void save(User user) {
        String insertUser = "INSERT INTO users (name, email, password, role) VALUES (?, ?, ?, ?)";
        String insertCustomer = "INSERT INTO customers (user_id, address, phone_number) VALUES (?, ?, ?)";
        String insertDelivery = "INSERT INTO delivery_persons (user_id, vehicle_number, is_available) VALUES (?, ?, TRUE)";

        try (Connection conn = DatabaseConfig.getConnection()) {
            conn.setAutoCommit(false);
            try (PreparedStatement userStmt = conn.prepareStatement(insertUser, Statement.RETURN_GENERATED_KEYS)) {

                userStmt.setString(1, user.getName());
                userStmt.setString(2, user.getEmail());
                userStmt.setString(3, user.getPassword());

                String role = "CUSTOMER";
                if (user instanceof DeliveryPerson) role = "DELIVERY_PERSON";
                else if (user instanceof model.RestaurantOwner) role = "RESTAURANT_OWNER";
                else if (user instanceof model.Admin) role = "ADMIN";

                userStmt.setString(4, role);
                userStmt.executeUpdate();

                ResultSet keys = userStmt.getGeneratedKeys();
                if (keys.next()) {
                    int userId = keys.getInt(1);
                    if (user instanceof Customer) {
                        Customer c = (Customer) user;
                        try (PreparedStatement custStmt = conn.prepareStatement(insertCustomer)) {
                            custStmt.setInt(1, userId);
                            custStmt.setString(2, c.getAddress());
                            custStmt.setString(3, c.getPhoneNumber());
                            custStmt.executeUpdate();
                        }
                    } else if (user instanceof DeliveryPerson) {
                        DeliveryPerson d = (DeliveryPerson) user;
                        try (PreparedStatement delStmt = conn.prepareStatement(insertDelivery)) {
                            delStmt.setInt(1, userId);
                            delStmt.setString(2, d.getVehicleNumber());
                            delStmt.executeUpdate();
                        }
                    }
                }
                conn.commit();
                System.out.println("Registration successful!");
            } catch (SQLException e) {
                conn.rollback();
                System.out.println("Registration failed. Email might already exist.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Keep ONLY this updated login method
    @Override
    public User login(String email, String password) {
        String query = "SELECT u.user_id, u.name, u.email, u.password, u.role, c.address, c.phone_number, d.vehicle_number " +
                "FROM users u LEFT JOIN customers c ON u.user_id = c.user_id " +
                "LEFT JOIN delivery_persons d ON u.user_id = d.user_id " +
                "WHERE u.email = ? AND u.password = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, email); stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String role = rs.getString("role");
                if ("CUSTOMER".equals(role)) {
                    return new Customer(rs.getInt("user_id"), rs.getString("name"), rs.getString("email"),
                            rs.getString("password"), rs.getString("address"), rs.getString("phone_number"));
                } else if ("DELIVERY_PERSON".equals(role)) {
                    return new DeliveryPerson(rs.getInt("user_id"), rs.getString("name"), rs.getString("email"),
                            rs.getString("password"), rs.getString("vehicle_number"));
                } else if ("ADMIN".equals(role)) {
                    return new model.Admin(rs.getInt("user_id"), rs.getString("name"), rs.getString("email"), rs.getString("password"));
                } else if ("RESTAURANT_OWNER".equals(role)) {
                    return new model.RestaurantOwner(rs.getInt("user_id"), rs.getString("name"), rs.getString("email"), rs.getString("password"));
                }
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }
}