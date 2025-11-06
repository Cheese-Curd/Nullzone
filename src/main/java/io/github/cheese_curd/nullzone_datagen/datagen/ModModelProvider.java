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

			boolean isBottomBlock = blockName.contains("_bottom");
	        boolean isTopBlock = blockName.contains("_top");

			String identStr = "blocks/" + blockName;

			Identifier sideTex    = Nullzone.getIdentifier(identStr);
	        Identifier topTex     = Nullzone.getIdentifier(identStr + "-top");
	        Identifier bottomText = Nullzone.getIdentifier(identStr + "-bottom");

			if (isBottomBlock)
				topTex = sideTex;
	        if (isTopBlock)
		        bottomText = sideTex;

            blockStateModelGenerator.registerSingleton(
                            block,
                            TextureMap.sideTopBottom(block)
                                    .put(TextureKey.SIDE, sideTex)
                                    .put(TextureKey.TOP, topTex)
                                    .put(TextureKey.BOTTOM, bottomText),
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
