package io.github.cheese_curd.nullzone.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.random.RandomGenerator;
import net.minecraft.world.World;

public abstract class AbstractToggledBlock extends Block {
	public static final BooleanProperty ON = BooleanProperty.of("on");
	public static final BooleanProperty POWERED = BooleanProperty.of("powered");

	public AbstractToggledBlock(Settings settings) {
		super(settings.luminance(state -> state.get(ON) ? 15 : 0));
		setDefaultState(getStateManager().getDefaultState()
			.with(ON, false)
			.with(POWERED, false));
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(ON, POWERED);
	}

	@Override
	public void neighborUpdate(BlockState state, World world, BlockPos pos, Block block, BlockPos fromPos, boolean notify) {
		if (world.isClient) return;

		boolean isPowered = world.isReceivingRedstonePower(pos);
		boolean wasPowered = state.get(POWERED);

		if (isPowered != wasPowered) {
			world.setBlockState(pos, state.with(POWERED, isPowered), Block.NOTIFY_ALL);

			if (isPowered && !wasPowered) {
				world.scheduleBlockTick(pos, this, 1);
			}
		}
	}

	@Override
	public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, RandomGenerator random) {
		boolean isPowered = state.get(POWERED);

		if (isPowered) {
			world.setBlockState(pos, state.cycle(ON), Block.NOTIFY_ALL);
		}
	}
}
