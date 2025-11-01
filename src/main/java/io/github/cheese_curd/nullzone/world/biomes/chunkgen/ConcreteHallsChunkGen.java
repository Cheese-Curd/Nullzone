package io.github.cheese_curd.nullzone.world.biomes.chunkgen;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.cheese_curd.nullzone.ModLimGen;
import net.ludocrypt.limlib.api.world.LimlibHelper;
import net.ludocrypt.limlib.api.world.NbtGroup;
import net.ludocrypt.limlib.api.world.chunk.AbstractNbtChunkGenerator;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ChunkHolder;
import net.minecraft.server.world.ServerLightingProvider;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.structure.StructureTemplateManager;
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

public class ConcreteHallsChunkGen extends AbstractNbtChunkGenerator
{
	public static final Codec<ConcreteHallsChunkGen> CODEC = RecordCodecBuilder.create((instance) -> {
		return instance.group(BiomeSource.CODEC.fieldOf("biome_source").stable().forGetter((chunkGenerator) -> {
			return chunkGenerator.populationSource;
		}), NbtGroup.CODEC.fieldOf("nbt_group").stable().forGetter((chunkGenerator) -> {
			return chunkGenerator.nbtGroup;
		})).apply(instance, instance.stable(ConcreteHallsChunkGen::new));
	});

	public static NbtGroup createGroup() {
		return NbtGroup.Builder
			.create(ModLimGen.CONCRETE_HALLS_ID)
			.with("concrete_halls", "concrete_halls_straight", 1, 2	) // Replace with to from range
			.build();
	}

	public ConcreteHallsChunkGen(BiomeSource biomeSource) {
		super(biomeSource, createGroup());
	}

	public ConcreteHallsChunkGen(BiomeSource biomeSource, NbtGroup nbtGroup) {
		super(biomeSource, nbtGroup);
	}

	@Override
	public int getPlacementRadius() {
		return 1;
	}

	private void fillBelowZeroWithStone(Chunk chunk, ChunkRegion region) {
		int minY = region.getBottomY();
		int maxY = 0;

		BlockState stone = Blocks.STONE.getDefaultState();
		BlockState bedrock = Blocks.BEDROCK.getDefaultState();

		for (int x = 0; x < 16; x++) {
			for (int z = 0; z < 16; z++) {

				chunk.setBlockState(new BlockPos(x, minY, z), bedrock, false);

				for (int y = minY + 1; y < maxY; y++) {
					chunk.setBlockState(new BlockPos(x, y, z), stone, false);
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
		fillBelowZeroWithStone(chunk, chunkRegion);

		BlockPos chunkOrigin = chunk.getPos().getStartPos();
		BlockPos origin = chunkOrigin.add(0, -1, 0);

		RandomGenerator random = RandomGenerator.createLegacy(chunkRegion.getSeed() + LimlibHelper.blockSeed(origin));
		generateNbt(chunkRegion, origin, nbtGroup.pick("concrete_halls", random));

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

		if (state.isOf(Blocks.STRUCTURE_BLOCK))
		{
			region.setBlockState(pos, Blocks.AIR.getDefaultState(), Block.NOTIFY_ALL, 1);
		}
	}
}
