package org.pancakehub.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.pancakehub.model.order.Address;
import org.pancakehub.model.order.PancakeOrder;
import org.pancakehub.model.order.Order;
import java.util.List;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class OrderRepositoryTest {
    private OrderRepository orderRepository;

    @BeforeEach
    void setUp() {
        orderRepository = new OrderRepository();
    }

    @Test
    void GivenNewOrder_WhenAddingOrder_ThenOrderIsRetrievableById_Test() {
        Order order = createOrder();
        orderRepository.addOrder(order);

        Order foundOrder = orderRepository.findById(order.getId());
        assertEquals(order, foundOrder);
    }

    @Test
    void GivenNonExistentOrderId_WhenFindingOrderById_ThenNullOrderIsReturned_Test() {
        UUID nonExistentId = UUID.randomUUID();
        Order result = orderRepository.findById(nonExistentId);

        assertTrue(result.isNull());
    }

    @Test
    void GivenExistingOrder_WhenRemovingOrder_ThenOrderIsNoLongerRetrievable_Test() {
        Order order = createOrder();
        orderRepository.addOrder(order);

        orderRepository.removeOrder(order);
        Order foundOrder = orderRepository.findById(order.getId());

        assertTrue(foundOrder.isNull());
    }

    @Test
    void GivenMultipleOrders_WhenGettingAllOrders_ThenAllOrdersAreReturned_Test() {
        Order order1 = createOrder();
        Order order2 = createOrder();
        orderRepository.addOrder(order1);
        orderRepository.addOrder(order2);

        List<Order> allOrders = orderRepository.getAllOrders();
        assertEquals(2, allOrders.size());
        assertTrue(allOrders.contains(order1));
        assertTrue(allOrders.contains(order2));
    }

    @Test
    void GivenOrdersWithSomePrepared_WhenGettingPreparedOrders_ThenOnlyPreparedOrdersAreReturned_Test() {
        Order preparedOrder = createOrder();
        preparedOrder.markAsCompleted();
        preparedOrder.markAsPrepared();
        Order unpreparedOrder = createOrder();

        orderRepository.addOrder(preparedOrder);
        orderRepository.addOrder(unpreparedOrder);

        List<Order> preparedOrders = orderRepository.getPreparedOrders();
        assertEquals(1, preparedOrders.size());
        assertTrue(preparedOrders.contains(preparedOrder));
        assertFalse(preparedOrders.contains(unpreparedOrder));
    }

    @Test
    void GivenOrdersWithSomeCompleted_WhenGettingCompletedOrders_ThenOnlyCompletedOrdersAreReturned_Test() {
        Order completedOrder = createOrder();
        completedOrder.markAsCompleted();
        Order incompleteOrder = createOrder();

        orderRepository.addOrder(completedOrder);
        orderRepository.addOrder(incompleteOrder);

        List<Order> completedOrders = orderRepository.getCompletedOrders();
        assertEquals(1, completedOrders.size());
        assertTrue(completedOrders.contains(completedOrder));
        assertFalse(completedOrders.contains(incompleteOrder));
    }

    private static PancakeOrder createOrder() {
        return new PancakeOrder(new Address(1, 101));
    }
}