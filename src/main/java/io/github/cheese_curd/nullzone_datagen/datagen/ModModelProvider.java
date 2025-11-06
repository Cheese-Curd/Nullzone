package io.github.cheese_curd.nullzone_datagen.datagen;

import io.github.cheese_curd.nullzone_datagen.ModBlocks;
import io.github.cheese_curd.nullzone_datagen.ModItems;
import io.github.cheese_curd.nullzone_datagen.Nullzone;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.block.Block;
import net.minecraft.data.client.*;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

public class ModModelProvider extends FabricModelProvider {
	public ModModelProvider(FabricDataOutput output) {
		super(output);
	}

	@Override
	public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {
		for (Block block : ModBlocks.MOD_BLOCKS_MODELS)
			blockStateModelGenerator.registerSimpleCubeAll(block);

        for (Block block : ModBlocks.MOD_BLOCKS_TB_MODELS)
        {
            Identifier id    = Registries.BLOCK.getId(block);
            String blockName = id.getPath();

            blockStateModelGenerator.registerSingleton(
                            block,
                            TextureMap.sideTopBottom(block)
                                    .put(TextureKey.SIDE, Nullzone.getIdentifier(blockName))
                                    .put(TextureKey.TOP, Nullzone.getIdentifier(blockName + "_top"))
                                    .put(TextureKey.BOTTOM, Nullzone.getIdentifier(blockName + "_bottom")),
                    Models.CUBE_BOTTOM_TOP
            );
        }

	}

	@Override
	public void generateItemModels(ItemModelGenerator itemModelGenerator) {
		for (Item item : ModItems.MOD_ITEMS)
			itemModelGenerator.register(item, Models.GENERATED);
	}
}
