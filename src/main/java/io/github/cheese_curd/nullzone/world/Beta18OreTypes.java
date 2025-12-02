package io.github.cheese_curd.nullzone.world;

public enum Beta18OreTypes
{
	COAL(0),
	IRON(1),
	GOLD(2),
	REDSTONE(3),
	LAPIS_LAZULI(4),
	DIAMOND(5);

	private final int value;

	Beta18OreTypes(int value) { this.value = value; }

	public int getValue() { return value; }
}
