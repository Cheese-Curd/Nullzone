package io.github.cheese_curd.nullzone.world.chunkgen;

import io.github.cheese_curd.nullzone.ModBlocks;
import net.ludocrypt.limlib.api.world.LimlibHelper;
import net.ludocrypt.limlib.api.world.maze.MazeComponent;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.random.RandomGenerator;
import net.minecraft.world.ChunkRegion;

import java.util.Optional;

public class ChunkGenBase
{
	public static String getCellDir(MazeComponent.CellState state, MazeComponent.Vec2i mazePos, MazeComponent maze) {
		StringBuilder dir = new StringBuilder();

		if (state.goesLeft())  dir.append('n');
		if (state.goesUp())    dir.append('e');
		if (state.goesRight()) dir.append('s');
		if (state.goesDown())  dir.append('w');

		return dir.toString();
	}

	public void modifyStructure(ChunkRegion region, BlockPos pos, BlockState state, Optional<NbtCompound> blockEntityNbt)
	{
		if (state.isOf(Blocks.STRUCTURE_BLOCK))
			region.setBlockState(pos, Blocks.AIR.getDefaultState(), Block.NOTIFY_ALL, 1);

		// Wet Blocks
		RandomGenerator random = RandomGenerator.createLegacy(region.getSeed() + LimlibHelper.blockSeed(pos));
		if (state.isOf(ModBlocks.CEILING))
			if (random.nextDouble() < 0.2)
				region.setBlockState(pos, ModBlocks.WET_CEILING.getDefaultState(), Block.NOTIFY_ALL, 1);
		if (state.isOf(ModBlocks.CEILING_TILE))
			if (random.nextDouble() < 0.2)
				region.setBlockState(pos, ModBlocks.WET_CEILING_TILE.getDefaultState(), Block.NOTIFY_ALL, 1);

		if (state.isOf(Blocks.COBWEB))
			if (random.nextDouble() < 0.5)
				region.setBlockState(pos, Blocks.COBWEB.getDefaultState(), Block.NOTIFY_ALL, 1);
			else
				region.setBlockState(pos, Blocks.AIR.getDefaultState(), Block.NOTIFY_ALL, 1);
	}
}
