import model.Customer;
import model.FoodItem;
import repository.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Main {
    private static UserRepository userRepo = new JdbcUserRepository();
    private static OrderRepository orderRepo = new JdbcOrderRepository();
    private static Scanner scanner = new Scanner(System.in);
    private static Customer loggedInUser = null;

    public static void main(String[] args) {
        boolean running = true;

        System.out.println("=== Welcome to Online Food Delivery ===");

        while (running) {
            if (loggedInUser == null) {
                System.out.println("\n1. Register\n2. Login\n3. Exit");
                System.out.print("Choose an option: ");
                int choice = Integer.parseInt(scanner.nextLine());

                switch (choice) {
                    case 1: register(); break;
                    case 2: login(); break;
                    case 3: running = false; break;
                    default: System.out.println("Invalid choice.");
                }
            } else {
                System.out.println("\nWelcome, " + loggedInUser.getName() + "!");
                System.out.println("1. View Menu & Order\n2. Logout");
                System.out.print("Choose an option: ");
                int choice = Integer.parseInt(scanner.nextLine());

                switch (choice) {
                    case 1: placeOrderFlow(); break;
                    case 2: loggedInUser = null; break;
                    default: System.out.println("Invalid choice.");
                }
            }
        }
        System.out.println("Thank you for using our app!");
    }

    private static void register() {
        System.out.print("Enter Name: ");
        String name = scanner.nextLine();
        System.out.print("Enter Email: ");
        String email = scanner.nextLine();
        System.out.print("Enter Password: ");
        String password = scanner.nextLine();
        System.out.print("Enter Address: ");
        String address = scanner.nextLine();
        System.out.print("Enter Phone Number: ");
        String phone = scanner.nextLine();

        // ID is 0 because database Auto-Increments it
        Customer newCustomer = new Customer(0, name, email, password, address, phone);
        userRepo.save(newCustomer);
    }

    private static void login() {
        System.out.print("Enter Email: ");
        String email = scanner.nextLine();
        System.out.print("Enter Password: ");
        String password = scanner.nextLine();

        loggedInUser = (Customer) userRepo.login(email, password);
        if (loggedInUser == null) {
            System.out.println("Invalid credentials.");
        }
    }

    private static void placeOrderFlow() {
        List<FoodItem> menu = orderRepo.getMenu();
        if (menu.isEmpty()) {
            System.out.println("Menu is currently empty! (Please add items to DB directly)");
            return;
        }

        Map<FoodItem, Integer> cart = new HashMap<>();
        double totalAmount = 0.0;
        boolean ordering = true;

        while (ordering) {
            System.out.println("\n--- MENU ---");
            for (FoodItem item : menu) {
                System.out.println(item.getId() + ". " + item.getName() + " - $" + item.getPrice());
            }
            System.out.println("0. Finish and Place Order");

            System.out.print("Enter Item ID to add to cart: ");
            int itemId = Integer.parseInt(scanner.nextLine());

            if (itemId == 0) {
                ordering = false;
                break;
            }

            FoodItem selectedItem = menu.stream().filter(i -> i.getId() == itemId).findFirst().orElse(null);

            if (selectedItem != null) {
                System.out.print("Enter Quantity: ");
                int qty = Integer.parseInt(scanner.nextLine());

                cart.put(selectedItem, cart.getOrDefault(selectedItem, 0) + qty);
                totalAmount += (selectedItem.getPrice() * qty);
                System.out.println(qty + "x " + selectedItem.getName() + " added to cart.");
            } else {
                System.out.println("Invalid Item ID.");
            }
        }

        if (!cart.isEmpty()) {
            orderRepo.placeOrder(loggedInUser, cart, totalAmount);
        } else {
            System.out.println("Cart is empty. Order cancelled.");
        }
    }
}
