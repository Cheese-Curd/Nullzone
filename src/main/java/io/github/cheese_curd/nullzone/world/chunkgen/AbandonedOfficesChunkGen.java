package io.github.cheese_curd.nullzone.world.chunkgen;

import com.mojang.datafixers.util.Either;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.cheese_curd.nullzone.ModBlocks;
import io.github.cheese_curd.nullzone.ModLimGen;
import io.github.cheese_curd.nullzone.blocks.CuttableBlock;
import io.github.cheese_curd.nullzone.blocks.WetRotatableBlock;
import net.ludocrypt.limlib.api.world.LimlibHelper;
import net.ludocrypt.limlib.api.world.Manipulation;
import net.ludocrypt.limlib.api.world.NbtGroup;
import net.ludocrypt.limlib.api.world.chunk.AbstractNbtChunkGenerator;
import net.ludocrypt.limlib.api.world.maze.DepthFirstMaze;
import net.ludocrypt.limlib.api.world.maze.MazeComponent;
import net.ludocrypt.limlib.api.world.maze.MazeGenerator;
import net.ludocrypt.limlib.api.world.maze.MazePiece;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ChunkHolder;
import net.minecraft.server.world.ServerLightingProvider;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.structure.StructureTemplateManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.random.RandomGenerator;
import net.minecraft.world.ChunkRegion;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkStatus;
import net.minecraft.world.gen.RandomState;
import net.minecraft.world.gen.chunk.ChunkGenerator;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.Function;

public class AbandonedOfficesChunkGen extends AbstractNbtChunkGenerator
{
	public static final Codec<AbandonedOfficesChunkGen> CODEC = RecordCodecBuilder.create((instance) -> {
		return instance.group(BiomeSource.CODEC.fieldOf("biome_source").stable().forGetter((chunkGenerator) -> {
			return chunkGenerator.populationSource;
		}), Codec.INT.fieldOf("width").stable().forGetter((chunkGenerator) -> {
			return chunkGenerator.mazeGenerator.width;
		}), Codec.INT.fieldOf("height").stable().forGetter((chunkGenerator) -> {
			return chunkGenerator.mazeGenerator.height;
		}), Codec.INT.fieldOf("padding").stable().forGetter((chunkGenerator) -> {
			return chunkGenerator.mazeGenerator.thicknessX - chunkGenerator.mazeGenerator.width;
		})).apply(instance, instance.stable(AbandonedOfficesChunkGen::new));
	});

//	boolean[][] wetCells;
//	MazeComponent.Vec2i curMaze;

	boolean isWet;

	MazeGenerator<MazeComponent> mazeGenerator;
	ChunkGenBase chunkGenBase;

	public static NbtGroup createGroup() {
		NbtGroup.Builder builder = NbtGroup.Builder
			.create(ModLimGen.ABANDONED_OFFICES_ID)
			.with("ceiling_decoration", 1, 2)
			.with("rare_ceiling_decoration", 1, 2)
			.with("walls", 1, 6)
			.with("rare_walls", 1, 2)
			.with("base", "base")
			.with("base_dark", "base_dark");

		ChunkGenBase.getAllPieces(builder, false);

		return builder.build();
	}

	public AbandonedOfficesChunkGen(BiomeSource biomeSource, int width, int height, int padding) {
		super(biomeSource, createGroup());
		this.mazeGenerator = new MazeGenerator<MazeComponent>(width, height, padding + width, padding + width, 0);
		this.chunkGenBase = new ChunkGenBase();
	}

	@Override
	public int getPlacementRadius() {
		return 4;
	}

	public MazeComponent newMaze(ChunkRegion region, MazeComponent.Vec2i mazePos, int width, int height, RandomGenerator random) {
		DepthFirstMaze maze = new DepthFirstMaze(width, height, random);
//		DepthFirstMazeSolver solver = new DepthFirstMazeSolver(maze, random, mazePos);

		maze.generateMaze();

//		wetCells = new boolean[width][height]; // empty cells

//		maze.cellState(0, maze.height / 2).down(true);
//		maze.cellState(maze.width - 1, maze.height / 2).up(true);
//		maze.cellState(maze.width / 2, maze.height - 1).right(true);
//		maze.cellState(maze.width / 2, 0).left(true);

		return maze;
	}

	public void decorateCell(ChunkRegion region, MazeComponent.Vec2i pos, MazeComponent.Vec2i mazePos, MazeComponent maze, MazeComponent.CellState state, MazeComponent.Vec2i thickness, RandomGenerator random) {
		Pair<MazePiece, Manipulation> piece = MazePiece.getFromCell(state, random);

		isWet = random.nextFloat() < 0.25;

//		curMaze = pos;

		// the normal random is REALLY predictable so
		random = RandomGenerator.createLegacy(region.getSeed() + LimlibHelper.blockSeed(pos.toBlock()));

		if (isWet && random.nextFloat() < 0.25)
			isWet = random.nextBoolean();

//		wetCells[mazePos.getX()][mazePos.getY()] = isWet;

		if (piece.getFirst() != MazePiece.E)
		{
			BlockPos blockPos = pos.toBlock();

			if (random.nextDouble() <= 0.25 || isWet)
				generateNbt(region, blockPos, nbtGroup.nbtId("base_dark", "base_dark"));
			else
				generateNbt(region, blockPos, nbtGroup.nbtId("base", "base"));

			// Add Walls
			if (random.nextDouble() > 0.25)
				if (random.nextDouble() > 0.8)
					generateNbt(region, blockPos, nbtGroup.pick("rare_walls", random), Manipulation.random(random));
				else
					generateNbt(region, blockPos.up(1), nbtGroup.pick("walls", random), Manipulation.random(random));

			int ceilDecor   = random.nextInt(3);
			boolean rareGen = false;

			for (int i = 1; ceilDecor > i; i++)
			{
				if (random.nextDouble() < 0.25)
				{
					if (random.nextDouble() > 0.9 && !rareGen)
					{
						generateNbt(region, blockPos, nbtGroup.pick("rare_ceiling_decoration", random), Manipulation.random(random));
						rareGen = true;
					}
					else
						generateNbt(region, blockPos, nbtGroup.pick("ceiling_decoration", random), Manipulation.random(random));
				}
			}
		}
	}

