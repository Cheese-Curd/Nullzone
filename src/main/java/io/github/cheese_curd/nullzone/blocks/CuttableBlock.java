package io.github.cheese_curd.nullzone.blocks;

import io.github.cheese_curd.nullzone.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class CuttableBlock extends NullzoneBlock
{
	private final Item  droppedItem;
	private final Block cutBlock;

	public CuttableBlock(Settings settings, Item droppedItem, Block cutBlock) {
		super(settings);

		this.droppedItem = droppedItem;
		this.cutBlock = cutBlock;
	}

	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		ItemStack held = player.getEquippedStack(EquipmentSlot.MAINHAND);

		if (held.isOf(Items.SHEARS))
		{
			if (!world.isClient())
			{
				world.playSound(null, pos, SoundEvents.ENTITY_SHEEP_SHEAR, SoundCategory.BLOCKS, 1, 1);
				world.addBlockBreakParticles(pos, state);
				world.setBlockState(pos, cutBlock.getDefaultState());
				Block.dropStack(world, pos, new ItemStack(droppedItem));

				held.damage(2, player, p -> p.sendToolBreakStatus(hand));
			}

			return ActionResult.SUCCESS;
		}

		return ActionResult.PASS;
	}
}
