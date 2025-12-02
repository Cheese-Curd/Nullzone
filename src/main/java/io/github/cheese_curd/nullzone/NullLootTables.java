package io.github.cheese_curd.nullzone;

import com.google.common.collect.Sets;
import net.minecraft.loot.LootTables;
import net.minecraft.util.Identifier;

import java.util.Set;

public class NullLootTables
{
	private static final Set<Identifier> LOOT_TABLES = Sets.newHashSet();
	public static Identifier STONESTILLS_STORAGE;

	private static Identifier register(String id) {
		return registerLootTable(new Identifier(Nullzone.MOD_ID, id));
	}
	private static Identifier registerLootTable(Identifier id) {
		if (LOOT_TABLES.add(id))
			return id;
		else
			throw new IllegalArgumentException(id + " is already a registered loot table");
	}

	static {
		STONESTILLS_STORAGE = register("chests/stonestills_storage");
	}
}
