package io.github.cheese_curd.nullzone.datagen;

import io.github.cheese_curd.nullzone.ModBlocks;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.registry.HolderLookup;
import net.minecraft.registry.tag.BlockTags;

import java.util.concurrent.CompletableFuture;

public class ModBlockTagGenerator extends FabricTagProvider.BlockTagProvider {
	public ModBlockTagGenerator(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
		super(output, registriesFuture);
	}

	@Override
	protected void configure(HolderLookup.Provider arg) {
		getOrCreateTagBuilder(BlockTags.PICKAXE_MINEABLE)
			.add(ModBlocks.CEILING)

			.add(ModBlocks.CONCRETE_FLOOR)

			.add(ModBlocks.CONCRETE_WALL_TOP)
			.add(ModBlocks.CONCRETE_WALL)
			.add(ModBlocks.CONCRETE_WALL_BOTTOM);

//			.add(ModBlocks.CONCRETE_WALL_STAIRS);

		getOrCreateTagBuilder(BlockTags.NEEDS_STONE_TOOL)
			.add(ModBlocks.CEILING)

			.add(ModBlocks.CONCRETE_FLOOR)

			.add(ModBlocks.CONCRETE_WALL_TOP)
			.add(ModBlocks.CONCRETE_WALL)
			.add(ModBlocks.CONCRETE_WALL_BOTTOM);

//			.add(ModBlocks.CONCRETE_WALL_STAIRS);
	}
}
