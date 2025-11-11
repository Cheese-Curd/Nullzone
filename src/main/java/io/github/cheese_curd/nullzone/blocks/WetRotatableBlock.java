package io.github.cheese_curd.nullzone.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.PotionUtil;
import net.minecraft.potion.Potions;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WetRotatableBlock extends RotatableBlock
{
	public static final BooleanProperty WET = BooleanProperty.of("wet");

	public WetRotatableBlock(Settings settings) {
		super(settings);

		setDefaultState(getStateManager().getDefaultState()
			.with(WET, false));
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		super.appendProperties(builder);
		builder.add(WET);
	}

	boolean holdingWaterBottle(PlayerEntity player) {
		ItemStack main = player.getMainHandStack();
		ItemStack off = player.getOffHandStack();

		return (main.getItem() == Items.POTION && PotionUtil.getPotion(main) == Potions.WATER)
			|| (off.getItem() == Items.POTION && PotionUtil.getPotion(off) == Potions.WATER);
	}

	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		if (state.get(WET)) return ActionResult.PASS;

		if (holdingWaterBottle(player))
		{
			if (!world.isClient())
			{
				world.playSound(null, pos, SoundEvents.ITEM_BOTTLE_EMPTY, SoundCategory.BLOCKS, 1, 1);
				world.addBlockBreakParticles(pos, state);
				world.setBlockState(pos, state.with(WET, true).with(FACING, state.get(FACING)));

				if (!player.getAbilities().creativeMode)
				{
					ItemStack stack = player.getMainHandStack();
					if (stack.getItem() != Items.POTION)
						stack = player.getOffHandStack();

					stack.decrement(1);

					ItemStack empty = new ItemStack(Items.GLASS_BOTTLE);

					if (!player.getInventory().insertStack(empty))
						player.dropItem(empty, false);
				}

				return ActionResult.CONSUME;
			}
		}

		return ActionResult.PASS;
	}
}
