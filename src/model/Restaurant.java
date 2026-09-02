package model;
import java.util.ArrayList;
import java.util.List;

public class Restaurant {
    private int id;
    private String name;
    private RestaurantOwner owner;
    private List<FoodItem> menu;

    public Restaurant(int id, String name, RestaurantOwner owner) {
        this.id = id;
        this.name = name;
        this.owner = owner;
        this.menu = new ArrayList<>();
    }

    public void addFoodItem(FoodItem item) {
        menu.add(item);
    }

    public List<FoodItem> getMenu() { return menu; }
    public String getName() { return name; }
}