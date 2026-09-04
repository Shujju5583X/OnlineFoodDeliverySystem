import model.*;
import repository.*;
import service.*;
import java.util.*;

public class Main {
    private static UserRepository userRepo = new JdbcUserRepository();
    private static OrderRepository orderRepo = new JdbcOrderRepository();
    private static DeliveryRepository deliveryRepo = new JdbcDeliveryRepository();

    private static AuthenticationService authService = new AuthenticationService(userRepo);
    private static PaymentService paymentService = new PaymentService();
    private static OrderService orderService = new OrderService(orderRepo, paymentService);
    private static DeliveryService deliveryService = new DeliveryService(deliveryRepo);
    private static RestaurantService restaurantService = new RestaurantService(); // Initialized to not be dead code

    private static Scanner scanner = new Scanner(System.in);
    private static User loggedInUser = null;

    public static void main(String[] args) {
        System.out.println("=== Welcome to Online Food Delivery ===");
        while (true) {
            if (loggedInUser == null) {
                System.out.println("\n1. Register\n2. Login\n3. Exit");
                System.out.print("Choose an option: ");
                int choice = Integer.parseInt(scanner.nextLine());

                if (choice == 1) register();
                else if (choice == 2) login();
                else break;
            } else if (loggedInUser instanceof Customer) {
                customerMenu();
            } else if (loggedInUser instanceof DeliveryPerson) {
                deliveryMenu();
            } else if (loggedInUser instanceof Admin) {
                adminMenu();
            } else if (loggedInUser instanceof RestaurantOwner) {
                restaurantMenu();
            }
        }
    }

    private static void register() {
        System.out.println("Select Role: 1. Customer  2. Delivery Person  3. Restaurant Owner");
        System.out.print("Choice: ");
        int roleChoice = Integer.parseInt(scanner.nextLine());

        System.out.print("Enter Name: "); String name = scanner.nextLine();
        System.out.print("Enter Email: "); String email = scanner.nextLine();
        System.out.print("Enter Password: "); String password = scanner.nextLine();

        User newUser = null;
        if (roleChoice == 1) {
            System.out.print("Enter Address: "); String address = scanner.nextLine();
            System.out.print("Enter Phone Number: "); String phone = scanner.nextLine();
            newUser = new Customer(0, name, email, password, address, phone);
        } else if (roleChoice == 2) {
            System.out.print("Enter Vehicle Number: "); String vehicle = scanner.nextLine();
            newUser = new DeliveryPerson(0, name, email, password, vehicle);
        } else if (roleChoice == 3) {
            newUser = new RestaurantOwner(0, name, email, password);
        } else {
            System.out.println("Invalid role choice.");
            return;
        }

        authService.registerUser(newUser);
    }

    private static void login() {
        System.out.println("Select Role: 1. Customer  2. Delivery Person  3. Restaurant Owner  4. Admin");
        System.out.print("Choice: ");
        int roleChoice = Integer.parseInt(scanner.nextLine());

        System.out.print("Email: "); String email = scanner.nextLine();
        System.out.print("Password: "); String pass = scanner.nextLine();
        User user = authService.login(email, pass);

        if (user != null) {
            boolean roleMatches = false;
            if (roleChoice == 1 && user instanceof Customer) roleMatches = true;
            else if (roleChoice == 2 && user instanceof DeliveryPerson) roleMatches = true;
            else if (roleChoice == 3 && user instanceof RestaurantOwner) roleMatches = true;
            else if (roleChoice == 4 && user instanceof Admin) roleMatches = true;

            if (roleMatches) {
                loggedInUser = user;
            } else {
                System.out.println("Role mismatch. You are not registered as this role.");
                loggedInUser = null;
            }
        }
    }

    private static void adminMenu() {
        System.out.println("\nWelcome Admin, " + loggedInUser.getName() + "!");
        System.out.println("1. Logout");
        System.out.print("Choose an option: ");
        if (Integer.parseInt(scanner.nextLine()) == 1) loggedInUser = null;
    }

