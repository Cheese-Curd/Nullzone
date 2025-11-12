package io.github.cheese_curd.nullzone;

import net.minecraft.client.render.RenderLayer;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.client.ClientModInitializer;
import org.quiltmc.qsl.block.extensions.api.client.BlockRenderLayerMap;

public class NullzoneClient implements ClientModInitializer
{
	@Override
	public void onInitializeClient(ModContainer mod)
	{
		BlockRenderLayerMap.put(RenderLayer.getCutout(), ModBlocks.CEILING_BEAM);
	}
}
