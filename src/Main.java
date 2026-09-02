import model.*;
import repository.*;
import java.util.*;

public class Main {
    private static UserRepository userRepo = new JdbcUserRepository();
    private static OrderRepository orderRepo = new JdbcOrderRepository();
    private static DeliveryRepository deliveryRepo = new JdbcDeliveryRepository();
    private static Scanner scanner = new Scanner(System.in);
    private static User loggedInUser = null;

    public static void main(String[] args) {
        System.out.println("=== Welcome to Online Food Delivery ===");
        while (true) {
            if (loggedInUser == null) {
                System.out.println("\n1. Register (Customer)\n2. Login\n3. Exit");
                System.out.print("Choose an option: ");
                int choice = Integer.parseInt(scanner.nextLine());

                if (choice == 1) register();
                else if (choice == 2) login();
                else break;
            } else if (loggedInUser instanceof Customer) {
                customerMenu();
            } else if (loggedInUser instanceof DeliveryPerson) {
                deliveryMenu();
            }
        }
    }

    private static void register() {
        System.out.print("Enter Name: "); String name = scanner.nextLine();
        System.out.print("Enter Email: "); String email = scanner.nextLine();
        System.out.print("Enter Password: "); String password = scanner.nextLine();
        System.out.print("Enter Address: "); String address = scanner.nextLine();
        System.out.print("Enter Phone Number: "); String phone = scanner.nextLine();

        Customer newCustomer = new Customer(0, name, email, password, address, phone);
        userRepo.save(newCustomer);
    }

    private static void login() {
        System.out.print("Email: "); String email = scanner.nextLine();
        System.out.print("Password: "); String pass = scanner.nextLine();
        loggedInUser = userRepo.login(email, pass);
        if (loggedInUser == null) System.out.println("Invalid credentials.");
    }

    private static void customerMenu() {
        System.out.println("\nWelcome, " + loggedInUser.getName() + "!");
        System.out.println("1. View Menu & Place Order\n2. Logout");
        System.out.print("Choose an option: ");

        if (Integer.parseInt(scanner.nextLine()) == 1) {
            List<FoodItem> menu = orderRepo.getMenu();
            if (menu.isEmpty()) {
                System.out.println("Menu is empty!");
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

                System.out.print("Enter Item ID: ");
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
                int orderId = orderRepo.placeOrder((Customer)loggedInUser, cart, totalAmount);
                if (orderId > 0) {
                    DeliveryPerson driver = deliveryRepo.findAvailableDriver();
                    if (driver != null) {
                        deliveryRepo.assignDelivery(orderId, driver.getId());
                        System.out.println("Driver " + driver.getName() + " has been assigned to your order!");
                    } else {
                        System.out.println("No drivers available right now. Your order is pending.");
                    }
                }
            } else {
                System.out.println("Cart is empty. Order cancelled.");
            }
        } else {
            loggedInUser = null;
        }
    }

    private static void deliveryMenu() {
        System.out.println("\nWelcome Delivery Partner, " + loggedInUser.getName() + "!");
        System.out.println("1. Complete Current Delivery\n2. Logout");
        System.out.print("Choose an option: ");

        if (Integer.parseInt(scanner.nextLine()) == 1) {
            deliveryRepo.completeDelivery(loggedInUser.getId());
        } else {
            loggedInUser = null;
        }
    }
}