package io.github.cheese_curd.nullzone.world.biomes;

import io.github.cheese_curd.nullzone.ModSounds;
import net.minecraft.registry.HolderProvider;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeEffects;
import net.minecraft.world.biome.GenerationSettings;
import net.minecraft.world.biome.SpawnSettings;
import net.minecraft.world.gen.carver.ConfiguredCarver;
import net.minecraft.world.gen.feature.PlacedFeature;

public class AbandonedOfficesBiome
{
	public static Biome create(HolderProvider<PlacedFeature> features, HolderProvider<ConfiguredCarver<?>> carvers) {
		Biome.Builder biome = new Biome.Builder();

		SpawnSettings.Builder spawnSettings = new SpawnSettings.Builder();

		GenerationSettings.Builder generationSettings = new GenerationSettings.Builder(features, carvers);

		BiomeEffects.Builder biomeEffects = new BiomeEffects.Builder()
			.fogColor(0xFFAC91)
			.skyColor(0x808080)
			.waterColor(0x7C96FF)
			.waterFogColor(0x5466AD)
			.loopSound(ModSounds.LIGHT_BUZZ);;

		BiomeEffects effects = biomeEffects.build();

		biome.effects(effects)
			.hasPrecipitation(true)
			.temperature(0.8F)
			.downfall(1.0F)
			.spawnSettings(spawnSettings.build())
			.generationSettings(generationSettings.build())
			.effects(effects)
			.build();

		return biome.build();
	}
}
