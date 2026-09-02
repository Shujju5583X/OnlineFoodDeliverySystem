package model;

public class Delivery {
    private int deliveryId;
    private Order order;
    private DeliveryPerson deliveryPerson;
    private String status;

    public Delivery(int deliveryId, Order order, DeliveryPerson deliveryPerson) {
        this.deliveryId = deliveryId;
        this.order = order;
        this.deliveryPerson = deliveryPerson;
        this.status = "ASSIGNED";
    }

    public void completeDelivery() {
        this.status = "DELIVERED";
        this.order.setStatus("DELIVERED");
        this.deliveryPerson.setAvailable(true);
        System.out.println("Order " + order.getOrderId() + " delivered by " + deliveryPerson.getName());
    }
}