package io.github.cheese_curd.nullzone.blocks;

import io.github.cheese_curd.nullzone.Nullzone;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.Difficulty;

public class NullzoneBlock extends Block
{
	public NullzoneBlock(Settings settings) {
		super(settings);
	}

	@Override
	public float calcBlockBreakingDelta(BlockState state, PlayerEntity player, BlockView world, BlockPos pos) {
		if (player.getWorld().getRegistryKey().getRegistry().getNamespace().equals(Nullzone.MOD_ID)
				&& player.getWorld().getDifficulty() != Difficulty.EASY)
			return 0f;

		return super.calcBlockBreakingDelta(state, player, world, pos);
	}
}
