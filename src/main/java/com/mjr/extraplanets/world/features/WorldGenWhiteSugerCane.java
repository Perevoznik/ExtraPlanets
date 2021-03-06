package com.mjr.extraplanets.world.features;

import java.util.Random;

import com.mjr.extraplanets.blocks.ExtraPlanets_Blocks;
import com.mjr.extraplanets.blocks.fluid.ExtraPlanets_Fluids;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

public class WorldGenWhiteSugerCane extends WorldGenerator {
	@Override
	public boolean generate(World world, Random rand, BlockPos position) {
		for (int i = 0; i < 20; ++i) {
			BlockPos blockpos = position.add(rand.nextInt(4) - rand.nextInt(4), 0, rand.nextInt(4) - rand.nextInt(4));

			if (world.isAirBlock(blockpos)) {
				BlockPos blockpos1 = blockpos.down();

				if (world.getBlockState(blockpos1.west()).getMaterial() == ExtraPlanets_Fluids.CHOCOLATE_MATERIAL || world.getBlockState(blockpos1.east()).getMaterial() == ExtraPlanets_Fluids.CHOCOLATE_MATERIAL
						|| world.getBlockState(blockpos1.north()).getMaterial() == ExtraPlanets_Fluids.CHOCOLATE_MATERIAL || world.getBlockState(blockpos1.south()).getMaterial() == ExtraPlanets_Fluids.CHOCOLATE_MATERIAL) {
					int j = 2 + rand.nextInt(rand.nextInt(3) + 1);

					for (int k = 0; k < j; ++k) {
						if (ExtraPlanets_Blocks.WHITE_SUGAR_CANE.canBlockStay(world, blockpos)) {
							world.setBlockState(blockpos.up(k), ExtraPlanets_Blocks.WHITE_SUGAR_CANE.getDefaultState(), 2);
						}
					}
				}
			}
		}

		return true;
	}
}