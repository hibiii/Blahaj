package hibi.blahaj.mixin;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import hibi.blahaj.CuddlyItem;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.LivingEntity;

@Mixin(HumanoidModel.class)
public class HumanoidModelMixin {

	@Shadow
	public @Final ModelPart rightArm;

	@Shadow
	public @Final ModelPart leftArm;

	@Shadow
	public @Final ModelPart head;

	@Inject(
		method = "setupAnim(Lnet/minecraft/world/entity/LivingEntity;FFFFF)V",
		at = @At("TAIL")
	)
	public void poseArms(LivingEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, CallbackInfo ci) {
		boolean mainHand = entity.getMainHandItem().getItem() instanceof CuddlyItem;
		boolean offHand = entity.getOffhandItem().getItem() instanceof CuddlyItem;
		if (!mainHand && !offHand) return;

		if (entity.isUsingItem() && entity.getUseItem().getItem() instanceof CuddlyItem) {
			// Petting: both arms oscillate up and down like loading/unloading a crossbow
			float ticks = entity.getTicksUsingItem() + (ageInTicks % 1.0F);
			float oscillate = (float) Math.sin(ticks * 0.3) * 0.4F;

			this.rightArm.xRot = -1.1F + oscillate;
			this.rightArm.yRot = (float) (-Math.PI / 8);
			this.leftArm.xRot = -1.1F + oscillate;
			this.leftArm.yRot = (float) (Math.PI / 8);
		} else {
			// Default holding: static cuddle pose
			this.rightArm.xRot = -0.95F;
			this.rightArm.yRot = (float) (-Math.PI / 8);
			this.leftArm.xRot = -0.90F;
			this.leftArm.yRot = (float) (Math.PI / 8);
		}
	}
}
