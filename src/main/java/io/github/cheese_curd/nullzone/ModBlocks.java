package io.github.cheese_curd.nullzone;

import io.github.cheese_curd.nullzone.blocks.SubflooringBlock;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.MapColor;
import net.minecraft.block.enums.NoteBlockInstrument;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.block.extensions.api.QuiltBlockSettings;
import org.quiltmc.qsl.item.setting.api.QuiltItemSettings;

public class ModBlocks
{
	public static Block registerBlock(ModContainer mod, String id, QuiltBlockSettings blockSettings, QuiltItemSettings itemSettings)
	{
		final Block _BLOCK = new Block(blockSettings);
		final Item  _ITEM  = new BlockItem(_BLOCK, itemSettings);

		Registry.register(Registries.BLOCK, new Identifier(mod.metadata().id(), id), _BLOCK);
		Registry.register(Registries.ITEM,  new Identifier(mod.metadata().id(), id), _ITEM);

		return _BLOCK;
	}

	static QuiltBlockSettings CONCRETE_SETTINGS = QuiltBlockSettings.create()
		.sounds(BlockSoundGroup.STONE)
		.mapColor(MapColor.STONE)
		.instrument(NoteBlockInstrument.BASEDRUM)
		.requiresTool()
		.strength(1.5F, 6.0F);

	public static void register(ModContainer mod)
	{
		final Block CEILING_TILE =  registerBlock(mod, "ceiling_tile", QuiltBlockSettings.copyOf(Blocks.WHITE_WOOL), Nullzone.ITEM_NO_SETTINGS);

		final Block CONCRETE_WALL =  registerBlock(mod, "concrete_wall",        CONCRETE_SETTINGS, Nullzone.ITEM_NO_SETTINGS);
		final Block CONCRETE_WALL_TOP =  registerBlock(mod, "concrete_wall_top",    CONCRETE_SETTINGS, Nullzone.ITEM_NO_SETTINGS);
		final Block CONCRETE_WALL_BOTTOM =  registerBlock(mod, "concrete_wall_bottom", CONCRETE_SETTINGS, Nullzone.ITEM_NO_SETTINGS);

		final Block CEILING =  registerBlock(mod, "ceiling", CONCRETE_SETTINGS, Nullzone.ITEM_NO_SETTINGS);

		// Sub Flooring
		final SubflooringBlock SUBFLOORING = new SubflooringBlock(QuiltBlockSettings.copyOf(Blocks.OAK_PLANKS));

		Registry.register(Registries.BLOCK, new Identifier(mod.metadata().id(), "subflooring"), SUBFLOORING);
		Registry.register(Registries.ITEM,  new Identifier(mod.metadata().id(), "subflooring"), new BlockItem(SUBFLOORING, Nullzone.ITEM_NO_SETTINGS));

		ItemGroupEvents.modifyEntriesEvent(Nullzone.NULLZONE_GROUP_KEY).register(entries -> {
			entries.addItem(CEILING_TILE.asItem());

			entries.addItem(CONCRETE_WALL.asItem());
			entries.addItem(CONCRETE_WALL_TOP.asItem());
			entries.addItem(CONCRETE_WALL_BOTTOM.asItem());

			entries.addItem(CEILING.asItem());

			entries.addItem(SUBFLOORING.asItem());
		});
	}
}
