package hibi.blahaj;

import org.quiltmc.loader.api.ModContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Common {
	public static final Logger LOGGER = LoggerFactory.getLogger("Blahaj");

	public void onInitialize(ModContainer mod) {
		LOGGER.info("Hello Quilt world from {}!", mod.metadata().name());
	}
}
