package io.github.cheese_curd.nullzone.world.chunkgen;

import com.mojang.datafixers.util.Either;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.cheese_curd.nullzone.ModLimGen;
import io.github.cheese_curd.nullzone.Nullzone;
import net.ludocrypt.limlib.api.world.LimlibHelper;
import net.ludocrypt.limlib.api.world.Manipulation;
import net.ludocrypt.limlib.api.world.NbtGroup;
import net.ludocrypt.limlib.api.world.chunk.AbstractNbtChunkGenerator;
import net.ludocrypt.limlib.api.world.maze.DepthFirstMaze;
import net.ludocrypt.limlib.api.world.maze.MazeComponent;
import net.ludocrypt.limlib.api.world.maze.MazeGenerator;
import net.ludocrypt.limlib.api.world.maze.MazePiece;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ChunkHolder;
import net.minecraft.server.world.ServerLightingProvider;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.structure.StructureTemplateManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.random.RandomGenerator;
import net.minecraft.world.ChunkRegion;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkStatus;
import net.minecraft.world.gen.RandomState;
import net.minecraft.world.gen.chunk.ChunkGenerator;

import java.util.List;
import java.util.Optional;
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

	MazeGenerator<MazeComponent> mazeGenerator;
	ChunkGenBase chunkGenBase;

	public static NbtGroup createGroup() {
		NbtGroup.Builder builder = NbtGroup.Builder
			.create(ModLimGen.CONCRETE_HALLS_ID)
//			.with("decoration", 1, 4)
			.with("base", "base")
			.with("base_dark", "base_dark");

		ChunkGenBase.getAllPieces(builder);

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

		maze.cellState(0, maze.height / 2).down(true);
		maze.cellState(maze.width - 1, maze.height / 2).up(true);
		maze.cellState(maze.width / 2, maze.height - 1).right(true);
		maze.cellState(maze.width / 2, 0).left(true);

		return maze;
	}

	public void decorateCell(ChunkRegion region, MazeComponent.Vec2i pos, MazeComponent.Vec2i mazePos, MazeComponent maze, MazeComponent.CellState state, MazeComponent.Vec2i thickness, RandomGenerator random) {
		Pair<MazePiece, Manipulation> piece = MazePiece.getFromCell(state, random);

		// the normal random is REALLY predictable so
		random = RandomGenerator.createLegacy(region.getSeed() + LimlibHelper.blockSeed(pos.toBlock()));

		if (piece.getFirst() != MazePiece.E) {
			Identifier nbtFile;

			// Stole this from LudoCrypt
			// https://github.com/LudoCrypt/The-Corners/blob/cb8d030ce315d7cac4207dfd5b2ce0fee89b6a7c/src/main/java/net/ludocrypt/corners/world/chunk/CommunalCorridorsChunkGenerator.java#L434
			String dir = ChunkGenBase.getCellDir(state);

			BlockPos blockPos = pos.toBlock();

			// Decorate the room
			// Reason this is before is for rooms that have certain walls that we don't want to overwrite
			if (random.nextDouble() < 0.5)
				generateNbt(region, blockPos.up(1), nbtGroup.pick("decoration", random), Manipulation.random(random));

			if (!dir.isEmpty())
			{
				nbtFile = nbtGroup.nbtId("maze/" + dir, dir);

				generateNbt(region, blockPos.up(1), nbtFile);
			}
			else
				Nullzone.LOGGER.info("NO DIRECTION!!");

			if (random.nextDouble() <= 0.25)
				generateNbt(region, blockPos, nbtGroup.nbtId("base_dark", "base_dark"));
			else
				generateNbt(region, blockPos, nbtGroup.nbtId("base", "base"));
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

	@Override
	protected void modifyStructure(ChunkRegion region, BlockPos pos, BlockState state, Optional<NbtCompound> blockEntityNbt) {
		super.modifyStructure(region, pos, state, blockEntityNbt);

		chunkGenBase.modifyStructure(region, pos, state, blockEntityNbt);
	}
}
