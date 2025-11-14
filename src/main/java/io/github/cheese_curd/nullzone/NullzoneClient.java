package io.github.cheese_curd.nullzone;

import net.minecraft.block.Block;
import net.minecraft.client.render.RenderLayer;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.client.ClientModInitializer;
import org.quiltmc.qsl.block.extensions.api.client.BlockRenderLayerMap;

public class NullzoneClient implements ClientModInitializer
{
	private static void makeCutoutBlock(Block Block)
	{
		BlockRenderLayerMap.put(RenderLayer.getCutout(), Block);
	}

	@Override
	public void onInitializeClient(ModContainer mod)
	{
		makeCutoutBlock(ModBlocks.CEILING_BEAM);
		makeCutoutBlock(ModBlocks.OFFICE_DOOR);

		makeCutoutBlock(ModBlocks.DIRT_STAIN);
		makeCutoutBlock(ModBlocks.MOLD_STAIN);
	}
}
