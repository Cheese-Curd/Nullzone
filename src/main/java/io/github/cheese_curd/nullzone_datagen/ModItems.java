package io.github.cheese_curd.nullzone_datagen;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public class ModItems
{
	public static final List<Item> MOD_ITEMS = new ArrayList<>();

	static Item makeItem(FabricItemSettings itemSettings, boolean autoGenModel) {
		Item item = new Item(itemSettings);

		if (autoGenModel)
			MOD_ITEMS.add(item);

		return item;
	}

	public static final Item WALL_PAPER = makeItem(Nullzone.ITEM_NO_SETTINGS, true);

	public static void register()
	{
		Registry.register(Registries.ITEM, new Identifier(Nullzone.MOD_ID, "wall_paper"), WALL_PAPER);
	}
}
