package io.github.cheese_curd.nullzone;

import io.github.cheese_curd.nullzone.blocks.*;
import io.github.cheese_curd.nullzone.blocks.CeilingLight;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.block.*;
import net.minecraft.block.enums.NoteBlockInstrument;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.block.extensions.api.QuiltBlockSettings;
import org.quiltmc.qsl.item.setting.api.QuiltItemSettings;

import java.util.ArrayList;
import java.util.List;

public class ModBlocks
{
	public static final List<Block> MOD_BLOCKS = new ArrayList<>();

	static Block makeBlock(QuiltBlockSettings blockSettings) {
		return new NullzoneBlock(blockSettings);
	}

	static void registerBlock(ModContainer mod, String id, Block block, QuiltItemSettings itemSettings)
	{
		final Item  _ITEM  = new BlockItem(block, itemSettings);

		MOD_BLOCKS.add(block);

		Registry.register(Registries.BLOCK, new Identifier(mod.metadata().id(), id), block);
		Registry.register(Registries.ITEM,  new Identifier(mod.metadata().id(), id), _ITEM);

		Nullzone.LOGGER.info("Registered Block+BlockItem ({}:{})", mod.metadata().id(), id);
	}

	static QuiltBlockSettings CONCRETE_SETTINGS = QuiltBlockSettings.create()
		.sounds(BlockSoundGroup.STONE)
		.mapColor(MapColor.STONE)
		.instrument(NoteBlockInstrument.BASEDRUM)
		.requiresTool()
		.strength(1.5F, 6.0F);
	static QuiltBlockSettings OFFICE_WALL_SETTINGS = QuiltBlockSettings.copyOf(Blocks.OAK_PLANKS);

	public static final Block CEILING_TILE = makeBlock(QuiltBlockSettings.copyOf(Blocks.WHITE_WOOL));
	public static final SlabBlock CEILING_TILE_SLAB = new SlabBlock(QuiltBlockSettings.copyOf(Blocks.WHITE_WOOL));

	public static final SlabBlock CEILING_BEAM = new SlabBlock(QuiltBlockSettings.copyOf(Blocks.IRON_BLOCK).noCollision().nonOpaque());

	public static final Block CONCRETE_BRICKS            = makeBlock(CONCRETE_SETTINGS);
	public static final Block CONCRETE_FLOOR             = makeBlock(CONCRETE_SETTINGS);
	public static final Block DIRTY_CONCRETE_FLOOR       = makeBlock(CONCRETE_SETTINGS);
	public static final Block CONCRETE_WALL              = makeBlock(CONCRETE_SETTINGS);
//	public static final StairsBlock CONCRETE_WALL_STAIRS = new StairsBlock(CONCRETE_WALL.getDefaultState(), CONCRETE_SETTINGS);
	public static final Block CONCRETE_WALL_TOP          = makeBlock(CONCRETE_SETTINGS);
	public static final Block CONCRETE_WALL_BOTTOM       = makeBlock(CONCRETE_SETTINGS);

	public static final Block CEILING = makeBlock(CONCRETE_SETTINGS);

	public static final ToggleLight CEILING_LIGHT = new ToggleLight(QuiltBlockSettings.copyOf(Blocks.GLOWSTONE), 15, 0);
//	public static final CeilingLight CEILING_LIGHT = new CeilingLight(QuiltBlockSettings.copyOf(Blocks.GLOWSTONE));

	public static final SubflooringBlock SUBFLOORING = new SubflooringBlock(QuiltBlockSettings.copyOf(Blocks.OAK_PLANKS));

	public static final WetBlock WET_CEILING      = new WetBlock(CONCRETE_SETTINGS, 25F);
	public static final WetBlock WET_CEILING_TILE = new WetBlock(QuiltBlockSettings.copyOf(Blocks.WHITE_WOOL), 25F);
	public static final WetSlab  WET_CEILING_TILE_SLAB = new WetSlab(QuiltBlockSettings.copyOf(Blocks.WHITE_WOOL), 25F);

	// Office
	public static final Block OFFICE_WALL_TOP    = makeBlock(OFFICE_WALL_SETTINGS);
	public static final Block OFFICE_WALL        = makeBlock(OFFICE_WALL_SETTINGS);
	public static final Block OFFICE_WALL_BOTTOM = makeBlock(OFFICE_WALL_SETTINGS);

	public static final Block WALLPAPER_TOP    = new CuttableBlock(QuiltBlockSettings.copyOf(Blocks.BRICKS), ModItems.WALL_PAPER);
	public static final Block WALLPAPER        = new CuttableBlock(QuiltBlockSettings.copyOf(Blocks.BRICKS), ModItems.WALL_PAPER);
	public static final Block WALLPAPER_BOTTOM = makeBlock(QuiltBlockSettings.copyOf(Blocks.BRICKS));

