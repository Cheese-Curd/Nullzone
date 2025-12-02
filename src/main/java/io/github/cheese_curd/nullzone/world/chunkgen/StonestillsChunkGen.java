package io.github.cheese_curd.nullzone.world.chunkgen;

import com.mojang.datafixers.util.Either;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.cheese_curd.nullzone.ModLimGen;
import io.github.cheese_curd.nullzone.NullLootTables;
import io.github.cheese_curd.nullzone.Nullzone;
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
import net.minecraft.block.entity.LootableContainerBlockEntity;
import net.minecraft.loot.LootTables;
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

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.Function;

import io.github.cheese_curd.nullzone.world.Beta18OreTypes;

public class StonestillsChunkGen extends AbstractNbtChunkGenerator
{
	public static final Codec<StonestillsChunkGen> CODEC = RecordCodecBuilder.create((instance) -> {
		return instance.group(BiomeSource.CODEC.fieldOf("biome_source").stable().forGetter((chunkGenerator) -> {
			return chunkGenerator.populationSource;
		}), Codec.INT.fieldOf("width").stable().forGetter((chunkGenerator) -> {
			return chunkGenerator.mazeGenerator.width;
		}), Codec.INT.fieldOf("height").stable().forGetter((chunkGenerator) -> {
			return chunkGenerator.mazeGenerator.height;
		}), Codec.INT.fieldOf("padding").stable().forGetter((chunkGenerator) -> {
			return chunkGenerator.mazeGenerator.thicknessX - chunkGenerator.mazeGenerator.width;
		})).apply(instance, instance.stable(StonestillsChunkGen::new));
	});

	MazeGenerator<MazeComponent> mazeGenerator;
	ChunkGenBase chunkGenBase;

	public static NbtGroup createGroup() {
		NbtGroup.Builder builder = NbtGroup.Builder
			.create(ModLimGen.STONESTILLS_ID)
			.with("base", "base", 1, 2)
			.with("ladder", "ladder", 1, 2)
			.with("wall", 1, 5);

		return builder.build();
	}

	public StonestillsChunkGen(BiomeSource biomeSource, int width, int height, int padding) {
		super(biomeSource, createGroup());
		this.mazeGenerator = new MazeGenerator<MazeComponent>(width, height, padding + width, padding + width, 0);
		this.chunkGenBase = new ChunkGenBase(ModLimGen.STONESTILLS_ID);
	}

	@Override
	public int getPlacementRadius() {
		return 4;
	}

	public MazeComponent newMaze(ChunkRegion region, MazeComponent.Vec2i mazePos, int width, int height, RandomGenerator random) {
		DepthFirstMaze maze = new DepthFirstMaze(width, height, random);

		maze.generateMaze();

		return maze;
	}

	public void decorateCell(ChunkRegion region, MazeComponent.Vec2i pos, MazeComponent.Vec2i mazePos, MazeComponent maze, MazeComponent.CellState state, MazeComponent.Vec2i thickness, RandomGenerator random) {
		Pair<MazePiece, Manipulation> piece = MazePiece.getFromCell(state, random);

		// the normal random is REALLY predictable so
		random = RandomGenerator.createLegacy(region.getSeed() + LimlibHelper.blockSeed(pos.toBlock()));

		generateNbt(region, pos.toBlock().up(5).east(), nbtGroup.pick("wall", random));
		generateNbt(region, pos.toBlock().up(1).east(), nbtGroup.pick("wall", random));
		// Other Wall
		generateNbt(region, pos.toBlock().up(5), nbtGroup.pick("wall", random), Manipulation.CLOCKWISE_90);
		generateNbt(region, pos.toBlock().up(1), nbtGroup.pick("wall", random), Manipulation.CLOCKWISE_90);

		boolean isLadder = random.nextDouble() < 0.025;

		if (isLadder)
			generateNbt(region, pos.toBlock().down(8), nbtGroup.pick("ladder", random));
		else
			generateNbt(region, pos.toBlock(), nbtGroup.pick("base", random));

		// Floor -1
		BlockPos blockPos = pos.toBlock().down(8);
		random = RandomGenerator.createLegacy(region.getSeed() + LimlibHelper.blockSeed(pos.toBlock()) + (random.nextInt() * 4L));

		generateNbt(region, blockPos.up(5).east(), nbtGroup.pick("wall", random));
		generateNbt(region, blockPos.up(1).east(), nbtGroup.pick("wall", random));
		// Other Wall
		generateNbt(region, blockPos.up(5), nbtGroup.pick("wall", random), Manipulation.CLOCKWISE_90);
		generateNbt(region, blockPos.up(1), nbtGroup.pick("wall", random), Manipulation.CLOCKWISE_90);

		if (!isLadder) // Floor 2
			generateNbt(region, blockPos, nbtGroup.pick("base", random));
	}

