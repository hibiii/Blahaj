package hibi.blahaj.mixin;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ItemCombinerMenu;

@Mixin(ItemCombinerMenu.class)
public class ItemCombinerMenuMixin {

	@Shadow
	protected @Final Player player;
}
