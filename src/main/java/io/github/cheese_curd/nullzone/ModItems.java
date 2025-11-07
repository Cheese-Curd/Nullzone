package io.github.cheese_curd.nullzone;

import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.item.setting.api.QuiltItemSettings;

public class ModItems
{

	static Item makeItem(QuiltItemSettings itemSettings) {
		return new Item(itemSettings);
	}

	public static final Item WALL_PAPER = makeItem(Nullzone.ITEM_NO_SETTINGS);

	public static void register(ModContainer mod)
	{
		Registry.register(Registries.ITEM, new Identifier(mod.metadata().id(), "wall_paper"), WALL_PAPER);
	}
}
