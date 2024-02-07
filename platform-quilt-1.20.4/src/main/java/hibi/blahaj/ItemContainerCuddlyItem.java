package hibi.blahaj;

import java.util.List;

import org.jetbrains.annotations.Nullable;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.StackReference;
import net.minecraft.item.BundleItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.ClickType;
import net.minecraft.world.World;

public class ItemContainerCuddlyItem extends CuddlyItem {

	public static final String STORED_ITEM_KEY = "Item";

	public ItemContainerCuddlyItem(Settings settings, String subtitle) {
		super(settings, subtitle);
	}

	@Override
	public boolean onClickedOnOther(ItemStack thisStack, Slot otherSlot, ClickType clickType, PlayerEntity player) {
		if (clickType != ClickType.RIGHT) {
			return false;
		}
		ItemStack otherStack = otherSlot.getStack();
		NbtCompound storedItemNbt = thisStack.getSubNbt(STORED_ITEM_KEY);
		if (storedItemNbt != null) {
			if (!otherStack.isEmpty()) {
				return false;
			}
			ItemStack storedStack = ItemStack.fromNbt(storedItemNbt);
			if (!otherSlot.canInsert(storedStack)) {
				return false;
			}
			otherSlot.insertStack(storedStack, DEFAULT_MAX_COUNT);
			ItemContainerCuddlyItem.storeItemStack(thisStack, null);
			return true;
		} else {
			if (otherStack.isEmpty()) {
				return false;
			}
			if (!ItemContainerCuddlyItem.canHold(otherStack)) {
				return false;
			}
			ItemContainerCuddlyItem.storeItemStack(thisStack, otherStack);
			return true;
		}
	}

	@Override
	public boolean onClicked(ItemStack thisStack, ItemStack otherStack, Slot thisSlot, ClickType clickType, PlayerEntity player, StackReference cursorStackReference) {
		if (clickType != ClickType.RIGHT || otherStack.isEmpty()) {
			return false;
		}

		NbtCompound storedItemNbt = thisStack.getSubNbt(STORED_ITEM_KEY);
		if (storedItemNbt != null) {
			return false;
		} else {
			if (!ItemContainerCuddlyItem.canHold(otherStack)) {
				return false;
			}
			ItemContainerCuddlyItem.storeItemStack(thisStack, otherStack);
			return true;
		}
	}

	@Override
	public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext context) {
		super.appendTooltip(stack, world, tooltip, context);
		NbtCompound itemsNbt = stack.getSubNbt(STORED_ITEM_KEY);
		if (itemsNbt == null) {
			return;
		}
		ItemStack storedStack = ItemStack.fromNbt(itemsNbt);
		MutableText text = storedStack.getName().copy();
		text.append(" x").append(String.valueOf(storedStack.getCount()));
		tooltip.add(text);
	}

	protected static boolean canHold(ItemStack otherStack) {
		if (!otherStack.getItem().canBeNested()
		|| otherStack.getItem() instanceof ItemContainerCuddlyItem
		|| otherStack.getItem() instanceof BundleItem) {
			return false;
		}
		return true;
	}

	protected static void storeItemStack(ItemStack thisStack, @Nullable ItemStack otherStack) {
		NbtCompound nbt = thisStack.getOrCreateNbt();
		if (otherStack == null || otherStack.isEmpty()) {
			nbt.remove(STORED_ITEM_KEY);
		} else {
			thisStack.getOrCreateNbt().put(STORED_ITEM_KEY, otherStack.writeNbt(new NbtCompound()));
			otherStack.setCount(0);
		}
	}

	protected static boolean mergeStacks(ItemStack dest, ItemStack source) {
		if (!ItemStack.canCombine(dest, source)) {
			return false;
		}
		int destCount = dest.getCount();
		int sourceCount = source.getCount();
		int destMax = dest.getMaxCount();
		dest.increment(destCount + sourceCount);
		int surplus = destCount + sourceCount - destMax;
		source.setCount(surplus);
		return source.isEmpty();
	}
}
