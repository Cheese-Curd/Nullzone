package io.github.cheese_curd.nullzone;

import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.PersistentState;

public class PlayerPlacedStorage extends PersistentState
{
	private final LongOpenHashSet placed = new LongOpenHashSet();

	public void markPlaced(BlockPos pos)
	{
		placed.add(pos.asLong());
		markDirty();
	}

	public void clear(BlockPos pos)
	{
		placed.remove(pos.asLong());
		markDirty();
	}

	public boolean isPlaced(BlockPos pos)
	{
		return placed.contains(pos.asLong());
	}

	@Override
	public NbtCompound writeNbt(NbtCompound nbt)
	{
		nbt.putLongArray("placed", placed.toLongArray());
		return nbt;
	}

	public static PlayerPlacedStorage readNbt(NbtCompound nbt)
	{
		PlayerPlacedStorage storage = new PlayerPlacedStorage();

		if (nbt.contains("placed")) {
			for (long l : nbt.getLongArray("placed")) {
				storage.placed.add(l);
			}
		}

		return storage;
	}

	public static PlayerPlacedStorage get(ServerWorld world)
	{
		return world.getPersistentStateManager().getOrCreate(
			PlayerPlacedStorage::readNbt,
			PlayerPlacedStorage::new,
			"player_placed_storage"
		);
	}
}
