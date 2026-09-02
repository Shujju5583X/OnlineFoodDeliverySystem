package model;

public class OrderItem {
    private FoodItem foodItem;
    private int quantity;

    public OrderItem(FoodItem foodItem, int quantity) {
        this.foodItem = foodItem;
        this.quantity = quantity;
    }

    public double getSubtotal() {
        return foodItem.getPrice() * quantity;
    }

    public String getDetails() {
        return foodItem.getName() + " x" + quantity;
    }
}