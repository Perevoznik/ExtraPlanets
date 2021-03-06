package com.mjr.extraplanets.planets.Kepler22b.worldgen.features;

import java.util.Random;

import com.mjr.extraplanets.blocks.planetAndMoonBlocks.Kepler22b.BlockBasicKepler22bTallGrass;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

public class WorldGenKepler22bFlowers extends WorldGenerator {
	private BlockBasicKepler22bTallGrass flowerBlock;
	private int flowerBlockMeta;

	public WorldGenKepler22bFlowers(BlockBasicKepler22bTallGrass flowerBlock, int flowerBlockMeta) {
		this.flowerBlock = flowerBlock;
		this.flowerBlockMeta = flowerBlockMeta;
	}

	@Override
	public boolean generate(World world, Random rand, BlockPos position) {
		for (int l = 0; l < 64; ++l) {
			BlockPos blockpos = position.add(rand.nextInt(8) - rand.nextInt(8), rand.nextInt(4) - rand.nextInt(4), rand.nextInt(8) - rand.nextInt(8));
			IBlockState state = this.flowerBlock.getStateFromMeta(this.flowerBlockMeta);

			if (world.isAirBlock(blockpos) && (!world.provider.hasSkyLight() || blockpos.getY() < 255) && this.flowerBlock.canBlockStay(world, blockpos, state)) {
				world.setBlockState(blockpos, state, 2);
			}
		}

		return true;
	}
}