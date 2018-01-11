package com.mjr.extraplanets.jei.densifier;

import java.util.List;

import javax.annotation.Nonnull;

import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.item.ItemStack;

public class DensifierRecipeWrapper implements IRecipeWrapper {
	@Nonnull
	private final List<ItemStack> input;
	@Nonnull
	private final ItemStack output;

	public DensifierRecipeWrapper(@Nonnull List<ItemStack> input, @Nonnull ItemStack output) {
		this.input = input;
		this.output = output;
	}

	@Override
	public void getIngredients(IIngredients ingredients) {
		ingredients.setInputs(ItemStack.class, this.input);
		ingredients.setOutput(ItemStack.class, this.output);
	}
}
