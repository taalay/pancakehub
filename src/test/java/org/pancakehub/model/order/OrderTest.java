package org.pancakehub.model.order;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.pancakehub.model.exception.OrderStateException;
import org.pancakehub.model.pancakes.PancakeRecipe;
import org.pancakehub.model.pancakes.PancakeFactory;
import org.pancakehub.model.pancakes.PancakeType;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class OrderTest {

    private Order order;

    @BeforeEach
    void setup() {
        order = new PancakeOrder(new Address(1, 101));
    }

    @Test
    void GivenNewOrder_WhenCreated_ThenOrderHasUniqueId() {
        Order anotherOrder = new PancakeOrder(new Address(2, 202));
        assertNotEquals(order.getId(), anotherOrder.getId());
    }

    @Test
    void GivenOrderWithPancakes_WhenAddingPancake_ThenPancakeIsAdded() {
        PancakeRecipe pancake = createPancake(PancakeType.DARK_CHOCOLATE_WHIPPED_CREAM_HAZELNUT_PANCAKE);
        order.addPancake(pancake);

        List<String> descriptions = order.getPancakeDescriptions();
        assertEquals(1, descriptions.size());
        assertEquals(pancake.toString(), descriptions.get(0));
    }

    @Test
    void GivenOrderWithMultiplePancakes_WhenRemovingSomePancakes_ThenCorrectNumberOfPancakesIsRemoved() {
        PancakeRecipe pancake = createPancake(PancakeType.DARK_CHOCOLATE_PANCAKE);
        order.addPancake(pancake);
        order.addPancake(pancake);
        order.addPancake(pancake);

        order.removePancakes(pancake.pancakeType(), 2);
        List<String> descriptions = order.getPancakeDescriptions();
        assertEquals(1, descriptions.size());
    }

    @Test
    void GivenNewOrder_WhenMarkedAsCompleted_ThenOrderIsCompleted() {
        order.markAsCompleted();
        assertTrue(order.isCompleted());
    }

    @Test
    void GivenCompletedOrder_WhenMarkedAsPrepared_ThenOrderIsPrepared() {
        order.markAsCompleted();
        order.markAsPrepared();
        assertTrue(order.isPrepared());
    }

    @Test
    void GivenOrderNotCompleted_WhenMarkedAsPrepared_ThenExceptionIsThrown() {
        assertThrows(OrderStateException.class, order::markAsPrepared);
        assertFalse(order.isPrepared());
    }

    @Test
    void GivenPreparedOrder_WhenMarkedAsDelivered_ThenOrderIsDelivered() {
        order.markAsCompleted();
        order.markAsPrepared();
        order.markAsDelivered();
        assertTrue(order.isDelivered());
    }

    @Test
    void GivenOrderNotPrepared_WhenMarkedAsDelivered_ThenExceptionIsThrown() {
        order.markAsCompleted();
        assertThrows(OrderStateException.class, order::markAsDelivered);
        assertFalse(order.isDelivered()); // Ensure status didn't incorrectly change
    }

    @Test
    void GivenOrder_WhenCancelledAndMarkedAsCompleted_ThenExceptionIsThrown() {
        order.cancel();
        assertThrows(OrderStateException.class, order::markAsCompleted);
    }

    @Test
    void GivenOrder_WhenCheckedForNull_ThenReturnsFalse() {
        assertFalse(order.isNull());
    }

    private static PancakeRecipe createPancake(PancakeType pancakeType) {
        return PancakeFactory.createPancake(pancakeType);
    }
}