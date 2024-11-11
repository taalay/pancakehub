package org.pancakehub.model.order;

import org.pancakehub.model.pancakes.PancakeRecipe;
import org.pancakehub.model.pancakes.PancakeType;

import java.util.List;
import java.util.UUID;

public class NullOrder implements Order {

    @Override
    public UUID getId() {
        return new UUID(0L, 0L); // Return a default UUID to represent a null order
    }

    @Override
    public void addPancake(PancakeRecipe pancake) {
        // No operation for NullOrder
    }

    @Override
    public void removePancakes(PancakeType pancakeRecipe, int count) {
        // No operation for NullOrder
    }

    @Override
    public List<String> getPancakeDescriptions() {
        return List.of(); // Return an empty list for NullOrder
    }

    @Override
    public void markAsPrepared() {
        // No operation for NullOrder
    }

    @Override
    public void markAsCompleted() {
        // No operation for NullOrder
    }

    @Override
    public void markAsDelivered() {
        // No operation for NullOrder
    }

    @Override
    public boolean isPrepared() {
        return false; // NullOrder is never prepared
    }

    @Override
    public boolean isCompleted() {
        return false; // NullOrder is never completed
    }

    @Override
    public boolean isDelivered() {
        return false;
    }

    @Override
    public Address getAddress() {
        return new Address(1, 1); // Return a default address to represent a null order
    }

    public boolean isNull() {
        return true; // Identify this as a NullOrder
    }

    @Override
    public void cancel() {
        // No operation for NullOrder
    }
}