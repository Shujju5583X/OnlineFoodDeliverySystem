package model;

public class Customer extends User {
    private String address;
    private String phoneNumber;

    public Customer(int id, String name, String email, String password, String address, String phoneNumber) {
        super(id, name, email, password);
        this.address = address;
        this.phoneNumber = phoneNumber;
    }

    // Add this method to satisfy the abstract requirement from User.java
    @Override
    public void displayRole() {
        System.out.println("Role: Customer");
    }

    public String getAddress() { return address; }
    public String getPhoneNumber() { return phoneNumber; }
}
