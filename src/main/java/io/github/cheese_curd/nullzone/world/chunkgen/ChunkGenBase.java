package io.github.cheese_curd.nullzone.world.chunkgen;

import io.github.cheese_curd.nullzone.ModBlocks;
import io.github.cheese_curd.nullzone.Nullzone;
import net.ludocrypt.limlib.api.world.LimlibHelper;
import net.ludocrypt.limlib.api.world.NbtGroup;
import net.ludocrypt.limlib.api.world.maze.MazeComponent;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.random.RandomGenerator;
import net.minecraft.world.ChunkRegion;
import net.minecraft.world.chunk.Chunk;

import java.util.Optional;

public class ChunkGenBase
{
	// (Kinda) stole this from LudoCrypt
	// https://github.com/LudoCrypt/The-Corners/blob/cb8d030ce315d7cac4207dfd5b2ce0fee89b6a7c/src/main/java/net/ludocrypt/corners/world/chunk/CommunalCorridorsChunkGenerator.java#L434
	public static String getCellDir(MazeComponent.CellState state) {
		StringBuilder dir = new StringBuilder();

		if (state.goesLeft())  dir.append('n');
		if (state.goesUp())    dir.append('e');
		if (state.goesRight()) dir.append('s');
		if (state.goesDown())  dir.append('w');

		return dir.toString();
	}

	public static void connectMazes(MazeComponent maze)
	{
		maze.cellState(0, maze.height / 2).down(true);
		maze.cellState(maze.width - 1, maze.height / 2).up(true);
		maze.cellState(maze.width / 2, maze.height - 1).right(true);
		maze.cellState(maze.width / 2, 0).left(true);
	}

	public static void fillBelowZeroWith(Block block, Chunk chunk, ChunkRegion region) {
		int minY = region.getBottomY();
		int maxY = 0;

		BlockState bedrock = Blocks.BEDROCK.getDefaultState();

		for (int x = 0; x < 16; x++) {
			for (int z = 0; z < 16; z++) {

				chunk.setBlockState(new BlockPos(x, minY, z), bedrock, false);

				for (int y = minY + 1; y < maxY; y++) {
					chunk.setBlockState(new BlockPos(x, y, z), block.getDefaultState(), false);
				}
			}
		}
	}

	public static void fillAboveYWith(int minY, Block block, Chunk chunk, ChunkRegion region)
	{
		int maxY = region.getTopY();

		for (int x = 0; x < 16; x++) {
			for (int z = 0; z < 16; z++) {
				for (int y = minY + 1; y <= maxY; y++) {
					chunk.setBlockState(new BlockPos(x, y, z), block.getDefaultState(), false);
				}
			}
		}
	}

	public static void getAllPieces(NbtGroup.Builder builder, boolean all15)
	{
		if (all15) {
			// Taken from LudoCrypt
			// https://github.com/LudoCrypt/The-Corners/blob/cb8d030ce315d7cac4207dfd5b2ce0fee89b6a7c/src/main/java/net/ludocrypt/corners/world/chunk/CommunalCorridorsChunkGenerator.java#L91
			for (int i = 0; i < 15; i++) {
				String dir = "nesw";
				boolean north = ((i & 8) != 0);
				boolean east = ((i & 4) != 0);
				boolean south = ((i & 2) != 0);
				boolean west = ((i & 1) != 0);

				if (north)
					dir = dir.replace("n", "");
				if (east)
					dir = dir.replace("e", "");
				if (south)
					dir = dir.replace("s", "");
				if (west)
					dir = dir.replace("w", "");

				builder.with("maze/" + dir, dir);
			}
		}
		else
		{
			String dir = "nesw";

			/*
			if (state.goesLeft())  dir.append('n');
			if (state.goesUp())    dir.append('e');
			if (state.goesRight()) dir.append('s');
			if (state.goesDown())  dir.append('w');
			 */

//			builder.with("maze/" + dir, dir);
//			builder.with("maze/" + dir, dir);
//			builder.with("maze/" + dir, dir);
//			builder.with("maze/" + dir, dir);
			Nullzone.LOGGER.warn("all15 == false is unfinished!!");
		}
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
