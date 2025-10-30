package io.github.cheese_curd.nullzone.world.biomes.chunkgen;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.cheese_curd.nullzone.ModLimGen;
import io.github.cheese_curd.nullzone.Nullzone;
import net.ludocrypt.limlib.api.world.NbtGroup;
import net.ludocrypt.limlib.api.world.chunk.AbstractNbtChunkGenerator;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.server.world.ChunkHolder;
import net.minecraft.server.world.ServerLightingProvider;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.structure.StructureTemplateManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ChunkRegion;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkStatus;
import net.minecraft.world.gen.RandomState;
import net.minecraft.world.gen.chunk.ChunkGenerator;

import java.util.List;
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
			.with("concrete_halls_straight_1") // Replace with to from range
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

	@Override
	public CompletableFuture<Chunk> populateNoise(ChunkRegion chunkRegion, ChunkStatus targetStatus, Executor executor, ServerWorld world, ChunkGenerator generator, StructureTemplateManager structureTemplateManager, ServerLightingProvider lightingProvider, Function<Chunk, CompletableFuture<Either<Chunk, ChunkHolder.Unloaded>>> fullChunkConverter, List<Chunk> chunks, Chunk chunk) {
		return CompletableFuture.completedFuture(chunk);
	}

	@Override
	protected Codec<? extends ChunkGenerator> getCodec() {
		return CODEC;
	}

	@Override
	public int getWorldHeight() {
		return 150;
	}

	@Override
	public void method_40450(List<String> list, RandomState randomState, BlockPos pos) {}

	public static void init() {
		Registry.register(Registries.CHUNK_GENERATOR, new Identifier(Nullzone.MOD_ID, "concretehallschunkgen"), ConcreteHallsChunkGen.CODEC);
	}
}
