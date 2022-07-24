package hibi.blahaj;

import org.quiltmc.loader.api.ModContainer;

import net.minecraft.block.BlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class Common {

	public static final Identifier BLAHAJ_ID;
	public static final Identifier BREAD_ID;

	public void onInitialize(ModContainer mod) {
		Registry.register(Registry.ITEM, BLAHAJ_ID, new CuddlyItem(new Item.Settings().maxCount(1).group(ItemGroup.MISC)));
		Registry.register(Registry.ITEM, BREAD_ID, new CuddlyItem(new Item.Settings().maxCount(1).group(ItemGroup.MISC)));
	}

	static {
		BLAHAJ_ID = new Identifier("blahaj", "blue_shark");
		BREAD_ID = new Identifier("blahaj", "bread");
	}

	public static class CuddlyItem extends Item {
		public CuddlyItem(Settings settings) {
			super(settings);
		}
		
		@Override
		public float getMiningSpeedMultiplier(ItemStack stack, BlockState state) {
			return 0.25f;
		}
	}
}
