package io.github.cheese_curd.nullzone.world.maze;

import net.ludocrypt.limlib.api.world.maze.MazeComponent;
import net.ludocrypt.limlib.api.world.maze.MazeGenerator;
import net.minecraft.util.math.BlockPos;

import java.util.HashMap;

/*
	THIS HAS TAKEN A LOT OF INSPIRATION FROM LUDO'S GrandMazeGenerator!!
	https://github.com/LudoCrypt/The-Corners/blob/main/src/main/java/net/ludocrypt/corners/world/maze/GrandMazeGenerator.java
 */
public class WholeMaze extends MazeGenerator<MazeComponent>
{
	public final HashMap<BlockPos, MazeComponent> mazeMap = new HashMap<BlockPos, MazeComponent>(30);

	public WholeMaze(int width, int height, int thicknessX, int thicknessY, long seedModifier, int dilation) {
		super(width * dilation, height * dilation, thicknessX, thicknessY, seedModifier);
	}
}
