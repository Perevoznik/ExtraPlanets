package com.mjr.extraplanets.planets.Kepler22b.worldgen.layer;

import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.GenLayerRiver;
import net.minecraft.world.gen.layer.GenLayerVoronoiZoom;
import net.minecraft.world.gen.layer.GenLayerZoom;

public class GenLayerKepler22b extends GenLayer {
	public GenLayerKepler22b(long seed) {
		super(seed);
	}

	public static GenLayer[] makeTheWorld(long seed) {
		GenLayer biomes = new GenLayerKepler22bBiomes(1L);
		biomes = new GenLayerZoom(1000L, biomes);
		biomes = new GenLayerZoom(1001L, biomes);
		biomes = new GenLayerZoom(1002L, biomes);
		biomes = new GenLayerZoom(1003L, biomes);
		biomes = new GenLayerZoom(1004L, biomes);
		biomes = new GenLayerZoom(1005L, biomes);
		GenLayer genlayervoronoizoom = new GenLayerVoronoiZoom(10L, biomes);
		GenLayerRiver genlayerriver = new GenLayerRiver(1000L, biomes);
		biomes.initWorldGenSeed(seed);
		genlayervoronoizoom.initWorldGenSeed(seed);
		return new GenLayer[] { biomes, genlayervoronoizoom, genlayerriver };
	}

	@Override
	public int[] getInts(int x, int z, int width, int depth) {
		return null;
	}
}
