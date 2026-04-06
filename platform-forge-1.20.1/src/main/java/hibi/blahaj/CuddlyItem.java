package hibi.blahaj;

import java.util.List;
import java.util.function.Consumer;

import javax.annotation.Nullable;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;

public class CuddlyItem extends Item {

	public static final String OWNER_KEY = "Owner";

	private final Component subtitle;

	public CuddlyItem(Properties properties, String subtitle) {
		super(properties);
		this.subtitle = subtitle == null ? null : Component.translatable(subtitle).withStyle(ChatFormatting.GRAY);
	}

	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
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
		if (player != null) { // compensate for auto-crafter mods
			stack.addTagElement(OWNER_KEY, StringTag.valueOf(player.getName().getString()));
		}
		super.onCraftedBy(stack, level, player);
	}

	@Override
	public float getDestroySpeed(ItemStack stack, BlockState state) {
		return 0.25f;
	}

	// --- Placement ---

	@Override
	public InteractionResult useOn(UseOnContext context) {
		Player player = context.getPlayer();
		if (player == null) return InteractionResult.PASS;
		Level level = context.getLevel();
		BlockPos placePos = context.getClickedPos().relative(context.getClickedFace());
		BlockState placeState = level.getBlockState(placePos);

		if (!placeState.canBeReplaced()) return InteractionResult.PASS;

		// Check that our block won't intersect the clicked block's collision
		// (e.g. placing on top of a slab - the air is replaceable, but make sure
		// there's room above the solid surface)
		if (!level.isClientSide()) {
			Direction facing = player.getDirection().getOpposite();
			int rotation = CuddlyBlock.directionToRotation(facing);
			BlockState state = Blahaj.CUDDLY_BLOCK.get().defaultBlockState()
				.setValue(CuddlyBlock.ROTATION, rotation);
			level.setBlock(placePos, state, 3);
			if (level.getBlockEntity(placePos) instanceof CuddlyBlockEntity be) {
				be.setItem(context.getItemInHand());
			}
			if (!player.isCreative()) {
				context.getItemInHand().shrink(1);
			}
		}
		return InteractionResult.sidedSuccess(level.isClientSide());
	}

	// --- Pet interaction (hold right-click in air) ---

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
		player.startUsingItem(hand);
		return InteractionResultHolder.consume(player.getItemInHand(hand));
	}

	@Override
	public int getUseDuration(ItemStack stack) {
		return 72000; // Endless (1 hour)
	}

	@Override
	public UseAnim getUseAnimation(ItemStack stack) {
		// NONE keeps blahaj in the normal held position.
		// Third person arm poses are overridden by the HumanoidModelMixin.
		return UseAnim.NONE;
	}

	@Override
	public void onUseTick(Level level, LivingEntity entity, ItemStack stack, int remainingTicks) {
		int elapsed = getUseDuration(stack) - remainingTicks;
		// Spawn hearts periodically while petting
		if (!level.isClientSide() && elapsed > 0 && elapsed % 15 == 0) {
			((ServerLevel) level).sendParticles(ParticleTypes.HEART,
				entity.getX(), entity.getEyeY() + 0.2, entity.getZ(),
				2, 0.2, 0.2, 0.2, 0.0);
		}
	}

	@Override
	public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
		// Final burst of hearts
		if (!level.isClientSide()) {
			((ServerLevel) level).sendParticles(ParticleTypes.HEART,
				entity.getX(), entity.getEyeY() + 0.2, entity.getZ(),
				5, 0.3, 0.3, 0.3, 0.0);
		}
		return stack;
	}

	@Override
	public void initializeClient(Consumer<IClientItemExtensions> consumer) {
		consumer.accept(new IClientItemExtensions() {
			@Override
			public net.minecraft.client.model.HumanoidModel.ArmPose getArmPose(
					LivingEntity entity, InteractionHand hand, ItemStack stack) {
				// Forge hook for third person arm pose - CROSSBOW_CHARGE = hug pose
				return net.minecraft.client.model.HumanoidModel.ArmPose.CROSSBOW_CHARGE;
			}

			@Override
			public boolean applyForgeHandTransform(com.mojang.blaze3d.vertex.PoseStack poseStack,
					net.minecraft.client.player.LocalPlayer player,
					net.minecraft.world.entity.HumanoidArm arm,
					ItemStack itemStack, float partialTick, float equipProcess, float swingProcess) {
				if (player.isUsingItem() && player.getUseItem().getItem() instanceof CuddlyItem) {
					float ticks = player.getTicksUsingItem() + partialTick;
					// Gentle side-to-side rock in the hand
					float rock = (float) Math.sin(ticks * 0.4) * 2.5F;
					poseStack.mulPose(com.mojang.math.Axis.ZP.rotationDegrees(rock));
				}
				return false;
			}
		});
	}
}
