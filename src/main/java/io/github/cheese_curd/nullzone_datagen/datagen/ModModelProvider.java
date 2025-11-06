package io.github.cheese_curd.nullzone_datagen.datagen;

import io.github.cheese_curd.nullzone_datagen.ModBlocks;
import io.github.cheese_curd.nullzone_datagen.ModItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.block.Block;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.client.ItemModelGenerator;
import net.minecraft.data.client.Models;
import net.minecraft.item.Item;

public class ModModelProvider extends FabricModelProvider {
	public ModModelProvider(FabricDataOutput output) {
		super(output);
	}

	@Override
	public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {
		for (Block block : ModBlocks.MOD_BLOCKS_MODELS)
			blockStateModelGenerator.registerSimpleCubeAll(block);
	}


	@Override
	public void generateItemModels(ItemModelGenerator itemModelGenerator) {
		for (Item item : ModItems.MOD_ITEMS)
			itemModelGenerator.register(item, Models.GENERATED);
	}
}
