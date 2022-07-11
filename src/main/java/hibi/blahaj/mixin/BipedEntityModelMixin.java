package hibi.blahaj.mixin;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import hibi.blahaj.Common;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.entity.LivingEntity;

@Mixin(BipedEntityModel.class)
public class BipedEntityModelMixin {

	@Shadow
	public @Final ModelPart rightArm;

	@Shadow
	public @Final ModelPart leftArm;

	@Inject(
		method = {"positionRightArm", "positionLeftArm"},
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/render/entity/model/CrossbowPosing;hold(Lnet/minecraft/client/model/ModelPart;Lnet/minecraft/client/model/ModelPart;Lnet/minecraft/client/model/ModelPart;Z)V",
			shift = Shift.AFTER
		),
		cancellable = true
	)
	public void poseArms(LivingEntity entity, CallbackInfo ci) {
		if(entity.getMainHandStack().isOf(Common.BLAHAJ_ITEM) || entity.getOffHandStack().isOf(Common.BLAHAJ_ITEM)) {
			this.rightArm.pitch = -0.95F;
			this.rightArm.yaw = (float) (-Math.PI / 8);
			this.leftArm.pitch = -0.90F;
			this.leftArm.yaw = (float) (Math.PI / 8);
			ci.cancel();
		}
	}
}
