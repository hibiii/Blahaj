package hibi.blahaj;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class CuddlyBlockEntity extends BlockEntity {

	private static final String ITEM_KEY = "CuddlyItem";
	private ItemStack item = ItemStack.EMPTY;

	public CuddlyBlockEntity(BlockPos pos, BlockState state) {
		super(Blahaj.CUDDLY_BLOCK_ENTITY.get(), pos, state);
	}

	public ItemStack getItem() {
		return this.item;
	}

	public void setItem(ItemStack stack) {
		this.item = stack.copy();
		this.setChanged();
		if (this.level != null) {
			this.level.sendBlockUpdated(this.worldPosition, this.getBlockState(), this.getBlockState(), 3);
		}
	}

	@Override
	protected void saveAdditional(CompoundTag tag) {
		super.saveAdditional(tag);
		if (!this.item.isEmpty()) {
			tag.put(ITEM_KEY, this.item.save(new CompoundTag()));
		}
	}

	@Override
	public void load(CompoundTag tag) {
		super.load(tag);
		if (tag.contains(ITEM_KEY)) {
			this.item = ItemStack.of(tag.getCompound(ITEM_KEY));
		} else {
			this.item = ItemStack.EMPTY;
		}
	}

	@Override
	public Packet<ClientGamePacketListener> getUpdatePacket() {
		return ClientboundBlockEntityDataPacket.create(this);
	}

	@Override
	public CompoundTag getUpdateTag() {
		return saveWithoutMetadata();
	}
}
