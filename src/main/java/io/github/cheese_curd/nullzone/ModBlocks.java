package io.github.cheese_curd.nullzone;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.MapColor;
import net.minecraft.block.enums.NoteBlockInstrument;
import net.minecraft.item.BlockItem;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.block.extensions.api.QuiltBlockSettings;
import org.quiltmc.qsl.item.setting.api.QuiltItemSettings;

public class ModBlocks
{
	public static void registerBlock(ModContainer mod, String id, QuiltBlockSettings blockSettings, QuiltItemSettings itemSettings)
	{
		final Block _BLOCK = new Block(blockSettings);

		Registry.register(Registries.BLOCK, new Identifier(mod.metadata().id(), id), _BLOCK);
		Registry.register(Registries.ITEM,  new Identifier(mod.metadata().id(), id), new BlockItem(_BLOCK, itemSettings));
	}

	static QuiltBlockSettings CONCRETE_SETTINGS = QuiltBlockSettings.create()
		.sounds(BlockSoundGroup.STONE)
		.mapColor(MapColor.STONE)
		.instrument(NoteBlockInstrument.BASEDRUM)
		.requiresTool()
		.strength(1.5F, 6.0F);

	public static void register(ModContainer mod)
	{
		registerBlock(mod, "ceiling_tile", QuiltBlockSettings.copyOf(Blocks.WHITE_WOOL), new QuiltItemSettings());

		registerBlock(mod, "concrete_wall",        CONCRETE_SETTINGS, new QuiltItemSettings());
		registerBlock(mod, "concrete_wall_top",    CONCRETE_SETTINGS, new QuiltItemSettings());
		registerBlock(mod, "concrete_wall_bottom", CONCRETE_SETTINGS, new QuiltItemSettings());

		registerBlock(mod, "ceiling", CONCRETE_SETTINGS, new QuiltItemSettings());
	}
}
