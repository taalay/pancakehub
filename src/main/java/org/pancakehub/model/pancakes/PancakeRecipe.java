package org.pancakehub.model.pancakes;

import java.util.List;

public record PancakeRecipe(PancakeType pancakeType, List<Ingredient> ingredients) {

    @Override
    public List<Ingredient> ingredients() {
        return List.copyOf(ingredients);
    }

    public String toString() {
        StringBuilder description = new StringBuilder("Delicious pancake with ");
        for (Ingredient ingredient : ingredients) {
            description.append(ingredient.name().toLowerCase().replace('_', ' ')).append(", ");
        }
        if (!description.isEmpty()) {
            description.setLength(description.length() - 2);
        }
        description.append("!");
        return description.toString();
    }
}