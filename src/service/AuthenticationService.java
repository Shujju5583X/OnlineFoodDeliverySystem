package service;
import model.User;
import java.util.ArrayList;
import java.util.List;

public class AuthenticationService {
    private List<User> usersDatabase = new ArrayList<>();

    public void registerUser(User user) {
        usersDatabase.add(user);
        System.out.println(user.getName() + " registered successfully.");
    }

    public User login(String email, String password) {
        for (User user : usersDatabase) {
            if (user.getEmail().equals(email) && user.getPassword().equals(password)) {
                System.out.println("Login successful for: " + user.getName());
                return user;
            }
        }
        System.out.println("Invalid credentials.");
        return null;
    }
}