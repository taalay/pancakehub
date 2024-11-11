package org.pancakehub.service;

import org.junit.jupiter.api.*;
import org.pancakehub.model.order.Address;
import org.pancakehub.model.order.Order;
import org.pancakehub.repository.OrderRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.pancakehub.model.pancakes.PancakeType.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PancakeServiceTest {
    private static final String DARK_CHOCOLATE_PANCAKE_DESCRIPTION = "Delicious pancake with dark chocolate!";
    private static final String MILK_CHOCOLATE_PANCAKE_DESCRIPTION = "Delicious pancake with milk chocolate!";
    private static final String MILK_CHOCOLATE_HAZELNUTS_PANCAKE_DESCRIPTION = "Delicious pancake with milk chocolate, hazelnuts!";

    private PancakeService  pancakeService;
    private Order order;

    @BeforeAll
    void beforeAll() {
        OrderRepository orderRepository = new OrderRepository();
        pancakeService = new PancakeService(orderRepository);
    }

    @Test
    @org.junit.jupiter.api.Order(1)
    void GivenOrderDoesNotExist_WhenCreatingOrder_ThenOrderCreatedWithCorrectData_Test() {
        // setup

        // exercise
        order = pancakeService.createOrder(new Address(10, 20));

        // verify
        Address address = order.getAddress();
        assertEquals(10, address.building());
        assertEquals(20, address.room());

        // tear down
    }

    @Test
    @org.junit.jupiter.api.Order(2)
    void GivenOrderExists_WhenAddingPancakes_ThenCorrectNumberOfPancakesAdded_Test() {
        // setup

        // exercise
        addPancakes();

        // verify
        List<String> ordersPancakes = pancakeService.viewOrder(order.getId());

        assertEquals(List.of(DARK_CHOCOLATE_PANCAKE_DESCRIPTION,
                DARK_CHOCOLATE_PANCAKE_DESCRIPTION,
                DARK_CHOCOLATE_PANCAKE_DESCRIPTION,
                MILK_CHOCOLATE_PANCAKE_DESCRIPTION,
                MILK_CHOCOLATE_PANCAKE_DESCRIPTION,
                MILK_CHOCOLATE_PANCAKE_DESCRIPTION,
                MILK_CHOCOLATE_HAZELNUTS_PANCAKE_DESCRIPTION,
                MILK_CHOCOLATE_HAZELNUTS_PANCAKE_DESCRIPTION,
                MILK_CHOCOLATE_HAZELNUTS_PANCAKE_DESCRIPTION), ordersPancakes);

        // tear down
    }

    @Test
    @org.junit.jupiter.api.Order(3)
    void GivenPancakesExists_WhenRemovingPancakes_ThenCorrectNumberOfPancakesRemoved_Test() {
        // setup

        // exercise
        pancakeService.removePancakes(DARK_CHOCOLATE_PANCAKE, order.getId(), 2);
        pancakeService.removePancakes(MILK_CHOCOLATE_PANCAKE, order.getId(), 3);
        pancakeService.removePancakes(MILK_CHOCOLATE_HAZELNUT_PANCAKE, order.getId(), 1);

        // verify
        List<String> ordersPancakes = pancakeService.viewOrder(order.getId());

        assertEquals(List.of(DARK_CHOCOLATE_PANCAKE_DESCRIPTION,
                MILK_CHOCOLATE_HAZELNUTS_PANCAKE_DESCRIPTION,
                MILK_CHOCOLATE_HAZELNUTS_PANCAKE_DESCRIPTION), ordersPancakes);

        // tear down
    }

    @Test
    @org.junit.jupiter.api.Order(4)
    void GivenOrderExists_WhenCompletingOrder_ThenOrderCompleted_Test() {
        // setup

        // exercise
        pancakeService.completeOrder(order.getId());

        // verify
        List<Order> listCompletedOrders = pancakeService.listCompletedOrders();
        assertTrue(listCompletedOrders.contains(order));

        // tear down
    }

    @Test
    @org.junit.jupiter.api.Order(5)
    void GivenOrderExists_WhenPreparingOrder_ThenOrderPrepared_Test() {
        // setup

        // exercise
        pancakeService.prepareOrder(order.getId());

        // verify
        List<Order> listCompletedOrders = pancakeService.listCompletedOrders();
        assertFalse(listCompletedOrders.contains(order));
        List<Order> listPreparedOrders = pancakeService.listPreparedOrders();
        assertTrue(listPreparedOrders.contains(order));

        // tear down
    }

    @Test
    @org.junit.jupiter.api.Order(6)
    void GivenOrderExists_WhenDeliveringOrder_ThenCorrectOrderReturnedAndOrderRemovedFromTheDatabase_Test() {
        // setup
        List<String> pancakesToDeliver = pancakeService.viewOrder(order.getId());

        // exercise
        Object[] deliveredOrder = pancakeService.deliverOrder(order.getId());

        // verify
        List<Order> completedOrders = pancakeService.listCompletedOrders();
        assertFalse(completedOrders.contains(order));

        List<Order> preparedOrders = pancakeService.listPreparedOrders();
        assertFalse(preparedOrders.contains(order));

        List<String> ordersPancakes = pancakeService.viewOrder(order.getId());

        assertEquals(List.of(), ordersPancakes);
        assertEquals(order.getId(), ((Order) deliveredOrder[0]).getId());
        assertEquals(pancakesToDeliver, deliveredOrder[1]);

        // tear down
        order = null;
    }

    @Test
    @org.junit.jupiter.api.Order(7)
    void GivenOrderExists_WhenCancellingOrder_ThenOrderAndPancakesRemoved_Test() {
        // setup
        order = pancakeService.createOrder(new Address(10, 20));
        addPancakes();

        // exercise
        pancakeService.cancelOrder(order.getId());

        // verify
        List<Order> completedOrders = pancakeService.listCompletedOrders();
        assertFalse(completedOrders.contains(order));

        List<Order> preparedOrders = pancakeService.listPreparedOrders();
        assertFalse(preparedOrders.contains(order));

        List<String> ordersPancakes = pancakeService.viewOrder(order.getId());

        assertEquals(List.of(), ordersPancakes);

        // tear down
    }

    private void addPancakes() {
        pancakeService.addPancake(order.getId(), DARK_CHOCOLATE_PANCAKE, 3);
        pancakeService.addPancake(order.getId(), MILK_CHOCOLATE_PANCAKE, 3);
        pancakeService.addPancake(order.getId(), MILK_CHOCOLATE_HAZELNUT_PANCAKE, 3);
    }
}