    private static void restaurantMenu() {
        System.out.println("\nWelcome Restaurant Owner, " + loggedInUser.getName() + "!");
        System.out.println("1. View Menu\n2. Add Menu Item\n3. Edit Menu Item\n4. Delete Menu Item\n5. Logout");
        System.out.print("Choose an option: ");

        int choice;
        try {
            choice = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Invalid input.");
            return;
        }

        switch (choice) {
            case 1: {
                List<FoodItem> items = orderService.getAllMenuItems();
                if (items.isEmpty()) {
                    System.out.println("No menu items found.");
                } else {
                    System.out.println("\n--- ALL MENU ITEMS ---");
                    System.out.printf("%-6s %-25s %-10s %-10s %s%n", "ID", "Name", "Price", "Available", "Description");
                    System.out.println("-".repeat(75));
                    for (FoodItem item : items) {
                        System.out.printf("%-6d %-25s $%-9.2f %-10s %s%n",
                                item.getId(), item.getName(), item.getPrice(),
                                item.isAvailable() ? "Yes" : "No",
                                item.getDescription());
                    }
                }
                break;
            }
            case 2: {
                System.out.print("Item Name: "); String name = scanner.nextLine();
                System.out.print("Price: $"); double price;
                try { price = Double.parseDouble(scanner.nextLine().trim()); }
                catch (NumberFormatException e) { System.out.println("Invalid price."); break; }
                System.out.print("Description: "); String desc = scanner.nextLine();
                if (orderService.addMenuItem(name, price, desc)) {
                    System.out.println("Menu item '" + name + "' added successfully!");
                } else {
                    System.out.println("Failed to add menu item.");
                }
                break;
            }
            case 3: {
                List<FoodItem> items = orderService.getAllMenuItems();
                if (items.isEmpty()) { System.out.println("No items to edit."); break; }
                System.out.println("\n--- MENU ITEMS ---");
                for (FoodItem item : items) {
                    System.out.printf("[%d] %s - $%.2f (%s)%n",
                            item.getId(), item.getName(), item.getPrice(),
                            item.isAvailable() ? "Available" : "Unavailable");
                }
                System.out.print("Enter Item ID to edit: ");
                int editId;
                try { editId = Integer.parseInt(scanner.nextLine().trim()); }
                catch (NumberFormatException e) { System.out.println("Invalid ID."); break; }

                FoodItem target = items.stream().filter(i -> i.getId() == editId).findFirst().orElse(null);
                if (target == null) { System.out.println("Item not found."); break; }

                System.out.println("Current Name: " + target.getName());
                System.out.print("New Name (leave blank to keep): "); String newName = scanner.nextLine();
                if (newName.isBlank()) newName = target.getName();

                System.out.println("Current Price: $" + target.getPrice());
                System.out.print("New Price (leave blank to keep): "); String priceInput = scanner.nextLine().trim();
                double newPrice = priceInput.isBlank() ? target.getPrice() : Double.parseDouble(priceInput);

                System.out.println("Current Description: " + target.getDescription());
                System.out.print("New Description (leave blank to keep): "); String newDesc = scanner.nextLine();
                if (newDesc.isBlank()) newDesc = target.getDescription();

                System.out.print("Available? (y/n, current=" + (target.isAvailable() ? "y" : "n") + "): ");
                String availInput = scanner.nextLine().trim().toLowerCase();
                boolean newAvail = availInput.isBlank() ? target.isAvailable() : availInput.equals("y");

                if (orderService.updateMenuItem(editId, newName, newPrice, newDesc, newAvail)) {
                    System.out.println("Item updated successfully!");
                } else {
                    System.out.println("Failed to update item.");
                }
                break;
            }
            case 4: {
                List<FoodItem> items = orderService.getAllMenuItems();
                if (items.isEmpty()) { System.out.println("No items to delete."); break; }
                System.out.println("\n--- MENU ITEMS ---");
                for (FoodItem item : items) {
                    System.out.printf("[%d] %s - $%.2f%n", item.getId(), item.getName(), item.getPrice());
                }
                System.out.print("Enter Item ID to delete: ");
                int delId;
                try { delId = Integer.parseInt(scanner.nextLine().trim()); }
                catch (NumberFormatException e) { System.out.println("Invalid ID."); break; }

                System.out.print("Are you sure you want to delete item #" + delId + "? (y/n): ");
                if (scanner.nextLine().trim().equalsIgnoreCase("y")) {
                    if (orderService.deleteMenuItem(delId)) {
                        System.out.println("Item deleted successfully.");
                    } else {
                        System.out.println("Failed to delete item. Check the ID.");
                    }
                } else {
                    System.out.println("Deletion cancelled.");
                }
                break;
            }
            case 5:
                loggedInUser = null;
                break;
            default:
                System.out.println("Invalid option.");
        }
    }

    private static void customerMenu() {
        System.out.println("\nWelcome, " + loggedInUser.getName() + "!");
        System.out.println("1. View Menu & Place Order\n2. Logout");
        System.out.print("Choose an option: ");

        if (Integer.parseInt(scanner.nextLine()) == 1) {
            List<FoodItem> menu = orderService.getMenu();
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
                // We use PaymentCash as a placeholder payment method
                int orderId = orderService.placeOrder((Customer)loggedInUser, cart, totalAmount, new PaymentCash(0, totalAmount));
                if (orderId > 0) {
                    DeliveryPerson driver = deliveryService.findAvailableDriver();
                    if (driver != null) {
                        deliveryService.assignDelivery(orderId, driver.getId());
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
            deliveryService.completeDelivery(loggedInUser.getId());
        } else {
            loggedInUser = null;
        }
    }
}
