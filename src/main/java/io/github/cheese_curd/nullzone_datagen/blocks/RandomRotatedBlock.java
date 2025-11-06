package io.github.cheese_curd.nullzone_datagen.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.util.math.Direction;

public class RandomRotatedBlock extends Block {
	public static final DirectionProperty FACING = DirectionProperty.of("facing", Direction.Type.HORIZONTAL);

	public RandomRotatedBlock(Settings settings) {
		super(settings);
		setDefaultState(getStateManager().getDefaultState().with(FACING, Direction.NORTH));
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(FACING);
	}

	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		Direction randomDirection = Direction.Type.HORIZONTAL.random(ctx.getWorld().getRandom());
		return getDefaultState().with(FACING, randomDirection);
	}
}
