package org.pancakehub.model.pancakes;

import org.junit.jupiter.api.Test;
import org.pancakehub.model.exception.PancakeNotFoundException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PancakeFactoryTest {

    @Test
    void GivenDarkChocolatePancake_WhenCreatingPancake_ThenCorrectIngredientsAreReturned_Test() {
        PancakeRecipe recipe = PancakeFactory.createPancake(PancakeType.DARK_CHOCOLATE_PANCAKE);
        assertNotNull(recipe);
        assertEquals(List.of(Ingredient.DARK_CHOCOLATE), recipe.ingredients());
    }

    @Test
    void GivenDarkChocolateWhippedCreamPancake_WhenCreatingPancake_ThenCorrectIngredientsAreReturned_Test() {
        PancakeRecipe recipe = PancakeFactory.createPancake(PancakeType.DARK_CHOCOLATE_WHIPPED_CREAM_PANCAKE);
        assertNotNull(recipe);
        assertEquals(List.of(Ingredient.DARK_CHOCOLATE, Ingredient.WHIPPED_CREAM), recipe.ingredients());
    }

    @Test
    void GivenMilkChocolatePancake_WhenCreatingPancake_ThenCorrectIngredientsAreReturned_Test() {
        PancakeRecipe recipe = PancakeFactory.createPancake(PancakeType.MILK_CHOCOLATE_PANCAKE);
        assertNotNull(recipe);
        assertEquals(List.of(Ingredient.MILK_CHOCOLATE), recipe.ingredients());
    }

    @Test
    void GivenMilkChocolateWhippedCreamHazelnutPancake_WhenCreatingPancake_ThenCorrectIngredientsAreReturned_Test() {
        PancakeRecipe recipe = PancakeFactory.createPancake(PancakeType.MILK_CHOCOLATE_HAZELNUT_PANCAKE);
        assertNotNull(recipe);
        assertEquals(List.of(Ingredient.MILK_CHOCOLATE, Ingredient.HAZELNUTS), recipe.ingredients());
    }

    @Test
    void GivenUnknownPancake_WhenCreatingPancake_ThenPanckakeNotFoundExceptionIsThrown_Test() {
        assertThrows(PancakeNotFoundException.class, () -> PancakeFactory.createPancake(null));
    }
}