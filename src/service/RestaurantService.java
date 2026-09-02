package service;
import model.Restaurant;
import model.FoodItem;
import java.util.ArrayList;
import java.util.List;

public class RestaurantService {
    private List<Restaurant> restaurants = new ArrayList<>();

    public void addRestaurant(Restaurant restaurant) {
        restaurants.add(restaurant);
    }

    public void displayMenu(Restaurant restaurant) {
        System.out.println("--- Menu for " + restaurant.getName() + " ---");
        for (FoodItem item : restaurant.getMenu()) {
            System.out.println(item.getName() + " - $" + item.getPrice());
        }
    }
}