package io.github.cheese_curd.nullzone.mixin;

import io.github.cheese_curd.nullzone.ModLimGen;
import net.minecraft.network.packet.s2c.play.GameStateUpdateS2CPacket;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.ServerWorldProperties;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/*
	Thank LudoCrypt for making this
	https://github.com/LudoCrypt/The-Corners/blob/main/src/main/java/net/ludocrypt/corners/mixin/ServerWorldMixin.java

	This has been taken and reworked to work with this project (WITH PERMISSION!!).

	LOOK AT THEIR CODE!!!!!!!!! (it's better)
 */
@Mixin(ServerWorld.class)
public class ServerWorldMixin
{
	@Shadow @Final
	private ServerWorldProperties worldProperties;

	@Inject(method = "tickWeather", at = @At("HEAD"))
	private void nullzoneWeather(CallbackInfo cbi)
	{
		ServerWorld world = ((ServerWorld) (Object) this); // This is witchery to me

		if (world.getRegistryKey().equals(ModLimGen.ABANDONED_OFFICES_KEY)) {
			// Make it rain
			this.worldProperties.setRainTime(0);
			this.worldProperties.setRaining(true);
			this.worldProperties.setThunderTime(0);
			this.worldProperties.setThundering(false);

			world.setRainGradient(2.0F);

			// Update Clients (Witch craft)
			if (!world.isRaining())
			{
				world
					.getServer()
					.getPlayerManager()
					.sendToDimension(new GameStateUpdateS2CPacket(GameStateUpdateS2CPacket.RAIN_GRADIENT_CHANGED, 2.0F),
						world.getRegistryKey());
				world
					.getServer()
					.getPlayerManager()
					.sendToDimension(new GameStateUpdateS2CPacket(GameStateUpdateS2CPacket.RAIN_STARTED, 0.0F),
						world.getRegistryKey());
			}
		}
		else if (world.getRegistryKey().getValue().getNamespace().equals("nullzone"))
		{
			// No Rain
			this.worldProperties.setRainTime(0);
			this.worldProperties.setRaining(false);
			this.worldProperties.setThunderTime(0);
			this.worldProperties.setThundering(false);

			world.setRainGradient(0.0F);

			// Update Clients
			if (world.isRaining()) {
				world
					.getServer()
					.getPlayerManager()
					.sendToDimension(new GameStateUpdateS2CPacket(GameStateUpdateS2CPacket.RAIN_GRADIENT_CHANGED, 0.0F),
						((ServerWorld) (Object) this).getRegistryKey());
				world
					.getServer()
					.getPlayerManager()
					.sendToDimension(new GameStateUpdateS2CPacket(GameStateUpdateS2CPacket.RAIN_STOPPED, 0.0F),
						world.getRegistryKey());
			}
		}
	}

	@Inject(method = "tickTime", at = @At("HEAD"), cancellable = true)
	private void nullzoneTime(CallbackInfo ci) {
		ServerWorld world = (ServerWorld) (Object) this;

		if (world.getRegistryKey().getValue().getNamespace().equals("nullzone")) {
			long time = world.getTimeOfDay();

			world.setTimeOfDay(time + 4); // 5 Minute daylight cycle
			ci.cancel();
		}
	}
}
