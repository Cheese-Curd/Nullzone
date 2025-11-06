package io.github.cheese_curd.nullzone_datagen;

import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Nullzone implements ModInitializer {
	public static final String MOD_ID = "nullzone";

	public static final FabricItemSettings ITEM_NO_SETTINGS   = new FabricItemSettings();
	public static final FabricBlockSettings BLOCK_NO_SETTINGS = FabricBlockSettings.create();

	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		LOGGER.info("Make sure this is running DataGen only!");
		LOGGER.info("{} Initializing...", MOD_ID);

		ModItems.register();
		ModBlocks.register();
	}
}