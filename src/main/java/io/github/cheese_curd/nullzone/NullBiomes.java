package io.github.cheese_curd.nullzone;

import com.mojang.serialization.Codec;
import io.github.cheese_curd.nullzone.world.chunkgen.AbandonedOfficesChunkGen;
import io.github.cheese_curd.nullzone.world.chunkgen.ConcreteHallsChunkGen;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.chunk.ChunkGenerator;

public class NullBiomes
{
	public static final RegistryKey<Biome> CONCRETE_HALLS_BIOME =
		RegistryKey.of(RegistryKeys.BIOME, new Identifier(Nullzone.MOD_ID, "concrete_halls"));
	public static final RegistryKey<Biome> ABANDONED_OFFICES_BIOME =
		RegistryKey.of(RegistryKeys.BIOME, new Identifier(Nullzone.MOD_ID, "abandoned_offices"));

	public static void init() {
		Nullzone.LOGGER.info("Initializing Chunk Gens...");
		get("concrete_halls_chunk_generator", ConcreteHallsChunkGen.CODEC);
		get("abandoned_offices_chunk_generator", AbandonedOfficesChunkGen.CODEC);
		Nullzone.LOGGER.info("Finished Initializing Chunk Gens");

		new ModLimGen().registerHooks();
	}

	public static <C extends ChunkGenerator, D extends Codec<C>> D get(String id, D chunkGeneratorCodec) {
		return Registry.register(Registries.CHUNK_GENERATOR, new Identifier(Nullzone.MOD_ID, id), chunkGeneratorCodec);
	}
}
