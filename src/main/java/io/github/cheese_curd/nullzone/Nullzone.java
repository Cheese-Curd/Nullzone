package io.github.cheese_curd.nullzone;

import io.github.cheese_curd.nullzone.world.biomes.chunkgen.ConcreteHallsChunkGen;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Nullzone implements ModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("Nullzone");

	public static final String MOD_ID = "nullzone";

    @Override
    public void onInitialize(ModContainer mod)
    {
        LOGGER.info("{} Initializing...", mod.metadata().name());

	    ModItems.register(mod);
	    ModBlocks.register(mod);
	    ConcreteHallsChunkGen.init();
    }
}
