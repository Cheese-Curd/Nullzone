package io.github.cheese_curd.nullzone.world.maze;

import com.mojang.datafixers.util.Pair;
import net.ludocrypt.limlib.api.world.Manipulation;
import net.ludocrypt.limlib.api.world.maze.MazeComponent;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.random.RandomGenerator;

import java.util.List;

public class BigRoomMaze extends MazeComponent
{
	RandomGenerator random;
	List<BigRoom> bigRooms;

	public BigRoomMaze(int width, int height, List<BigRoom> bigRooms, RandomGenerator random)
	{
		super(width, height);

		this.bigRooms = bigRooms;
		this.random   = random;
	}

	void addRoom(BigRoom room, Manipulation manipulation)
	{
		boolean shouldFlip = manipulation.getRotation() == BlockRotation.CLOCKWISE_90
			|| manipulation.getRotation() == BlockRotation.COUNTERCLOCKWISE_90;

		int realWidth  = shouldFlip ? room.height : room.width;
		int realHeight = shouldFlip ? room.width  : room.height;

		int x = random.nextInt((width  - realWidth)  - 2) + 1;
		int y = random.nextInt((height - realHeight) - 2) + 1;

		for (int i = x; i < x + realWidth; i++)
		{
			for (int j = y; j < y + realHeight; j++)
			{
				CellState cellState = this.cellState(i, j);

				if (cellState.getExtra().getOrDefault("room", new NbtCompound()).getBoolean("isRoom"))
					return;
			}
		}

		for (int i = x; i < x + realWidth; i++)
		{
			for (int j = y; j < y + realHeight; j++)
			{
				CellState cellState = this.cellState(i, j);

				NbtCompound compound = new NbtCompound();
				compound.putBoolean("isRoom", true);
				compound.putString("roomName", room.name);
				compound.putInt("originX", x);
				compound.putInt("originY", y);
				compound.putInt("manipulation", manipulation.ordinal());

				cellState.getExtra().put("room", compound);
			}
		}

		for (CellState exit : room.exits)
		{
			CellState cellState = this.cellState(exit.getPosition());

			cellState.up(exit.goesUp());
			cellState.left(exit.goesLeft());
			cellState.down(exit.goesDown());
			cellState.right(exit.goesRight());
		}
	}

	@Override
	public void create()
	{
		addRoom(bigRooms.get(random.nextInt(bigRooms.size())), Manipulation.random(random));
		addRoom(bigRooms.get(random.nextInt(bigRooms.size())), Manipulation.random(random));


	}

	public static class BigRoom
	{
		int width;
		int height;
		String name;

		List<CellState> exits;
	}
}
