package hibi.blahaj;

import java.util.List;

import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.util.FakePlayer;
import org.jetbrains.annotations.Nullable;

public class CuddlyItem extends Item {

	public static final String OWNER_KEY = "Owner";

	private final Component subtitle;

	public CuddlyItem(Properties properties, String subtitle) {
		super(properties);
		this.subtitle = subtitle == null? null: Component.translatable(subtitle).withStyle(ChatFormatting.GRAY);
	}

	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag context) {
		if (this.subtitle != null) {
			tooltip.add(this.subtitle);
		}
		if (stack.hasTag()) {
			CompoundTag nbt = stack.getTag();
			String owner = nbt.getString(OWNER_KEY);
			if (owner == "") {
				return;
			}
			if (stack.hasCustomHoverName()) {
				tooltip.add(Component.translatable("tooltip.blahaj.owner.rename", this.getDescription(), Component.literal(owner)).withStyle(ChatFormatting.GRAY));
			}
			else {
				tooltip.add(Component.translatable("tooltip.blahaj.owner.craft", Component.literal(owner)).withStyle(ChatFormatting.GRAY));
			}
		}
	}

	@Override
	public void onCraftedBy(ItemStack stack, Level level, Player player) {
		if (player != null && !(player instanceof FakePlayer)) { // compensate for auto-crafter mods
			stack.addTagElement(OWNER_KEY, StringTag.valueOf(player.getName().getString()));
		}
		super.onCraftedBy(stack, level, player);
	}

	@Override
	public float getDestroySpeed(ItemStack stack, BlockState state) {
		return super.getDestroySpeed(stack, state);
	}
}
