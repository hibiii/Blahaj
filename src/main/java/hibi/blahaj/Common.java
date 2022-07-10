package hibi.blahaj;

import org.quiltmc.loader.api.ModContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.UseAction;
import net.minecraft.util.registry.Registry;

public class Common {
	public static final Logger LOGGER = LoggerFactory.getLogger("Blahaj");

	public static final Identifier BLAHAJ_ID;
	public static final Item BLAHAJ_ITEM;

	public void onInitialize(ModContainer mod) {
		LOGGER.info("Hello Quilt world from {}!", mod.metadata().name());
	}

	static {
		BLAHAJ_ID = new Identifier("blahaj", "blahaj");
		BLAHAJ_ITEM =  Registry.register(Registry.ITEM, BLAHAJ_ID,
			new Item(new Item.Settings()
				.group(ItemGroup.MISC)
				.maxCount(1)
			) {
				public UseAction getUseAction(ItemStack stack) {
					return UseAction.CROSSBOW;
				};
			}
		);
	}
}
