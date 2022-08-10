package hibi.blahaj.mixin;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import hibi.blahaj.CuddlyItem;

@Mixin(HumanoidModel.class)
public class HumanoidModelMixin {

	@Inject(
		method = {"poseRightArm", "poseLeftArm"},
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/model/AnimationUtils;animateCrossbowHold(Lnet/minecraft/client/model/geom/ModelPart;Lnet/minecraft/client/model/geom/ModelPart;Lnet/minecraft/client/model/geom/ModelPart;Z)V",
			shift = Shift.AFTER
		),
		cancellable = true
	)
	public void poseArms(LivingEntity entity, CallbackInfo ci) {
		if(entity.getMainHandItem().getItem() instanceof CuddlyItem || entity.getOffhandItem().getItem() instanceof CuddlyItem) {
			asThis().rightArm.xRot = -0.95F;
			asThis().rightArm.yRot = (float) (-Math.PI / 8);
			asThis().leftArm.xRot = -0.90F;
			asThis().leftArm.yRot = (float) (Math.PI / 8);
			ci.cancel();
		}
	}

	public HumanoidModel asThis() {
		return (HumanoidModel) (Object)this;
	}
}
