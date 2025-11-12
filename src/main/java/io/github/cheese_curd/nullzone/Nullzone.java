package io.github.cheese_curd.nullzone;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer;
import org.quiltmc.qsl.block.extensions.api.QuiltBlockSettings;
import org.quiltmc.qsl.item.setting.api.QuiltItemSettings;
import org.quiltmc.qsl.lifecycle.api.event.ServerTickEvents;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Nullzone implements ModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("Nullzone");

	public static final String MOD_ID = "nullzone";

	public static final QuiltItemSettings ITEM_NO_SETTINGS   = new QuiltItemSettings();
	public static final QuiltBlockSettings BLOCK_NO_SETTINGS = QuiltBlockSettings.create();

	public static final RegistryKey<ItemGroup> NULLZONE_GROUP_KEY =
		RegistryKey.of(RegistryKeys.ITEM_GROUP, new Identifier(MOD_ID, "nullzone_group"));

	public static final ItemGroup NULLZONE_GROUP = Registry.register(
		Registries.ITEM_GROUP,
		NULLZONE_GROUP_KEY.getValue(),
		FabricItemGroup.builder()
			.icon(() -> new ItemStack(ModItems.WALL_PAPER))
			.name(Text.translatable("itemGroup.nullzone.nullzone_group"))
			.entries((context, entries) -> {
				entries.addItem(ModItems.WALL_PAPER);
			})
			.build()
	);

    @Override
    public void onInitialize(ModContainer mod)
    {
        LOGGER.info("{} Initializing...", mod.metadata().name());

		ModSounds.registerSounds();

	    ModItems.register(mod);
	    ModBlocks.register(mod);
		NullBiomes.init();
    }
}
