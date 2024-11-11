package org.pancakehub.model.order;

import org.pancakehub.model.pancakes.PancakeRecipe;
import org.pancakehub.model.pancakes.PancakeType;

import java.util.List;
import java.util.UUID;

public interface Order {
    UUID getId();

    void addPancake(PancakeRecipe pancake);

    void removePancakes(PancakeType pancakeType, int count);

    List<String> getPancakeDescriptions();

    void markAsPrepared();

    void markAsCompleted();

    void markAsDelivered();

    boolean isPrepared();

    boolean isCompleted();

    boolean isDelivered();

    Address getAddress();

    boolean isNull();

    void cancel();
}
