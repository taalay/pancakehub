package org.pancakehub.model.order;

import org.pancakehub.model.exception.InvalidAddressException;
import org.pancakehub.model.exception.OrderStateException;
import org.pancakehub.model.pancakes.PancakeRecipe;
import org.pancakehub.model.pancakes.PancakeType;

import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PancakeOrder implements Order {

    private static final Logger LOGGER = Logger.getLogger(PancakeOrder.class.getName());

    private final UUID id;
    private final Address address;
    private final List<PancakeRecipe> pancakes;
    private volatile OrderStatus status;
    private final Lock statusLock = new ReentrantLock();
    private final Lock pancakesLock = new ReentrantLock();
    public PancakeOrder(Address address) {
        if (address == null) {
            throw new InvalidAddressException("Address cannot be null.");
        }
        this.id = UUID.randomUUID();
        this.pancakes = Collections.synchronizedList(new ArrayList<>());
        this.address = address;
        this.status = OrderStatus.NEW;
    }

    @Override
    public UUID getId() {
        return id;
    }

    @Override
    public void addPancake(PancakeRecipe pancake) {
        pancakesLock.lock();
        try {
            pancakes.add(pancake);
            LOGGER.log(Level.INFO,
                    "Added pancake with description [{0}] to order {1} containing {2} pancakes, for building {3}, room {4}.",
                    new Object[]{pancake, id, pancakes.size(), address.building(), address.room()});
        } finally {
            pancakesLock.unlock();
        }
    }

    public void removePancakes(PancakeType pancakeType, int count) {

        pancakesLock.lock();
        try {
            Iterator<PancakeRecipe> iterator = pancakes.iterator();
            int removedCount = 0;
            while (iterator.hasNext() && removedCount < count) {
                PancakeRecipe pancake = iterator.next();
                if (pancake.pancakeType() == pancakeType) {
                    iterator.remove();
                    removedCount++;
                }
            }

            if (removedCount > 0) {
                LOGGER.log(Level.INFO,
                        "Removed [{0}] pancake(s) of type [{1}] from order [{2}], now containing [{3}] pancakes, for building [{4}], room [{5}].",
                        new Object[]{removedCount, pancakeType, id, pancakes.size(), address.building(), address.room()});
            }
        } finally {
            pancakesLock.unlock();
        }
    }

    @Override
    public List<String> getPancakeDescriptions() {
        return pancakes.stream().map(PancakeRecipe::toString).toList();
    }

    @Override
    public void markAsCompleted() {
        statusLock.lock();
        try {
            if (OrderStatus.NEW != status) {
                throw new OrderStateException("Cannot complete an order that is not new.");
            }
            status = OrderStatus.COMPLETED;
            LOGGER.log(Level.INFO,
                    "Order {0} for building {1}, room {2} marked as completed.",
                    new Object[]{id, address.building(), address.room()});
        } finally {
            statusLock.unlock();
        }

    }

    @Override
    public void markAsPrepared() {
        statusLock.lock();
        try {
            if (OrderStatus.COMPLETED != status) {
                throw new OrderStateException("Cannot prepare an order that is not completed.");
            }
            status = OrderStatus.PREPARED;
            LOGGER.log(Level.INFO,
                    "Order {0} for building {1}, room {2} marked as prepared.",
                    new Object[]{id, address.building(), address.room()});
        } finally {
            statusLock.unlock();
        }

    }
    
    @Override
    public void markAsDelivered() {
        statusLock.lock();
        try {
            if (OrderStatus.PREPARED != status) {
                throw new OrderStateException("Cannot deliver an order that is not prepared.");
            }
            status = OrderStatus.DELIVERED;
            LOGGER.log(Level.INFO,
                    "Order {0} with {1} pancakes for building {2}, room {3} out for delivery.",
                    new Object[]{id, pancakes.size(), address.building(), address.room()});
        }finally {
            statusLock.unlock();
        }
    }

    @Override
    public boolean isPrepared() {
        return OrderStatus.PREPARED == status;
    }

    @Override
    public boolean isCompleted() {
        return OrderStatus.COMPLETED == status;
    }

    @Override
    public boolean isDelivered() {
        return OrderStatus.DELIVERED == status;
    }

    @Override
    public Address getAddress() {
        return address;
    }

    @Override
    public boolean isNull() {
        return false;
    }

    @Override
    public void cancel() {
        statusLock.lock();
        try {
            status = OrderStatus.CANCELLED;
            LOGGER.log(Level.INFO,
                    "Cancelled order {0} with {1} pancakes for building {2}, room {3}.",
                    new Object[]{id, pancakes.size(), address.building(), address.room()});
        } finally {
            statusLock.unlock();
        }

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PancakeOrder that = (PancakeOrder) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}