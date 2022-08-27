package hibi.blahaj.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import hibi.blahaj.CuddlyItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtString;
import net.minecraft.screen.AnvilScreenHandler;

@Mixin(AnvilScreenHandler.class)
public class AnvilScreenHandlerMixin extends ForgingScreenHandlerMixin {
	
	@Inject(
		method = "updateResult()V",
		at = {
			@At(
				value = "INVOKE",
				target = "Lnet/minecraft/item/ItemStack;removeCustomName()V"
			),
			@At(
				value = "INVOKE",
				target = "Lnet/minecraft/item/ItemStack;setCustomName(Lnet/minecraft/text/Text;)Lnet/minecraft/item/ItemStack;"
			)
		},
		locals = LocalCapture.CAPTURE_FAILHARD,
		expect = 2,
		require = 2
	)
	public void setName(CallbackInfo info, ItemStack itemStack, int i, int j, int k, ItemStack itemStack2, ItemStack itemStack3) {
		if(itemStack2.getItem() instanceof CuddlyItem) {
			itemStack2.setSubNbt(CuddlyItem.OWNER_KEY, NbtString.of(this.player.getName().getString()));
		}
	}
}
