package hibi.blahaj;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.BundleItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

public class ItemContainerCuddlyItem extends CuddlyItem {

	public static final String STORED_ITEM_KEY = "Item";

	public ItemContainerCuddlyItem(Properties properties, String subtitle) {
		super(properties, subtitle);
	}

	@Override
	public boolean overrideStackedOnOther(ItemStack thisStack, Slot otherSlot, ClickAction clickAction, Player player) {
		if (clickAction != ClickAction.SECONDARY) {
			return false;
		}
		ItemStack otherStack = otherSlot.getItem();
		CompoundTag storedItemNbt = thisStack.getTagElement(STORED_ITEM_KEY);
		if (storedItemNbt != null) {
			if (!otherStack.isEmpty()) {
				return false;
			}
			ItemStack storedStack = ItemStack.of(storedItemNbt);
			if (!otherSlot.mayPlace(storedStack)) {
				return false;
			}
			otherSlot.safeInsert(storedStack, 64);
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
	public boolean overrideOtherStackedOnMe(ItemStack thisStack, ItemStack otherStack, Slot thisSlot, ClickAction clickAction, Player player, SlotAccess slotAccess) {
		if (clickAction != ClickAction.SECONDARY || otherStack.isEmpty()) {
			return false;
		}

		CompoundTag storedItemNbt = thisStack.getTagElement(STORED_ITEM_KEY);
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
	public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
		super.appendHoverText(stack, level, tooltip, flag);
		CompoundTag itemsNbt = stack.getTagElement(STORED_ITEM_KEY);
		if (itemsNbt == null) {
			return;
		}
		ItemStack storedStack = ItemStack.of(itemsNbt);
		MutableComponent text = storedStack.getHoverName().copy();
		text.append(" x").append(String.valueOf(storedStack.getCount()));
		tooltip.add(text);
	}

	protected static boolean canHold(ItemStack otherStack) {
		if (!otherStack.getItem().canFitInsideContainerItems()
		|| otherStack.getItem() instanceof ItemContainerCuddlyItem
		|| otherStack.getItem() instanceof BundleItem) {
			return false;
		}
		return true;
	}

	protected static void storeItemStack(ItemStack thisStack, @Nullable ItemStack otherStack) {
		CompoundTag nbt = thisStack.getOrCreateTag();
		if (otherStack == null || otherStack.isEmpty()) {
			nbt.remove(STORED_ITEM_KEY);
		} else {
			thisStack.getOrCreateTag().put(STORED_ITEM_KEY, otherStack.save(new CompoundTag()));
			otherStack.setCount(0);
		}
	}

	protected static boolean mergeStacks(ItemStack dest, ItemStack source) {
		if (!ItemStack.isSameItemSameTags(dest, source)) {
			return false;
		}
		int destCount = dest.getCount();
		int sourceCount = source.getCount();
		int destMax = dest.getMaxStackSize();
		dest.grow(destCount + sourceCount);
		int surplus = destCount + sourceCount - destMax;
		source.setCount(surplus);
		return source.isEmpty();
	}
}
