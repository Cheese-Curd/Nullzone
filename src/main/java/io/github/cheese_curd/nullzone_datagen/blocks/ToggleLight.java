package io.github.cheese_curd.nullzone_datagen.blocks;

public class ToggleLight extends AbstractToggledBlock
{
	public ToggleLight(Settings settings, Integer maxLuminance, Integer minLuminance) {
		super(settings.luminance(state -> state.get(ON) ? maxLuminance : minLuminance));
	}
}