	public static final Block OFFICE_CARPET = new WetRotatableBlock(QuiltBlockSettings.copyOf(Blocks.WHITE_WOOL));

	public static final Block AIRDUCT_TUBE = new PillarBlock(QuiltBlockSettings.copyOf(Blocks.WHITE_WOOL));

	public static final Block OFFICE_DOOR = new DoorBlock(QuiltBlockSettings.copyOf(Blocks.OAK_DOOR), BlockSetType.OAK);

	public static void register(ModContainer mod)
	{
		registerBlock(mod, "ceiling_tile", CEILING_TILE, Nullzone.ITEM_NO_SETTINGS);
		registerBlock(mod, "ceiling_tile_slab", CEILING_TILE_SLAB, Nullzone.ITEM_NO_SETTINGS);

		registerBlock(mod, "ceiling_beam", CEILING_BEAM, Nullzone.ITEM_NO_SETTINGS);

		registerBlock(mod, "concrete_bricks",      CONCRETE_BRICKS, Nullzone.ITEM_NO_SETTINGS);
		registerBlock(mod, "concrete_floor",       CONCRETE_FLOOR, Nullzone.ITEM_NO_SETTINGS);
		registerBlock(mod, "dirty_concrete_floor", DIRTY_CONCRETE_FLOOR, Nullzone.ITEM_NO_SETTINGS);
		registerBlock(mod, "concrete_wall",        CONCRETE_WALL, Nullzone.ITEM_NO_SETTINGS);
		registerBlock(mod, "concrete_wall_top",    CONCRETE_WALL_TOP, Nullzone.ITEM_NO_SETTINGS);
		registerBlock(mod, "concrete_wall_bottom", CONCRETE_WALL_BOTTOM, Nullzone.ITEM_NO_SETTINGS);

		registerBlock(mod, "ceiling", CEILING, Nullzone.ITEM_NO_SETTINGS);

		// Office
		registerBlock(mod, "office_wall_top",    OFFICE_WALL_TOP, Nullzone.ITEM_NO_SETTINGS);
		registerBlock(mod, "office_wall",        OFFICE_WALL, Nullzone.ITEM_NO_SETTINGS);
		registerBlock(mod, "office_wall_bottom", OFFICE_WALL_BOTTOM, Nullzone.ITEM_NO_SETTINGS);

		registerBlock(mod, "wallpaper_top",    WALLPAPER_TOP, Nullzone.ITEM_NO_SETTINGS);
		registerBlock(mod, "wallpaper",        WALLPAPER, Nullzone.ITEM_NO_SETTINGS);
		registerBlock(mod, "wallpaper_bottom", WALLPAPER_BOTTOM, Nullzone.ITEM_NO_SETTINGS);

		registerBlock(mod, "office_carpet", OFFICE_CARPET, Nullzone.ITEM_NO_SETTINGS);

		registerBlock(mod, "office_door", OFFICE_DOOR, Nullzone.ITEM_NO_SETTINGS);

		// Concrete Stairs
//		Registry.register(Registries.BLOCK, new Identifier(mod.metadata().id(), "concrete_wall_stairs"), CONCRETE_WALL_STAIRS);
//		Registry.register(Registries.ITEM,  new Identifier(mod.metadata().id(), "concrete_wall_stairs"), new BlockItem(CONCRETE_WALL_STAIRS, Nullzone.ITEM_NO_SETTINGS));


		// Sub Flooring
		registerBlock(mod, "subflooring", SUBFLOORING, Nullzone.ITEM_NO_SETTINGS);

		// Ceiling Light
		registerBlock(mod, "ceiling_light", CEILING_LIGHT, Nullzone.ITEM_NO_SETTINGS);

		// Wet Blocks
		registerBlock(mod, "wet_ceiling", WET_CEILING, Nullzone.ITEM_NO_SETTINGS);
		registerBlock(mod, "wet_ceiling_tile", WET_CEILING_TILE, Nullzone.ITEM_NO_SETTINGS);
		registerBlock(mod, "wet_ceiling_tile_slab", WET_CEILING_TILE_SLAB, Nullzone.ITEM_NO_SETTINGS);


		registerBlock(mod, "airduct_tube", AIRDUCT_TUBE, Nullzone.ITEM_NO_SETTINGS);


		ItemGroupEvents.modifyEntriesEvent(Nullzone.NULLZONE_GROUP_KEY).register(entries -> {
			for (Block block : MOD_BLOCKS)
				entries.addItem(block.asItem());
		});
	}
}
