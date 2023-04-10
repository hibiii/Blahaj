package hibi.blahaj;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.StackReference;
import net.minecraft.item.BundleItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.ClickType;

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
		NbtCompound nbt = thisStack.getOrCreateNbt();
		if (nbt.contains(STORED_ITEM_KEY)) {
			ItemStack storedItem = ItemStack.fromNbt(nbt.getCompound(STORED_ITEM_KEY));
			if (!otherSlot.canInsert(storedItem)) {
				return false;
			}
			otherSlot.insertStack(storedItem);
			nbt.remove(STORED_ITEM_KEY);
			return true;
		} else {
			ItemStack otherStack = otherSlot.getStack();
			if (otherStack.isEmpty()) {
				return false;
			}
			if (!ItemContainerCuddlyItem.canHold(otherStack)) {
				return false;
			}
			ItemContainerCuddlyItem.addItem(thisStack, otherStack);
			return true;
		}
	}

	@Override
	public boolean onClicked(ItemStack thisStack, ItemStack otherStack, Slot thisSlot, ClickType clickType, PlayerEntity player, StackReference cursorStackReference) {
		if (clickType != ClickType.RIGHT || otherStack.isEmpty()) {
			return false;
		}

		NbtCompound nbt = thisStack.getOrCreateNbt();
		if (nbt.contains(STORED_ITEM_KEY)) {
			ItemStack storedItem = ItemStack.fromNbt(nbt.getCompound(STORED_ITEM_KEY));
			if (!ItemStack.canCombine(otherStack, storedItem)) {
				return false;
			}
			otherStack.increment(storedItem.getCount());
			nbt.remove(STORED_ITEM_KEY);
			return true;
		} else {
			if (!ItemContainerCuddlyItem.canHold(otherStack)) {
				return false;
			}
			ItemContainerCuddlyItem.addItem(thisStack, otherStack);
			return true;
		}
	}

	protected static boolean canHold(ItemStack otherStack) {
		if (!otherStack.getItem().canBeNested()
		|| otherStack.getItem() instanceof ItemContainerCuddlyItem
		|| otherStack.getItem() instanceof BundleItem) {
			return false;
		}
		return true;
	}

	protected static void addItem(ItemStack thisStack, ItemStack otherStack) {
		ItemStack one = otherStack.copy();
		otherStack.decrement(1);
		one.setCount(1);
		thisStack.getOrCreateNbt().put(STORED_ITEM_KEY, one.writeNbt(new NbtCompound()));
	}
}
