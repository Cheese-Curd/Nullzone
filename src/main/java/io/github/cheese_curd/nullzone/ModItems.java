package io.github.cheese_curd.nullzone;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.block.extensions.api.QuiltBlockSettings;
import org.quiltmc.qsl.item.setting.api.QuiltItemSettings;

import java.util.ArrayList;
import java.util.List;

public class ModItems
{

	static Item makeItem(QuiltItemSettings itemSettings) {
		Item item = new Item(itemSettings);

		return item;
	}

	public static final Item EXAMPLE_ITEM = makeItem(Nullzone.ITEM_NO_SETTINGS);

	public static void register(ModContainer mod)
	{
		Registry.register(Registries.ITEM, new Identifier(mod.metadata().id(), "example_item"), EXAMPLE_ITEM);
	}
}
