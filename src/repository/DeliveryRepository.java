package repository;
import model.DeliveryPerson;

public interface DeliveryRepository {
    DeliveryPerson findAvailableDriver();
    void assignDelivery(int orderId, int driverId);
    void completeDelivery(int driverId);
}