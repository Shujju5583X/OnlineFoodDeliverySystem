package service;
import model.Delivery;
import model.DeliveryPerson;
import model.Order;
import java.util.List;

public class DeliveryService {
    private int deliveryCounter = 1;

    public Delivery assignDeliveryPerson(Order order, List<DeliveryPerson> drivers) {
        for (DeliveryPerson driver : drivers) {
            if (driver.isAvailable()) {
                driver.setAvailable(false);
                System.out.println("Driver " + driver.getName() + " assigned to Order " + order.getOrderId());
                return new Delivery(deliveryCounter++, order, driver);
            }
        }
        System.out.println("No delivery persons available.");
        return null;
    }
}