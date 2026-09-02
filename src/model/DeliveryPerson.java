package model;

public class DeliveryPerson extends User {
    private String vehicleNumber;
    private boolean isAvailable;

    public DeliveryPerson(int id, String name, String email, String password, String vehicleNumber) {
        super(id, name, email, password);
        this.vehicleNumber = vehicleNumber;
        this.isAvailable = true;
    }

    @Override
    public void displayRole() {
        System.out.println("Role: Delivery Partner");
    }

    public boolean isAvailable() { return isAvailable; }
    public void setAvailable(boolean available) { isAvailable = available; }
}