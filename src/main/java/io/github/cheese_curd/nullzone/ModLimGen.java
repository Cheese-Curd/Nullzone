package io.github.cheese_curd.nullzone;

import com.mojang.serialization.Lifecycle;
import io.github.cheese_curd.nullzone.world.biomes.ConcreteHallsBiome;
import io.github.cheese_curd.nullzone.world.chunkgen.ConcreteHallsChunkGen;
import net.ludocrypt.limlib.api.LimlibRegistrar;
import net.ludocrypt.limlib.api.LimlibRegistryHooks;
import net.ludocrypt.limlib.api.LimlibWorld;
import net.minecraft.registry.HolderProvider;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.int_provider.ConstantIntProvider;
import net.minecraft.world.World;
import net.minecraft.world.biome.source.FixedBiomeSource;
import net.minecraft.world.dimension.DimensionOptions;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.dimension.DimensionTypes;
import net.minecraft.world.gen.carver.ConfiguredCarver;
import net.minecraft.world.gen.feature.PlacedFeature;

import java.util.OptionalLong;

public class ModLimGen implements LimlibRegistrar
{
	public static final Identifier CONCRETE_HALLS_ID = new Identifier(Nullzone.MOD_ID, "concrete_halls");

	public static final LimlibWorld CONCRETE_HALLS =
		new LimlibWorld(
			() -> new DimensionType(
				OptionalLong.empty(),
				true,
				false,
				false,
				true,
				1.0,
				true,
				false,
				-32, 144, 144,
				BlockTags.INFINIBURN_OVERWORLD,
				DimensionTypes.OVERWORLD_ID,
				0,
				new DimensionType.MonsterSettings(
					false,
					false,
					ConstantIntProvider.create(0),
					0
				)
			),
			(registry) ->
				new DimensionOptions(
					registry.get(RegistryKeys.DIMENSION_TYPE)
						.getHolder(RegistryKey.of(RegistryKeys.DIMENSION_TYPE, CONCRETE_HALLS_ID))
						.get(),
					new ConcreteHallsChunkGen(
						new FixedBiomeSource(registry
							.get(RegistryKeys.BIOME)
							.getHolder(NullBiomes.CONCRETE_HALLS_BIOME)
							.get()), 6, 6, 0
					)
				)
		);


	public static final RegistryKey<World> CONCRETE_HALLS_KEY = RegistryKey.of(RegistryKeys.WORLD, CONCRETE_HALLS_ID);

	@Override
	public void registerHooks()
	{
		LimlibWorld.LIMLIB_WORLD.register(
			RegistryKey.of(LimlibWorld.LIMLIB_WORLD_KEY, CONCRETE_HALLS_ID),
			CONCRETE_HALLS,
			Lifecycle.stable()
		);

		LimlibRegistryHooks.hook(RegistryKeys.BIOME, ((infoLookup, registryKey, registry) -> {
			HolderProvider<PlacedFeature> features = infoLookup.lookup(RegistryKeys.PLACED_FEATURE).get().getter();
			HolderProvider<ConfiguredCarver<?>> carvers = infoLookup.lookup(RegistryKeys.CONFIGURED_CARVER).get().getter();

			registry.register(NullBiomes.CONCRETE_HALLS_BIOME, ConcreteHallsBiome.create(features, carvers), Lifecycle.stable());
		}));
	}
}
