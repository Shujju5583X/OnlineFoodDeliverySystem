package model;

public class FoodItem {
    private int id;
    private String name;
    private double price;
    private String description;
    private boolean available;

    // Constructor used by customer menu (no description/availability needed)
    public FoodItem(int id, String name, double price) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.description = "";
        this.available = true;
    }

    // Full constructor used by restaurant owner
    public FoodItem(int id, String name, double price, String description, boolean available) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.description = description;
        this.available = available;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public double getPrice() { return price; }
    public String getDescription() { return description; }
    public boolean isAvailable() { return available; }
}