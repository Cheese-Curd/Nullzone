package io.github.cheese_curd.nullzone;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import org.quiltmc.loader.api.ModContainer;

public class ModSounds
{
	public static final SoundEvent LIGHT_BUZZ = registerSoundEvent("light_buzz");

	private static SoundEvent registerSoundEvent(String id)
	{
		Identifier identifier = new Identifier(Nullzone.MOD_ID, id);

		Nullzone.LOGGER.info("Registered SoundEvent ({}:{})", Nullzone.MOD_ID, id);
		return Registry.register(Registries.SOUND_EVENT, identifier, SoundEvent.createVariableRangeEvent(identifier));
	}

	public static void registerSounds()
	{
		Nullzone.LOGGER.info("Registered SoundsEvents");
	}
}
