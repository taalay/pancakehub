package org.pancakehub;

import org.pancakehub.repository.OrderRepository;
import org.pancakehub.service.PancakeService;
import org.pancakehub.model.order.Address;
import org.pancakehub.model.order.Order;
import org.pancakehub.model.pancakes.PancakeType;

import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {

    private static final Logger LOGGER = Logger.getLogger(Main.class.getName());


    public static void main(String[] args) {
        // Initialize the PancakeService
        PancakeService pancakeService = new PancakeService(new OrderRepository());

        // Create an address for the order
        Address address = new Address(10, 20);

        // 1. Create a new order
        LOGGER.log(Level.INFO, "Creating a new order...");
        Order order = pancakeService.createOrder(address);
        UUID orderId = order.getId();
        LOGGER.log(Level.INFO, "Order created with ID: {0}", orderId);

        // 2. Add pancakes to the order
        LOGGER.log(Level.INFO, "Adding pancakes to the order...");
        pancakeService.addPancake(orderId, PancakeType.DARK_CHOCOLATE_PANCAKE, 2);
        pancakeService.addPancake(orderId, PancakeType.MILK_CHOCOLATE_PANCAKE, 3);
        pancakeService.addPancake(orderId, PancakeType.MILK_CHOCOLATE_HAZELNUT_PANCAKE, 1);

        // View the order details to confirm the added pancakes
        List<String> pancakeDescriptions = pancakeService.viewOrder(orderId);
        LOGGER.log(Level.INFO, "Order pancakes: {0}", pancakeDescriptions);

        // 3. Complete the order
        LOGGER.log(Level.INFO, "Completing the order...");
        pancakeService.completeOrder(orderId);
        LOGGER.log(Level.INFO, "Order status after completion: {0}", order.isCompleted() ? "COMPLETED" : "NOT COMPLETED");

        // 4. Prepare the order
        LOGGER.log(Level.INFO, "Preparing the order...");
        pancakeService.prepareOrder(orderId);
        LOGGER.log(Level.INFO, "Order status after preparation: {0}", order.isPrepared() ? "PREPARED" : "NOT PREPARED");

        // 5. Deliver the order
        LOGGER.log(Level.INFO, "Delivering the order...");
        Object[] deliveryResult = pancakeService.deliverOrder(orderId);

        // Check the delivery result
        if (deliveryResult.length > 0) {
            Order deliveredOrder = (Order) deliveryResult[0];
            List<String> deliveredPancakes = (List<String>) deliveryResult[1];
            LOGGER.log(Level.INFO, "Order {0} delivered with pancakes: {1}", new Object[]{deliveredOrder.getId(), deliveredPancakes});
            LOGGER.log(Level.INFO, "Order status after delivery: {0}", order.isDelivered() ? "DELIVERED" : "NOT DELIVERED");
        } else {
            LOGGER.log(Level.WARNING, "Failed to deliver the order.");
        }
    }
}