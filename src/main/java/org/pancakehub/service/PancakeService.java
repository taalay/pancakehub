package org.pancakehub.service;

import org.pancakehub.model.order.Address;
import org.pancakehub.model.pancakes.PancakeFactory;
import org.pancakehub.model.pancakes.PancakeRecipe;
import org.pancakehub.model.pancakes.PancakeType;
import org.pancakehub.repository.OrderRepository;
import org.pancakehub.model.order.Order;
import org.pancakehub.model.order.PancakeOrder;

import java.util.*;

public class PancakeService {
    private final OrderRepository orderRepository;

    public PancakeService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public Order createOrder(Address address) {
        Order order = new PancakeOrder(address);
        orderRepository.addOrder(order);
        return order;
    }

    public void addPancake(UUID orderId, PancakeType pancakeMenuItem, int count) {
        Order order = orderRepository.findById(orderId);
        for (int i = 0; i < count; i++) {
            PancakeRecipe pancake = PancakeFactory.createPancake(pancakeMenuItem);
            order.addPancake(pancake);
        }
    }

    public List<String> viewOrder(UUID orderId) {
        Order order = orderRepository.findById(orderId);
        return order.getPancakeDescriptions();
    }

    public void removePancakes(PancakeType pancakeType, UUID orderId, int count) {
        Order order = orderRepository.findById(orderId);
        order.removePancakes(pancakeType, count);
    }

    public void cancelOrder(UUID orderId) {
        Order order = orderRepository.findById(orderId);
        order.cancel();
        orderRepository.removeOrder(order);
    }

    public void completeOrder(UUID orderId) {
        Order order = orderRepository.findById(orderId);
        order.markAsCompleted();
    }

    public List<Order> listCompletedOrders() {
        return orderRepository.getCompletedOrders();
    }

    public void prepareOrder(UUID orderId) {
        Order order = orderRepository.findById(orderId);
        order.markAsPrepared();
    }

    public List<Order> listPreparedOrders() {
        return orderRepository.getPreparedOrders();
    }

    public Object[] deliverOrder(UUID orderId) {
        Order order = orderRepository.findById(orderId);
        if (order.isNull() || !order.isPrepared()) return new Object[0];

        List<String> pancakesToDeliver = order.getPancakeDescriptions();
        order.markAsDelivered();
        orderRepository.removeOrder(order);

        return new Object[]{order, pancakesToDeliver};
    }
}