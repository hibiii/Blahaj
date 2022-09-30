package hibi.blahaj;

import net.minecraft.ChatFormatting;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;

import java.util.List;
import java.util.function.Consumer;

public class CuddlyItem extends Item {

	public static final String OWNER_KEY = "Owner";

	private final Component subtitle;

	public CuddlyItem(Properties settings, String subtitle) {
		super(settings);
		this.subtitle = subtitle == null ? null : Component.translatable(subtitle).withStyle(ChatFormatting.GRAY);
	}

	@Override
	public void appendHoverText(ItemStack stack, Level world, List<Component> tooltip, TooltipFlag context) {
		if(this.subtitle != null) {
			tooltip.add(this.subtitle);
		}
		if(stack.hasTag()) {
			CompoundTag nbt = stack.getTag();
			String owner = nbt.getString(OWNER_KEY);
			if(owner == "") {
				return;
			}
			if(stack.hasCustomHoverName()) {
				tooltip.add(Component.translatable("tooltip.blahaj.owner.rename", this.getDescription(), Component.literal(owner)).withStyle(ChatFormatting.GRAY));
			}
			else {
				tooltip.add(Component.translatable("tooltip.blahaj.owner.craft", Component.literal(owner)).withStyle(ChatFormatting.GRAY));
			}
		}
	}

	@Override
	public void onCraftedBy(ItemStack stack, Level world, Player player) {
		if(player != null) { // compensate for auto-crafter mods
			stack.addTagElement(OWNER_KEY, StringTag.valueOf(player.getName().getString()));
		}
		super.onCraftedBy(stack, world, player);
	}

	@Override
	public float getDestroySpeed(ItemStack stack, BlockState state) {
		return 0.25f;
	}

	@Override
	public void initializeClient(Consumer<IClientItemExtensions> consumer) {
		consumer.accept(new IClientItemExtensions() {
			@Override
			public HumanoidModel.ArmPose getArmPose(LivingEntity entityLiving, InteractionHand hand, ItemStack itemStack) {
				return ClientStuff.CUDDLE_BLAHAJ;
			}
		});
	}
}
