package io.github.cheese_curd.nullzone_datagen.datagen;

import io.github.cheese_curd.nullzone_datagen.ModBlocks;
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
	}
}
