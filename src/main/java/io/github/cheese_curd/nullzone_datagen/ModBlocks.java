package io.github.cheese_curd.nullzone_datagen;

import io.github.cheese_curd.nullzone_datagen.blocks.SubflooringBlock;
import io.github.cheese_curd.nullzone_datagen.blocks.ToggleLight;
import io.github.cheese_curd.nullzone_datagen.blocks.WetBlock;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.*;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public class ModBlocks
{
	public static final List<Block> MOD_BLOCKS        = new ArrayList<>();
	public static final List<Block> MOD_BLOCKS_MODELS = new ArrayList<>();
    public static final List<Block> MOD_BLOCKS_TB_MODELS = new ArrayList<>();

	static Block makeBlock(Block block) {
        MOD_BLOCKS_MODELS.add(block);

        return block;
	}

	static void registerBlock(String id, Block block, FabricItemSettings itemSettings)
	{
		final Item  _ITEM  = new BlockItem(block, itemSettings);

//		if (addToCreativeMenu)
		MOD_BLOCKS.add(block);

		Registry.register(Registries.BLOCK, new Identifier(Nullzone.MOD_ID, id), block);
		Registry.register(Registries.ITEM,  new Identifier(Nullzone.MOD_ID, id), _ITEM);
	}

	static FabricBlockSettings CONCRETE_SETTINGS = FabricBlockSettings.create()
		.sounds(BlockSoundGroup.STONE)
		.mapColor(MapColor.GRAY)
		.requiresTool()
		.strength(1.5F, 6.0F);
	static FabricBlockSettings OFFICE_WALL_SETTINGS = FabricBlockSettings.copyOf(Blocks.OAK_PLANKS);

	public static final Block CEILING_TILE = makeBlock(new Block(FabricBlockSettings.copyOf(Blocks.WHITE_WOOL)));

	public static final Block CONCRETE_FLOOR             = makeBlock(new Block(CONCRETE_SETTINGS));
	public static final Block DIRTY_CONCRETE_FLOOR       = makeBlock(new Block(CONCRETE_SETTINGS));
	public static final Block CONCRETE_WALL              = makeBlock(new Block(CONCRETE_SETTINGS));
//	public static final StairsBlock CONCRETE_WALL_STAIRS = new StairsBlock(CONCRETE_WALL.getDefaultState(), CONCRETE_SETTINGS);
	public static final Block CONCRETE_WALL_TOP          = new Block(CONCRETE_SETTINGS);
	public static final Block CONCRETE_WALL_BOTTOM       = new Block(CONCRETE_SETTINGS);

	public static final Block CEILING = makeBlock(new Block(CONCRETE_SETTINGS));

//	public static final ToggleLight CEILING_LIGHT = (ToggleLight) makeBlock(new ToggleLight(FabricBlockSettings.copyOf(Blocks.GLOWSTONE), 15, 0));

	public static final SubflooringBlock SUBFLOORING = (SubflooringBlock) makeBlock(new SubflooringBlock(FabricBlockSettings.copyOf(Blocks.OAK_PLANKS)));

	public static final WetBlock WET_CEILING = (WetBlock) makeBlock(new WetBlock(CONCRETE_SETTINGS, 25F));
	public static final WetBlock WET_CEILING_TILE = (WetBlock) makeBlock(new WetBlock(FabricBlockSettings.copyOf(Blocks.WHITE_WOOL), 25F));

	// Office
	public static final Block OFFICE_WALL_TOP    = new Block(OFFICE_WALL_SETTINGS);
	public static final Block OFFICE_WALL        = makeBlock(new Block(OFFICE_WALL_SETTINGS));
	public static final Block OFFICE_WALL_BOTTOM = new Block(OFFICE_WALL_SETTINGS);

	public static final Block WALLPAPER_TOP    = new Block(OFFICE_WALL_SETTINGS);
	public static final Block WALLPAPER        = makeBlock(new Block(OFFICE_WALL_SETTINGS));
	public static final Block WALLPAPER_BOTTOM = new Block(OFFICE_WALL_SETTINGS);

	public static final Block OFFICE_CARPET = makeBlock(new GlazedTerracottaBlock(FabricBlockSettings.copyOf(Blocks.WHITE_WOOL)));

	public static void register()
	{
        MOD_BLOCKS_TB_MODELS.add(CONCRETE_WALL_TOP);
        MOD_BLOCKS_TB_MODELS.add(CONCRETE_WALL_BOTTOM);

        MOD_BLOCKS_TB_MODELS.add(OFFICE_WALL_TOP);
        MOD_BLOCKS_TB_MODELS.add(OFFICE_WALL_BOTTOM);

        MOD_BLOCKS_TB_MODELS.add(WALLPAPER_TOP);
        MOD_BLOCKS_TB_MODELS.add(WALLPAPER_BOTTOM);

		registerBlock("ceiling_tile", CEILING_TILE, Nullzone.ITEM_NO_SETTINGS);

		registerBlock("concrete_floor",       CONCRETE_FLOOR, Nullzone.ITEM_NO_SETTINGS);
		registerBlock("dirty_concrete_floor", DIRTY_CONCRETE_FLOOR, Nullzone.ITEM_NO_SETTINGS);
		registerBlock("concrete_wall",        CONCRETE_WALL, Nullzone.ITEM_NO_SETTINGS);
		registerBlock("concrete_wall_top",    CONCRETE_WALL_TOP, Nullzone.ITEM_NO_SETTINGS);
		registerBlock("concrete_wall_bottom", CONCRETE_WALL_BOTTOM, Nullzone.ITEM_NO_SETTINGS);

		registerBlock("ceiling", CEILING, Nullzone.ITEM_NO_SETTINGS);

		// Office
		registerBlock("office_wall_top",    OFFICE_WALL_TOP, Nullzone.ITEM_NO_SETTINGS);
		registerBlock("office_wall",        OFFICE_WALL, Nullzone.ITEM_NO_SETTINGS);
		registerBlock("office_wall_bottom", OFFICE_WALL_BOTTOM, Nullzone.ITEM_NO_SETTINGS);

		registerBlock("wallpaper_top",    WALLPAPER_TOP, Nullzone.ITEM_NO_SETTINGS);
		registerBlock("wallpaper",        WALLPAPER, Nullzone.ITEM_NO_SETTINGS);
		registerBlock("wallpaper_bottom", WALLPAPER_BOTTOM, Nullzone.ITEM_NO_SETTINGS);

		registerBlock("office_carpet", OFFICE_CARPET, Nullzone.ITEM_NO_SETTINGS);

		// Concrete Stairs
//		Registry.register(Registries.BLOCK, new Identifier(mod.metadata().id(), "concrete_wall_stairs"), CONCRETE_WALL_STAIRS);
//		Registry.register(Registries.ITEM,  new Identifier(mod.metadata().id(), "concrete_wall_stairs"), new BlockItem(CONCRETE_WALL_STAIRS, Nullzone.ITEM_NO_SETTINGS));


		// Sub Flooring
		registerBlock("subflooring", SUBFLOORING, Nullzone.ITEM_NO_SETTINGS);

		// Ceiling Light
//		registerBlock("ceiling_light", CEILING_LIGHT, Nullzone.ITEM_NO_SETTINGS);

		// Wet Blocks
		registerBlock("wet_ceiling", WET_CEILING, Nullzone.ITEM_NO_SETTINGS);
		registerBlock("wet_ceiling_tile", WET_CEILING_TILE, Nullzone.ITEM_NO_SETTINGS);
	}
}
