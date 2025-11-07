package io.github.cheese_curd.nullzone.blocks;

import io.github.cheese_curd.nullzone.Nullzone;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.Difficulty;
import net.minecraft.world.World;

public class NullzoneBlock extends Block
{
	public NullzoneBlock(Settings settings) {
		super(settings);
	}

	boolean cannotBreak(PlayerEntity player)
	{
		return (player.getWorld().getRegistryKey().getRegistry().getNamespace().equals(Nullzone.MOD_ID)
			&& player.getWorld().getDifficulty() != Difficulty.EASY);
	}

	@Override
	public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
		if (cannotBreak(player))
		{
			world.setBlockState(pos, state, 3);
			return;
		}

		super.onBreak(world, pos, state, player);
	}
}
