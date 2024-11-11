package org.pancakehub.repository;

import org.pancakehub.model.order.NullOrder;
import org.pancakehub.model.order.Order;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class OrderRepository {
    private final Map<UUID, Order> orders = new ConcurrentHashMap<>();

    public void addOrder(Order order) {
        orders.put(order.getId(), order);
    }

    public Order findById(UUID orderId) {
        return orders.getOrDefault(orderId, new NullOrder()); // Return NullOrder if not found
    }

    public void removeOrder(Order order) {
        orders.remove(order.getId());
    }

    public List<Order> getAllOrders() {
        return List.copyOf(orders.values());
    }

    public List<Order> getPreparedOrders() {
        return orders.values().stream()
                .filter(Order::isPrepared)
                .toList();
    }

    public List<Order> getCompletedOrders() {
        return orders.values().stream()
                .filter(Order::isCompleted)
                .toList();
    }
}