package io.github.cheese_curd.nullzone;

import com.mojang.serialization.Lifecycle;
import io.github.cheese_curd.nullzone.world.biomes.*;
import io.github.cheese_curd.nullzone.world.chunkgen.*;
import net.ludocrypt.limlib.api.LimlibRegistrar;
import net.ludocrypt.limlib.api.LimlibRegistryHooks;
import net.ludocrypt.limlib.api.LimlibWorld;
import net.minecraft.registry.*;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.int_provider.ConstantIntProvider;
import net.minecraft.world.World;
import net.minecraft.world.biome.source.FixedBiomeSource;
import net.minecraft.world.dimension.DimensionOptions;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.carver.ConfiguredCarver;
import net.minecraft.world.gen.feature.PlacedFeature;

import java.util.OptionalLong;

public class ModLimGen implements LimlibRegistrar
{
	public static final Identifier CONCRETE_HALLS_ID    = new Identifier(Nullzone.MOD_ID, "concrete_halls");
	public static final Identifier ABANDONED_OFFICES_ID = new Identifier(Nullzone.MOD_ID, "abandoned_offices");
	public static final Identifier STONESTILLS_ID = new Identifier(Nullzone.MOD_ID, "stonestills");

	static DimensionType genericDimType(Identifier id)
	{
		return new DimensionType(
			OptionalLong.empty(),
			true,
			false,
			false,
			true, // This is annoying :(
			1.0,
			true,
			false,
			-32, 144, 144,
			BlockTags.INFINIBURN_OVERWORLD,
			id,
			0,
			new DimensionType.MonsterSettings(false, false, ConstantIntProvider.create(0), 0)
		);
	}

	// LimlibWorld's
	public static final LimlibWorld CONCRETE_HALLS = new LimlibWorld(
		() -> genericDimType(CONCRETE_HALLS_ID),
		(registry) -> new DimensionOptions(
			registry.get(RegistryKeys.DIMENSION_TYPE)
				.getHolder(RegistryKey.of(RegistryKeys.DIMENSION_TYPE, CONCRETE_HALLS_ID))
				.get(),
			new ConcreteHallsChunkGen(
				new FixedBiomeSource(registry.get(RegistryKeys.BIOME)
					.getHolder(NullBiomes.CONCRETE_HALLS_BIOME).get()), 6, 6, 0
			)
		)
	);

	public static final LimlibWorld ABANDONED_OFFICES = new LimlibWorld(
		() -> genericDimType(ABANDONED_OFFICES_ID),
		(registry) -> new DimensionOptions(
			registry.get(RegistryKeys.DIMENSION_TYPE)
				.getHolder(RegistryKey.of(RegistryKeys.DIMENSION_TYPE, ABANDONED_OFFICES_ID))
				.get(),
			new AbandonedOfficesChunkGen(
				new FixedBiomeSource(registry.get(RegistryKeys.BIOME)
					.getHolder(NullBiomes.ABANDONED_OFFICES_BIOME).get()), 12, 6, 0
			)
		)
	);

	public static final LimlibWorld STONESTILLS = new LimlibWorld(
		() -> genericDimType(STONESTILLS_ID),
		(registry) -> new DimensionOptions(
			registry.get(RegistryKeys.DIMENSION_TYPE)
				.getHolder(RegistryKey.of(RegistryKeys.DIMENSION_TYPE, STONESTILLS_ID))
				.get(),
			new StonestillsChunkGen(
				new FixedBiomeSource(registry.get(RegistryKeys.BIOME)
					.getHolder(NullBiomes.STONESTILLS_BIOME).get()), 9, 9, 0
			)
		)
	);

	// World Keys
	public static final RegistryKey<World> CONCRETE_HALLS_KEY = RegistryKey
		.of(RegistryKeys.WORLD, CONCRETE_HALLS_ID);
	public static final RegistryKey<World> ABANDONED_OFFICES_KEY = RegistryKey
		.of(RegistryKeys.WORLD, ABANDONED_OFFICES_ID);
	public static final RegistryKey<World> STONESTILLS_KEY = RegistryKey
		.of(RegistryKeys.WORLD, STONESTILLS_ID);

	// Effects 'n Such
//	public static final SoundEffects CH_SOUNDS = new SoundEffects(
//		Optional.of(
//			new StaticReverbEffect.Builder()
//				.setDecayTime(2f)
//				.build()
//		),
//		Optional.empty(),
//		Optional.of(new MusicSound(ModSounds.LIGHT_BUZZ,
//			2000,
//			20000,
//			true))
//	);

	@Override
	public void registerHooks()
	{
		LimlibRegistryHooks.hook(RegistryKeys.BIOME, (infoLookup, registryKey, registry) -> {
			HolderProvider<PlacedFeature> features = infoLookup.lookup(RegistryKeys.PLACED_FEATURE).get().getter();
			HolderProvider<ConfiguredCarver<?>> carvers = infoLookup.lookup(RegistryKeys.CONFIGURED_CARVER).get().getter();

			registry.register(NullBiomes.CONCRETE_HALLS_BIOME, ConcreteHallsBiome.create(features, carvers), Lifecycle.stable());
			registry.register(NullBiomes.ABANDONED_OFFICES_BIOME, AbandonedOfficesBiome.create(features, carvers), Lifecycle.stable());
			registry.register(NullBiomes.STONESTILLS_BIOME, AbandonedOfficesBiome.create(features, carvers), Lifecycle.stable());
		});

		LimlibWorld.LIMLIB_WORLD.register(
			RegistryKey.of(LimlibWorld.LIMLIB_WORLD_KEY, CONCRETE_HALLS_ID),
			CONCRETE_HALLS,
			Lifecycle.stable()
		);

		LimlibWorld.LIMLIB_WORLD.register(
			RegistryKey.of(LimlibWorld.LIMLIB_WORLD_KEY, ABANDONED_OFFICES_ID),
			ABANDONED_OFFICES,
			Lifecycle.stable()
		);

		LimlibWorld.LIMLIB_WORLD.register(
			RegistryKey.of(LimlibWorld.LIMLIB_WORLD_KEY, STONESTILLS_ID),
			STONESTILLS,
			Lifecycle.stable()
		);

		// Effects 'n Such
//		LimlibRegistryHooks.hook(SoundEffects.SOUND_EFFECTS_KEY,
//			(infoLookup, registryKey, registry) -> registry.register(
//				RegistryKey.of(SoundEffects.SOUND_EFFECTS_KEY, new Identifier("nullzone:ch_sounds")),
//				CH_SOUNDS,
//				Lifecycle.stable()));
	}
}