	private void applyPostStructurePass(ChunkRegion region, ChunkPos chunkPos) {
		int baseY = 0;

		// Loop through all blocks in this chunk at the base layer
		for (int x = 0; x < 16; x++) {
			for (int z = 0; z < 16; z++) {
				BlockPos pos = new BlockPos(chunkPos.getStartX() + x, baseY, chunkPos.getStartZ() + z);
				BlockState state = region.getBlockState(pos);

				if (state.isOf(ModBlocks.OFFICE_CARPET)) {
					boolean skyVisible = ChunkGenBase.canSeeSky(region, pos);

					if (skyVisible) {
						// Wet the base carpet
						makeCarpetWet(region, pos);

						// Wet surrounding carpet in X/Z directions
						makeCarpetWet(region, pos.add(1, 0, 0));
						makeCarpetWet(region, pos.add(-1, 0, 0));
						makeCarpetWet(region, pos.add(0, 0, 1));
						makeCarpetWet(region, pos.add(0, 0, -1));
					}
				}
			}
		}
	}



	@Override
	public CompletableFuture<Chunk> populateNoise(
		ChunkRegion chunkRegion, ChunkStatus targetStatus, Executor executor,
		ServerWorld world, ChunkGenerator generator, StructureTemplateManager structureTemplateManager,
		ServerLightingProvider lightingProvider, Function<Chunk, CompletableFuture<Either<Chunk,
			ChunkHolder.Unloaded>>> fullChunkConverter, List<Chunk> chunks, Chunk chunk)
	{
//		fillBelowZeroWithStone(chunk, chunkRegion);

		this.mazeGenerator.generateMaze(new MazeComponent.Vec2i(chunk.getPos().getStartPos()), chunkRegion, this::newMaze, this::decorateCell);

		executor.execute(() -> applyPostStructurePass(chunkRegion, chunk.getPos()));

		return CompletableFuture.completedFuture(chunk);
	}

	@Override
	protected Codec<? extends ChunkGenerator> getCodec() {
		return CODEC;
	}

	@Override
	public int getWorldHeight() {
		return 144;
	}

	@Override
	public void method_40450(List<String> list, RandomState randomState, BlockPos pos) {}

	private void makeCarpetWet(ChunkRegion region, BlockPos pos)
	{
		BlockState state = region.getBlockState(pos);
		if (state.contains(WetRotatableBlock.WET))
			region.setBlockState(pos, state.with(WetRotatableBlock.WET, true), Block.FORCE_STATE);
	}

	@Override
	protected void modifyStructure(ChunkRegion region, BlockPos pos, BlockState state, Optional<NbtCompound> blockEntityNbt) {
		super.modifyStructure(region, pos, state, blockEntityNbt);

		RandomGenerator random = chunkGenBase.modifyStructure(region, pos, state, blockEntityNbt);

//		if (wetCells[curMaze.getX()][curMaze.getY()])
//		if (isWet)
//		{
//			if (state.isOf(ModBlocks.CEILING_TILE))
//				if (random.nextDouble() < 0.2)
//					region.setBlockState(pos, ModBlocks.WET_CEILING_TILE.getDefaultState(), Block.FORCE_STATE);
//
//			if (state.isOf(ModBlocks.OFFICE_CARPET))
//			{
//				if (random.nextDouble() > 0.75)
//				{
//					BlockPos abovePos = pos.up();
//
//					if (region.getBlockState(abovePos).isOf(Blocks.AIR))
//						region.setBlockState(abovePos, ModBlocks.SUBFLOORING.getDefaultState(), Block.FORCE_STATE);
//				}
//
//				if (random.nextDouble() > 0.15)
//					makeCarpetWet(region, pos);
//			}
//		}

		if (state.isOf(ModBlocks.CEILING_TILE_SLAB) || state.isOf(ModBlocks.WET_CEILING_TILE_SLAB))
		{
			if (random.nextDouble() > 0.8)
			{
				BlockPos newPos = pos.add(ChunkGenBase.randomCircularOffset(random, 5, true));
				if (region.getBlockState(newPos).isOf(ModBlocks.CEILING_TILE_SLAB))
					region.setBlockState(newPos,
						ModBlocks.CEILING_BEAM.getDefaultState(), Block.FORCE_STATE);
			}
		}

		if (state.isOf(ModBlocks.CEILING_BEAM))
			if (random.nextBoolean() && random.nextDouble() < 0.25)
				region.setBlockState(pos, Blocks.AIR.getDefaultState(), Block.FORCE_STATE);

		if (state.isOf(ModBlocks.WALLPAPER))
			if (random.nextBoolean() && random.nextDouble() < 0.25)
				region.setBlockState(pos, state.with(CuttableBlock.CUT, true), Block.FORCE_STATE);
		if (state.isOf(ModBlocks.WALLPAPER_TOP))
			if (random.nextBoolean() && random.nextDouble() < 0.25)
				region.setBlockState(pos, state.with(CuttableBlock.CUT, true), Block.FORCE_STATE);
	}
}
