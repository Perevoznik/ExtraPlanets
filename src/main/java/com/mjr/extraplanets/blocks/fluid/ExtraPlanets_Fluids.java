package com.mjr.extraplanets.blocks.fluid;

import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialLiquid;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;

import com.mjr.extraplanets.Constants;

public class ExtraPlanets_Fluids {
	public static Block glowstone;
	public static Fluid glowstone_fluid;

	public static Block magma;
	public static Fluid magma_fluid;

	public static Block nitrogen;
	public static Fluid nitrogen_fluid;

	public static Block frozen_water;
	public static Fluid frozen_water_fluid;

	public static Block salt;
	public static Fluid salt_fluid;

	public static Block radioactiveWater;
	public static Fluid radioactiveWater_fluid;

	public static Block cleanWater;
	public static Fluid cleanWater_fluid;

	public static Block infectedWater;
	public static Fluid infectedWater_fluid;

	public static Block methane;
	public static Fluid methane_fluid;

	public static Block nitrogen_ice;
	public static Fluid nitrogen_ice_fluid;

	public static Material glowstone_material = new MaterialLiquid(MapColor.yellowColor);
	public static Material magma_material = new MaterialLiquid(MapColor.tntColor);
	public static Material nitrogen_material = new MaterialLiquid(MapColor.lightBlueColor);
	public static Material frozen_water_material = new MaterialLiquid(MapColor.waterColor);
	public static Material salt_material = new MaterialLiquid(MapColor.grayColor);
	public static Material methane_material = new MaterialLiquid(MapColor.greenColor);

	public static void init() {
		initFluid();
		registerBlocks();
	}

	private static void initFluid() {
		glowstone_fluid = new Fluid("glowstone_fluid", new ResourceLocation(Constants.TEXTURE_PREFIX + "blocks/fluids/glowstone_still"), new ResourceLocation(Constants.TEXTURE_PREFIX + "blocks/fluids/glowstone_flow")).setBlock(glowstone)
				.setDensity(800).setViscosity(1500);
		FluidRegistry.registerFluid(glowstone_fluid);
		glowstone = new FluidGlowstone("glowstone");

		magma_fluid = new Fluid("magma_fluid", new ResourceLocation(Constants.TEXTURE_PREFIX + "blocks/fluids/magma_still"), new ResourceLocation(Constants.TEXTURE_PREFIX + "blocks/fluids/magma_flow")).setBlock(magma).setDensity(800)
				.setViscosity(1500);
		FluidRegistry.registerFluid(magma_fluid);
		magma = new FluidMagma("magma");

		nitrogen_fluid = new Fluid("nitrogen_fluid", new ResourceLocation(Constants.TEXTURE_PREFIX + "blocks/fluids/nitrogen_still"), new ResourceLocation(Constants.TEXTURE_PREFIX + "blocks/fluids/nitrogen_flow")).setBlock(nitrogen).setDensity(800)
				.setViscosity(1500);
		FluidRegistry.registerFluid(nitrogen_fluid);
		nitrogen = new FluidNitrogen("nitrogen");

		frozen_water_fluid = new Fluid("frozen_water_fluid", new ResourceLocation(Constants.TEXTURE_PREFIX + "blocks/fluids/frozen_water_still"), new ResourceLocation(Constants.TEXTURE_PREFIX + "blocks/fluids/frozen_water_flow"))
				.setBlock(frozen_water).setDensity(800).setViscosity(1500);
		FluidRegistry.registerFluid(frozen_water_fluid);
		frozen_water = new FluidFrozenWater("frozen_water");

		salt_fluid = new Fluid("salt_fluid", new ResourceLocation(Constants.TEXTURE_PREFIX + "blocks/fluids/salt_still"), new ResourceLocation(Constants.TEXTURE_PREFIX + "blocks/fluids/salt_flow")).setBlock(salt).setDensity(800).setViscosity(1500);
		FluidRegistry.registerFluid(salt_fluid);
		salt = new FluidSalt("salt");

		radioactiveWater_fluid = new Fluid("radioactive_water_fluid", new ResourceLocation(Constants.TEXTURE_PREFIX + "blocks/fluids/radioactive_water_still"), new ResourceLocation(Constants.TEXTURE_PREFIX + "blocks/fluids/radioactive_water_flow"))
				.setBlock(radioactiveWater).setDensity(800).setViscosity(1500);
		FluidRegistry.registerFluid(radioactiveWater_fluid);
		radioactiveWater = new FluidRadioactiveWater("radioactive_water");

		cleanWater_fluid = new Fluid("clean_water_fluid", new ResourceLocation(Constants.TEXTURE_PREFIX + "blocks/fluids/clean_water_still"), new ResourceLocation(Constants.TEXTURE_PREFIX + "blocks/fluids/clean_water_flow")).setBlock(cleanWater)
				.setDensity(800).setViscosity(1500);
		FluidRegistry.registerFluid(cleanWater_fluid);
		cleanWater = new FluidCleanWater("clean_water");

		infectedWater_fluid = new Fluid("infected_water_fluid", new ResourceLocation(Constants.TEXTURE_PREFIX + "blocks/fluids/infected_water_still"), new ResourceLocation(Constants.TEXTURE_PREFIX + "blocks/fluids/infected_water_flow"))
				.setBlock(infectedWater).setDensity(800).setViscosity(1500);
		FluidRegistry.registerFluid(infectedWater_fluid);
		infectedWater = new FluidInfectedWater("infected_water");

		methane_fluid = new Fluid("methane_fluid", new ResourceLocation(Constants.TEXTURE_PREFIX + "blocks/fluids/methane_still"), new ResourceLocation(Constants.TEXTURE_PREFIX + "blocks/fluids/methane_flow")).setBlock(methane).setDensity(800)
				.setViscosity(1500);
		FluidRegistry.registerFluid(methane_fluid);
		methane = new FluidMethane("methane");

		nitrogen_ice_fluid = new Fluid("nitrogen_ice_fluid", new ResourceLocation(Constants.TEXTURE_PREFIX + "blocks/fluids/nitrogen_ice_still"), new ResourceLocation(Constants.TEXTURE_PREFIX + "blocks/fluids/nitrogen_ice_flow"))
				.setBlock(nitrogen_ice).setDensity(800).setViscosity(1500);
		FluidRegistry.registerFluid(nitrogen_ice_fluid);
		nitrogen_ice = new FluidNitrogenIce("nitrogen_ice");
	}

	private static void registerBlocks() {
		GameRegistry.registerBlock(glowstone, glowstone.getUnlocalizedName().substring(5));
		GameRegistry.registerBlock(magma, magma.getUnlocalizedName().substring(5));
		GameRegistry.registerBlock(nitrogen, nitrogen.getUnlocalizedName().substring(5));
		GameRegistry.registerBlock(frozen_water, frozen_water.getUnlocalizedName().substring(5));
		GameRegistry.registerBlock(salt, salt.getUnlocalizedName().substring(5));
		GameRegistry.registerBlock(radioactiveWater, radioactiveWater.getUnlocalizedName().substring(5));
		GameRegistry.registerBlock(cleanWater, cleanWater.getUnlocalizedName().substring(5));
		GameRegistry.registerBlock(infectedWater, infectedWater.getUnlocalizedName().substring(5));
		GameRegistry.registerBlock(methane, methane.getUnlocalizedName().substring(5));
		GameRegistry.registerBlock(nitrogen_ice, nitrogen_ice.getUnlocalizedName().substring(5));
	}
}
