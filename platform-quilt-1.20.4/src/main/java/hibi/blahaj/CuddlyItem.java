package hibi.blahaj;

import java.util.List;

import net.minecraft.block.BlockState;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtString;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;

public class CuddlyItem extends Item {

	public static final String OWNER_KEY = "Owner";

	private final Text subtitle;

	public CuddlyItem(Settings settings, String subtitle) {
		super(settings);
		this.subtitle = subtitle == null? null: Text.translatable(subtitle).formatted(Formatting.field_1080); // Gray
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
				tooltip.add(Text.translatable("tooltip.blahaj.owner.rename", this.getName(), Text.literal(owner)).formatted(Formatting.field_1080));
			}
			else {
				tooltip.add(Text.translatable("tooltip.blahaj.owner.craft", Text.literal(owner)).formatted(Formatting.field_1080));
			}
		}
	}

	@Override
	public void onCraftByPlayer(ItemStack stack, World world, PlayerEntity player) {
		stack.setSubNbt(OWNER_KEY, NbtString.of(player.getName().getString()));
		super.onCraftByPlayer(stack, world, player);
	}
	
	@Override
	public float getMiningSpeedMultiplier(ItemStack stack, BlockState state) {
		return 0.25f;
	}
}
