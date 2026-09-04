package service;
import model.User;
import repository.UserRepository;

public class AuthenticationService {
    private UserRepository userRepository;

    public AuthenticationService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void registerUser(User user) {
        userRepository.save(user);
        System.out.println(user.getName() + " registration attempted.");
    }

    public User login(String email, String password) {
        User user = userRepository.login(email, password);
        if (user != null) {
            System.out.println("Login successful for: " + user.getName());
        } else {
            System.out.println("Invalid credentials.");
        }
        return user;
    }
}