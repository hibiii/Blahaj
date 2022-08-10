package hibi.blahaj.mixin;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;

import net.minecraft.world.InteractionHand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import hibi.blahaj.CuddlyItem;

@Mixin(PlayerRenderer.class)
public class PlayerEntityRendererMixin {
	@Inject(
		method = "getArmPose(Lnet/minecraft/client/player/AbstractClientPlayer;Lnet/minecraft/world/InteractionHand;)Lnet/minecraft/client/model/HumanoidModel$ArmPose;",
		at = @At("TAIL"),
		cancellable = true
	)
	private static void cuddleBlahaj(AbstractClientPlayer player, InteractionHand hand, CallbackInfoReturnable<HumanoidModel.ArmPose> ci) {
		if (player.getItemInHand(hand).getItem() instanceof CuddlyItem) {
			ci.setReturnValue(HumanoidModel.ArmPose.CROSSBOW_HOLD);
			ci.cancel();
		}
	}
}
