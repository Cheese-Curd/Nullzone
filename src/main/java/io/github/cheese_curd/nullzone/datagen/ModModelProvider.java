package io.github.cheese_curd.nullzone.datagen;

import io.github.cheese_curd.nullzone.ModBlocks;
import io.github.cheese_curd.nullzone.ModItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.block.Block;
import net.minecraft.data.client.ItemModelGenerator;
import net.minecraft.data.client.model.BlockStateModelGenerator;
import net.minecraft.data.client.model.Models;
import net.minecraft.item.Item;

public class ModModelProvider extends FabricModelProvider {
	public ModModelProvider(FabricDataOutput output) {
		super(output);
	}

	@Override
	public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {
//		for (Block block : ModBlocks.MOD_BLOCKS_MODELS)
//			blockStateModelGenerator.registerSimpleCubeAll(block);
//
//		blockStateModelGenerator.registerSimpleCubeAll(ModBlocks.WET_CEILING_TILE);
//		blockStateModelGenerator.registerSimpleCubeAll(ModBlocks.WET_CEILING);
	}

	@Override
	public void generateItemModels(ItemModelGenerator itemModelGenerator) {
//		for (Item item : ModItems.MOD_ITEMS)
//			itemModelGenerator.register(item, Models.SINGLE_LAYER_ITEM);
	}
}
