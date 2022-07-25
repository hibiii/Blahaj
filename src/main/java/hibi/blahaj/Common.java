package hibi.blahaj;

import java.util.List;

import org.quiltmc.loader.api.ModContainer;

import net.minecraft.block.BlockState;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

public class Common {

	public static final Identifier BLAHAJ_ID;
	public static final Identifier BREAD_ID;

	public void onInitialize(ModContainer mod) {
		Registry.register(Registry.ITEM, BLAHAJ_ID, new CuddlyItem(new Item.Settings().maxCount(1).group(ItemGroup.MISC), "item.blahaj.blue_shark.tooltip"));
		Registry.register(Registry.ITEM, BREAD_ID, new CuddlyItem(new Item.Settings().maxCount(1).group(ItemGroup.MISC), null));
	}

	static {
		BLAHAJ_ID = new Identifier("blahaj", "blue_shark");
		BREAD_ID = new Identifier("blahaj", "bread");
	}

	public static class CuddlyItem extends Item {
		private final Text subtitle;
		public CuddlyItem(Settings settings, String subtitle) {
			super(settings);
			this.subtitle = subtitle == null? null: Text.translatable(subtitle).formatted(Formatting.GRAY);
		}

		@Override
		public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext context) {
			if(this.subtitle != null) {
				tooltip.add(this.subtitle);
			}
		}
		
		@Override
		public float getMiningSpeedMultiplier(ItemStack stack, BlockState state) {
			return 0.25f;
		}
	}
}
