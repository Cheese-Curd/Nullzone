package io.github.cheese_curd.nullzone;

import net.minecraft.registry.Holder;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public class ModSounds
{
	public static final Holder.Reference<SoundEvent> LIGHT_BUZZ = registerHolderSoundEvent("light_buzz");

	private static SoundEvent registerSoundEvent(String id)
	{
		Identifier identifier = new Identifier(Nullzone.MOD_ID, id);

		Nullzone.LOGGER.info("Registered SoundEvent ({}:{})", Nullzone.MOD_ID, id);
		return Registry.register(Registries.SOUND_EVENT, identifier, SoundEvent.createVariableRangeEvent(identifier));
	}

	private static Holder.Reference<SoundEvent> registerHolderSoundEvent(String id)
	{
		Identifier identifier = new Identifier(Nullzone.MOD_ID, id);

		Nullzone.LOGGER.info("[HOLDER] Registered SoundEvent ({}:{})", Nullzone.MOD_ID, id);

		return Registry
			.registerHolder(Registries.SOUND_EVENT, identifier,
				SoundEvent.createVariableRangeEvent(identifier));
	}


	public static void registerSounds()
	{
		Nullzone.LOGGER.info("Registered SoundsEvents");
	}
}
