package repository;

import config.DatabaseConfig;
import model.Customer;
import model.User;
import java.sql.*;

public class JdbcUserRepository implements UserRepository {

    @Override
    public void save(User user) {
        String insertUser = "INSERT INTO users (name, email, password, role) VALUES (?, ?, ?, ?)";
        String insertCustomer = "INSERT INTO customers (user_id, address, phone_number) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseConfig.getConnection()) {
            conn.setAutoCommit(false);
            try (PreparedStatement userStmt = conn.prepareStatement(insertUser, Statement.RETURN_GENERATED_KEYS)) {

                userStmt.setString(1, user.getName());
                userStmt.setString(2, user.getEmail());
                userStmt.setString(3, user.getPassword());
                userStmt.setString(4, "CUSTOMER");
                userStmt.executeUpdate();

                ResultSet keys = userStmt.getGeneratedKeys();
                if (keys.next() && user instanceof Customer) {
                    int userId = keys.getInt(1);
                    Customer c = (Customer) user;

                    try (PreparedStatement custStmt = conn.prepareStatement(insertCustomer)) {
                        custStmt.setInt(1, userId);
                        custStmt.setString(2, c.getAddress());
                        custStmt.setString(3, c.getPhoneNumber());
                        custStmt.executeUpdate();
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

    @Override
    public User login(String email, String password) {
        String query = "SELECT u.user_id, u.name, u.email, u.password, c.address, c.phone_number " +
                "FROM users u JOIN customers c ON u.user_id = c.user_id " +
                "WHERE u.email = ? AND u.password = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, email);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Customer(
                        rs.getInt("user_id"), rs.getString("name"), rs.getString("email"),
                        rs.getString("password"), rs.getString("address"), rs.getString("phone_number")
                );
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }
}