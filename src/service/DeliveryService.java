package service;
import model.DeliveryPerson;
import repository.DeliveryRepository;

public class DeliveryService {
    private DeliveryRepository deliveryRepository;

    public DeliveryService(DeliveryRepository deliveryRepository) {
        this.deliveryRepository = deliveryRepository;
    }

    public DeliveryPerson findAvailableDriver() {
        return deliveryRepository.findAvailableDriver();
    }

    public void assignDelivery(int orderId, int driverId) {
        deliveryRepository.assignDelivery(orderId, driverId);
    }

    public void completeDelivery(int driverId) {
        deliveryRepository.completeDelivery(driverId);
    }
}