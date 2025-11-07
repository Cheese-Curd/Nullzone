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

import java.util.Optional;

public class ModModelProvider extends FabricModelProvider {
	public ModModelProvider(FabricDataOutput output) {
		super(output);
	}

	@Override
	public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {
		for (Block block : ModBlocks.MOD_BLOCKS_MODELS)
		{
			Identifier id    = Registries.BLOCK.getId(block);
			String blockName = id.getPath();

			boolean isWet = blockName.contains("wet_");

			if (isWet)
			{
				String dryName = blockName.replace("wet_", "");
				Identifier dryTex = Nullzone.getIdentifier("block/" + dryName);

				blockStateModelGenerator.registerSingleton(
						block,
						TextureMap.sideTopBottom(block)
								.put(TextureKey.ALL, dryTex),
						Models.CUBE_ALL
				);
			}
			else
				blockStateModelGenerator.registerSimpleCubeAll(block);
		}

        for (Block block : ModBlocks.MOD_BLOCKS_TB_MODELS)
        {
            Identifier id    = Registries.BLOCK.getId(block);
            String blockName = id.getPath();

			boolean isBottomBlock = blockName.contains("_bottom");
	        boolean isTopBlock = blockName.contains("_top");

			String identStr = "block/" + blockName;

			Identifier sideTex    = Nullzone.getIdentifier(identStr);
	        Identifier topTex     = Nullzone.getIdentifier(identStr + "-top");
	        Identifier bottomText = Nullzone.getIdentifier(identStr + "-bottom");

	        if (isTopBlock || isBottomBlock)
	        {
		        Identifier baseText = Nullzone.getIdentifier(identStr.replace("_top", "").replace("_bottom", "")    );
		        if (isBottomBlock)
			        topTex = baseText;
		        if (isTopBlock)
			        bottomText = baseText;
	        }

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

	private Model existingModel(String path) {
		return new Model(Optional.of(new Identifier(Nullzone.MOD_ID, path)), Optional.empty());
	}

	@Override
	public void generateItemModels(ItemModelGenerator itemModelGenerator) {
		for (Item item : ModItems.MOD_ITEMS)
			itemModelGenerator.register(item, Models.GENERATED);

		for (Block block : ModBlocks.MOD_BLOCKS)
		{
			Identifier id    = Registries.BLOCK.getId(block);
			String blockName = id.getPath();

			boolean isWet = blockName.contains("wet_");
			if (isWet)
				itemModelGenerator.register(block.asItem(), existingModel("block/" + blockName.replace("wet_", "")));
			else
				itemModelGenerator.register(block.asItem(), existingModel("block/" + blockName));
		}
	}
}
