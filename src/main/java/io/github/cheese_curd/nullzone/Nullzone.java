package io.github.cheese_curd.nullzone;

import io.github.cheese_curd.nullzone.mixin.PlayerPlacedHolder;
import net.fabricmc.fabric.api.event.player.AttackBlockCallback;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.kyrptonaught.customportalapi.api.CustomPortalBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
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

		// Portal
		CustomPortalBuilder.beginPortal()
			.frameBlock(Blocks.NETHER_BRICKS)
			.lightWithWater()
			.destDimID(ModLimGen.ABANDONED_OFFICES_ID)
			.tintColor(0x000000)
			.setPortalSearchYRange(0, 0)
			.forcedSize(3, 3)
			.registerPortal();

		AttackBlockCallback.EVENT.register((player, world, hand, pos, dir) ->
		{
			if (world.isClient()) return ActionResult.PASS;

			BlockState state = world.getBlockState(pos);
			boolean playerPlaced = ((PlayerPlacedHolder)state.getBlock()).wasPlayerPlaced();

			if (world.getRegistryKey().getValue().getNamespace().equals(MOD_ID) && !playerPlaced)
				return ActionResult.FAIL;

			return ActionResult.PASS;
		});
    }
}
