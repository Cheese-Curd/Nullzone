package io.github.cheese_curd.nullzone.mixin;

import io.github.cheese_curd.nullzone.Nullzone;
import net.minecraft.client.resource.SplashTextResourceSupplier;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.profiler.Profiler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;

@Mixin(SplashTextResourceSupplier.class)
public class SplashTextMixin
{
	@Inject(method = "prepare", at = @At("RETURN"), cancellable = true)
	private void addSplashes(ResourceManager resourceManager, Profiler profiler, CallbackInfoReturnable<List<String>> cir)
	{
		List<String> splashes = new ArrayList<>(cir.getReturnValue());

		int oldSplashCount = splashes.size();

		splashes.add("Liminal!");
		splashes.add("Wet and Moldy!");
		splashes.add("Where am I?");
		splashes.add("Empty, void of life.");
		splashes.add("You shouldn't be here.");
		splashes.add("NullPointerException");
		splashes.add("null");
		splashes.add("Please remain calm.");
		splashes.add("Index §k-1§r out of bounds for length §k99§r");

		Nullzone.LOGGER.info("Successfully added {} Splashes!", splashes.size() - oldSplashCount);

		cir.setReturnValue(splashes);
	}
}