	private void generateSingleOreVein(ChunkRegion region, Chunk chunk, BlockPos origin, RandomGenerator random) {
		Block veinBlock;
		int veinSize = 1;

		if (random.nextDouble() < 0.25) // Lava/Air Vein
		{
			veinBlock = random.nextBoolean() ? Blocks.LAVA : Blocks.AIR;
			veinSize += random.nextInt(10);

			if (veinBlock == Blocks.AIR && random.nextBoolean()) // Cave Pocket
				veinSize = (veinSize + 5) * 2;
		}
		else // Ore Vein
		{
			Beta18OreTypes oreIndex = Beta18OreTypes.values()[random.nextInt(Beta18OreTypes.values().length)];

			switch (oreIndex)
			{
				case COAL:
					veinSize += random.nextInt(8);
					veinBlock = Blocks.COAL_ORE;
					break;
				case IRON:
					veinSize += random.nextInt(6);
					veinBlock = Blocks.IRON_ORE;
					break;
				case GOLD:
					veinSize += random.nextInt(3);
					veinBlock = Blocks.GOLD_ORE;
					break;
				case REDSTONE:
					veinSize += random.nextInt(4);
					veinBlock = Blocks.REDSTONE_ORE;
					break;
				case LAPIS_LAZULI:
					veinSize += random.nextInt(3);
					veinBlock = Blocks.LAPIS_ORE;
					break;
				case DIAMOND:
					veinSize += random.nextInt(4);
					veinBlock = Blocks.DIAMOND_ORE;
					break;
				default:
					veinBlock = Blocks.IRON_ORE;
					Nullzone.LOGGER.info("[Stonestills] Unknown Ore Index: {}", oreIndex);
			}
		}

		for (int i = 0; i < veinSize; i++) {
			BlockPos pos = origin.add(
				random.nextInt(3) - 1,
				random.nextInt(3) - 1,
				random.nextInt(3) - 1
			);

			if (!region.isValidForSetBlock(pos)) continue;

			BlockState current = chunk.getBlockState(pos);

			if (current.isOf(Blocks.STONE))
				chunk.setBlockState(pos, veinBlock.getDefaultState(), true);
		}
	}


	private void generateOreVeins(ChunkRegion chunkRegion, Chunk chunk)
	{
		ChunkPos chunkPos = chunk.getPos();

		RandomGenerator random = RandomGenerator.createLegacy(
			chunkRegion.getSeed() + LimlibHelper.blockSeed(chunkPos.getBlockPos(Math.min(16, chunkPos.x), chunkPos.x + chunkPos.z, Math.min(16, chunkPos.x)))
		);

		int veinAttempts = 4;

		for (int i = 0; i < veinAttempts; i++) {
			int x = chunkPos.getStartX() + random.nextInt(16);
			int y = chunkRegion.getBottomY() + random.nextInt(chunkRegion.getTopY());
			int z = chunkPos.getStartZ() + random.nextInt(16);

			generateSingleOreVein(chunkRegion, chunk, new BlockPos(x, y, z), random);
		}
	}

	@Override
	public CompletableFuture<Chunk> populateNoise(
		ChunkRegion chunkRegion, ChunkStatus targetStatus, Executor executor,
		ServerWorld world, ChunkGenerator generator, StructureTemplateManager structureTemplateManager,
		ServerLightingProvider lightingProvider, Function<Chunk, CompletableFuture<Either<Chunk,
			ChunkHolder.Unloaded>>> fullChunkConverter, List<Chunk> chunks, Chunk chunk)
	{
		ChunkGenBase.fillBelowYWith(-7, Blocks.STONE, chunk, chunkRegion);

		this.mazeGenerator.generateMaze(new MazeComponent.Vec2i(chunk.getPos().getStartPos()), chunkRegion, this::newMaze, this::decorateCell);

		ChunkGenBase.fillAboveYWith(9, Blocks.STONE, chunk, chunkRegion);

		generateOreVeins(chunkRegion, chunk);

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

	@Override
	protected Identifier getContainerLootTable(LootableContainerBlockEntity container)
	{
		return NullLootTables.STONESTILLS_STORAGE;
	}
}
