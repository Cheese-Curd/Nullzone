package io.github.cheese_curd.nullzone.blocks;

import net.minecraft.block.BlockState;
import net.minecraft.block.SlabBlock;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.random.RandomGenerator;
import net.minecraft.world.World;

public class WetSlab extends SlabBlock {
	float dripPercentage;

	public WetSlab(Settings settings, float dripPercentage) {
		super(settings);

		this.dripPercentage = dripPercentage / 100;
	}

	@Override
	public void randomDisplayTick(BlockState state, World world, BlockPos pos, RandomGenerator random) {
		if (random.nextFloat() < dripPercentage) {
			double x = pos.getX() + 0.5 + (random.nextDouble() - 0.5) * 0.3;
			double y = pos.getY() + 0.1;
			double z = pos.getZ() + 0.5 + (random.nextDouble() - 0.5) * 0.3;

			world.addParticle(ParticleTypes.DRIPPING_WATER, x, y, z, 0.0, 0.0, 0.0);
		}
	}
}
