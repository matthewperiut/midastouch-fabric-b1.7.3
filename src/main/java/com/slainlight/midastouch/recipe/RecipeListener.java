package com.slainlight.midastouch.recipe;

import com.slainlight.midastouch.item.ItemListener;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.minecraft.block.BlockBase;
import net.minecraft.item.ItemInstance;
import net.modificationstation.stationapi.api.event.recipe.RecipeRegisterEvent;
import net.modificationstation.stationapi.api.recipe.CraftingRegistry;

import java.util.Objects;

public class RecipeListener
{
    @EventListener
    public void registerRecipes(RecipeRegisterEvent event)
    {
        RecipeRegisterEvent.Vanilla type = RecipeRegisterEvent.Vanilla.fromType(event.recipeId);
        if (Objects.requireNonNull(type) == RecipeRegisterEvent.Vanilla.CRAFTING_SHAPED)
        {
            CraftingRegistry.addShapedRecipe(new ItemInstance(ItemListener.MIDAS_GLOVE, 1), "###", "# #", "###", '#', BlockBase.GOLD_BLOCK);
        }
    }
}
