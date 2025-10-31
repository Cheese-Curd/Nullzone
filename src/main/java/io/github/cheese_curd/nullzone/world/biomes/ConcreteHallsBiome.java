package io.github.cheese_curd.nullzone.world.biomes;

import io.github.cheese_curd.nullzone.Nullzone;
import net.minecraft.registry.HolderProvider;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeEffects;
import net.minecraft.world.biome.GenerationSettings;
import net.minecraft.world.biome.SpawnSettings;
import net.minecraft.world.gen.carver.ConfiguredCarver;
import net.minecraft.world.gen.feature.PlacedFeature;

public class ConcreteHallsBiome
{
	public static Biome create(HolderProvider<PlacedFeature> features, HolderProvider<ConfiguredCarver<?>> carvers) {
		Biome.Builder biome = new Biome.Builder();

		SpawnSettings.Builder spawnSettings = new SpawnSettings.Builder();

		GenerationSettings.Builder generationSettings = new GenerationSettings.Builder(features, carvers);

		BiomeEffects.Builder biomeEffects = new BiomeEffects.Builder()
			.fogColor(0x999999)
			.skyColor(0xAAAAAA)
			.waterColor(0x444444)
			.waterFogColor(0x222222);

		BiomeEffects effects = biomeEffects.build();

		biome.effects(effects)
			.hasPrecipitation(false)
			.temperature(0.8F)
			.downfall(0.0F)
			.spawnSettings(spawnSettings.build())
			.generationSettings(generationSettings.build())
			.effects(effects)
			.build();

		return biome.build();
	}
}
