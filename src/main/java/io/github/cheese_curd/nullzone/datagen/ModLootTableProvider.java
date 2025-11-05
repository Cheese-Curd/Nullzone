package io.github.cheese_curd.nullzone.datagen;

import io.github.cheese_curd.nullzone.ModBlocks;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.minecraft.block.Block;

public class ModLootTableProvider extends FabricBlockLootTableProvider {

	public ModLootTableProvider(FabricDataOutput dataOutput) {
		super(dataOutput);
	}

	@Override
	public void generate() {
		for (Block block : ModBlocks.MOD_BLOCKS)
			addDrop(block);

		addDrop(ModBlocks.SUBFLOORING);

		addDrop(ModBlocks.WET_CEILING);
		addDrop(ModBlocks.WET_CEILING_TILE);
	}
}
