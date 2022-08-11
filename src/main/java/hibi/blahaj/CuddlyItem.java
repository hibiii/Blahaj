package hibi.blahaj;

import java.util.List;

import net.minecraft.block.BlockState;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtString;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;

public class CuddlyItem extends Item {

	public static final String OWNER_KEY = "Owner";

	private final Text subtitle;

	public CuddlyItem(Settings settings, String subtitle) {
		super(settings);
		this.subtitle = subtitle == null? null: new TranslatableText(subtitle).formatted(Formatting.GRAY);
	}

	@Override
	public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext context) {
		if(this.subtitle != null) {
			tooltip.add(this.subtitle);
		}
		if(stack.hasNbt()) {
			NbtCompound nbt = stack.getNbt();
			String owner = nbt.getString(OWNER_KEY);
			if(owner == "") {
				return;
			}
			if(stack.hasCustomName()) {
				tooltip.add(new TranslatableText("tooltip.blahaj.owner.rename", this.getName(), new LiteralText(owner)).formatted(Formatting.GRAY));
			}
			else {
				tooltip.add(new TranslatableText("tooltip.blahaj.owner.craft", new LiteralText(owner)).formatted(Formatting.GRAY));
			}
		}
	}

	@Override
	public void onCraft(ItemStack stack, World world, PlayerEntity player) {
		if(player != null) { // compensate for auto-crafter mods
			stack.setSubNbt(OWNER_KEY, NbtString.of(player.getName().getString()));
		}
		super.onCraft(stack, world, player);
	}

	@Override
	public float getMiningSpeedMultiplier(ItemStack stack, BlockState state) {
		return 0.25f;
	}
}
