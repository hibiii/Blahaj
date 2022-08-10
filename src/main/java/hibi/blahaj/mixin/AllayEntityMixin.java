package hibi.blahaj.mixin;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.animal.allay.Allay;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import hibi.blahaj.CuddlyItem;

@Mixin(Allay.class)
public class AllayEntityMixin {

	@Inject(
		method = "mobInteract",
		at = @At("HEAD"),
		cancellable = true
	)
	public void preventTakePlush(Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResult> info) {
		if(player.getItemInHand(hand).getItem() instanceof CuddlyItem) {
			info.setReturnValue(InteractionResult.PASS);
			info.cancel();
		}
	}
}
