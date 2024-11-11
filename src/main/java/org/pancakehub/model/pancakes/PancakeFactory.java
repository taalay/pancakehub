package org.pancakehub.model.pancakes;

import org.pancakehub.model.exception.PancakeNotFoundException;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class PancakeFactory {

    private PancakeFactory() {}

    private static final ConcurrentMap<PancakeType, PancakeRecipe> PANCAKE_RECIPE_MAP = new ConcurrentHashMap<>();

    static {
        addPancakeRecipe(PancakeType.DARK_CHOCOLATE_PANCAKE, List.of(Ingredient.DARK_CHOCOLATE));
        addPancakeRecipe(PancakeType.DARK_CHOCOLATE_WHIPPED_CREAM_HAZELNUT_PANCAKE, List.of(Ingredient.DARK_CHOCOLATE,
                Ingredient.MUSTARD, Ingredient.WHIPPED_CREAM, Ingredient.HAZELNUTS));
        addPancakeRecipe(PancakeType.DARK_CHOCOLATE_WHIPPED_CREAM_PANCAKE, List.of(Ingredient.DARK_CHOCOLATE, Ingredient.WHIPPED_CREAM));
        addPancakeRecipe(PancakeType.MILK_CHOCOLATE_HAZELNUT_PANCAKE, List.of(Ingredient.MILK_CHOCOLATE, Ingredient.HAZELNUTS));
        addPancakeRecipe(PancakeType.MILK_CHOCOLATE_PANCAKE, List.of(Ingredient.MILK_CHOCOLATE));
    }
    public static PancakeRecipe createPancake(PancakeType pancakeType) {
        if (pancakeType == null) {
            throw new PancakeNotFoundException("PancakeType cannot be null");
        }

        PancakeRecipe recipe = PANCAKE_RECIPE_MAP.get(pancakeType);
        if (recipe == null) {
            throw new PancakeNotFoundException("Pancake recipe not found for: " + pancakeType);
        }
        return recipe;
    }
    private static void addPancakeRecipe(PancakeType pancakeMenuItem, List<Ingredient> ingredients) {
        PANCAKE_RECIPE_MAP.put(pancakeMenuItem, new PancakeRecipe(pancakeMenuItem, ingredients));
    }
}
