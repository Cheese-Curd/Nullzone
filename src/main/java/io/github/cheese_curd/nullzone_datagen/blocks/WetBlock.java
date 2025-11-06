package io.github.cheese_curd.nullzone_datagen.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

public class WetBlock extends Block {
	float dripPercentage;

	public WetBlock(Settings settings, float dripPercentage) {
		super(settings);

		this.dripPercentage = dripPercentage / 100;
	}

	@Override
	public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
		if (random.nextFloat() < dripPercentage) {
			double x = pos.getX() + 0.5 + (random.nextDouble() - 0.5) * 0.3;
			double y = pos.getY() + 0.1;
			double z = pos.getZ() + 0.5 + (random.nextDouble() - 0.5) * 0.3;

			world.addParticle(ParticleTypes.DRIPPING_WATER, x, y, z, 0.0, 0.0, 0.0);
		}
	}
}
