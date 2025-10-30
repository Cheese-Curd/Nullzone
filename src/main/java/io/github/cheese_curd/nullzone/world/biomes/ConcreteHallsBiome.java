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
	public static final RegistryKey<Biome> CONCRETE_HALLS_BIOME = RegistryKey.of(RegistryKeys.BIOME, new Identifier(Nullzone.MOD_ID, "concrete_halls"));

	public static Biome create(HolderProvider<PlacedFeature> features, HolderProvider<ConfiguredCarver<?>> carvers) {
		Biome.Builder biome = new Biome.Builder();

		SpawnSettings.Builder spawnSettings = new SpawnSettings.Builder();

		GenerationSettings.Builder generationSettings = new GenerationSettings.Builder(features, carvers);

		BiomeEffects.Builder biomeEffects = new BiomeEffects.Builder();
		// (your effects here)
		BiomeEffects effects = biomeEffects.build();

		biome.spawnSettings(spawnSettings.build());
		biome.generationSettings(generationSettings.build());
		biome.effects(effects);

		// (biome weather related settings here)

		return biome.build();
	}
}
