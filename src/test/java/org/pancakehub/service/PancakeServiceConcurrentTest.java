package org.pancakehub.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.pancakehub.model.order.Address;
import org.pancakehub.model.order.Order;
import org.pancakehub.model.pancakes.PancakeType;
import org.pancakehub.repository.OrderRepository;

import java.util.*;
import java.util.concurrent.*;
import static org.junit.jupiter.api.Assertions.*;

class PancakeServiceConcurrentTest {

    private static final String DARK_CHOCOLATE_PANCAKE_DESCRIPTION = "Delicious pancake with dark chocolate!";
    private final PancakeService pancakeService = new PancakeService(new OrderRepository());
    private Order order;

    @BeforeEach
    void setup() {
        order = pancakeService.createOrder(new Address(10, 20));
    }

    @Test
    void GivenMultipleThreads_WhenAddingPancakesConcurrently_ThenPancakesAddedCorrectly() throws InterruptedException {
        // given
        int threadCount = 10;
        int pancakesPerThread = 5;
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);

        // when
        for (int i = 0; i < threadCount; i++) {
            executor.submit(() -> pancakeService.addPancake(order.getId(), PancakeType.DARK_CHOCOLATE_PANCAKE, pancakesPerThread));
        }
        executor.shutdown();
        assertTrue(executor.awaitTermination(5, TimeUnit.SECONDS));

        // then
        List<String> pancakes = pancakeService.viewOrder(order.getId());
        assertEquals(threadCount * pancakesPerThread, pancakes.size());
        pancakes.forEach(description -> assertEquals(DARK_CHOCOLATE_PANCAKE_DESCRIPTION, description));
    }

    @Test
    void GivenMultipleThreads_WhenRemovingPancakesConcurrently_ThenCorrectFinalCount() throws InterruptedException {
        // given
        int initialPancakes = 100;
        int removeThreads = 5;
        int pancakesToRemovePerThread = 10;
        ExecutorService executor = Executors.newFixedThreadPool(removeThreads);

        pancakeService.addPancake(order.getId(), PancakeType.MILK_CHOCOLATE_PANCAKE, initialPancakes);

        // when
        for (int i = 0; i < removeThreads; i++) {
            executor.submit(() -> pancakeService.removePancakes(PancakeType.MILK_CHOCOLATE_PANCAKE, order.getId(), pancakesToRemovePerThread));
        }
        executor.shutdown();
        assertTrue(executor.awaitTermination(5, TimeUnit.SECONDS));

        // then
        List<String> pancakes = pancakeService.viewOrder(order.getId());
        int expectedCount = initialPancakes - (removeThreads * pancakesToRemovePerThread);
        assertEquals(expectedCount, pancakes.size());
    }

    @Test
    void GivenMultipleThreads_WhenCancellingAndModifyingOrderConcurrently_ThenOrderIsConsistentlyCancelled() throws InterruptedException {
        // given
        ExecutorService executor = Executors.newFixedThreadPool(5);

        // when
        for (int i = 0; i < 3; i++) {
            executor.submit(() -> pancakeService.addPancake(order.getId(), PancakeType.MILK_CHOCOLATE_HAZELNUT_PANCAKE, 5));
        }
        executor.submit(() -> pancakeService.cancelOrder(order.getId()));
        executor.submit(() -> pancakeService.completeOrder(order.getId()));

        executor.shutdown();
        assertTrue(executor.awaitTermination(5, TimeUnit.SECONDS));

        // then
        List<String> pancakes = pancakeService.viewOrder(order.getId());
        assertTrue(pancakes.isEmpty());
    }
}