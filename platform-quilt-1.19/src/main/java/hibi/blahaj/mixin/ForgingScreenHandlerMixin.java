package hibi.blahaj.mixin;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.ForgingScreenHandler;

@Mixin(ForgingScreenHandler.class)
public class ForgingScreenHandlerMixin {
	
	@Shadow
	protected @Final PlayerEntity player;
}
