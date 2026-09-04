package model;

public class RestaurantOwner extends User {
    public RestaurantOwner(int id, String name, String email, String password) {
        super(id, name, email, password);
    }

    @Override
    public void displayRole() {
        System.out.println("Role: Restaurant Owner");
    }
}
