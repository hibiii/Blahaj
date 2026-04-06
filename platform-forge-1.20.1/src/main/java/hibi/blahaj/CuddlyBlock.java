package hibi.blahaj;

import javax.annotation.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class CuddlyBlock extends BaseEntityBlock {

	// 0=N, 1=NE, 2=E, 3=SE, 4=S, 5=SW, 6=W, 7=NW (45-degree steps)
	public static final IntegerProperty ROTATION = IntegerProperty.create("rotation", 0, 7);
	private static final VoxelShape SHAPE = Block.box(3, 0, 3, 13, 5, 13);

	public CuddlyBlock() {
		super(BlockBehaviour.Properties.of()
			.noLootTable()
			.strength(0.5f)
			.sound(SoundType.WOOL)
			.noOcclusion());
		this.registerDefaultState(this.stateDefinition.any().setValue(ROTATION, 0));
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(ROTATION);
	}

	@Nullable
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		Direction facing = context.getHorizontalDirection().getOpposite();
		int rot = directionToRotation(facing);
		return this.defaultBlockState().setValue(ROTATION, rot);
	}

	public static int directionToRotation(Direction dir) {
		return switch (dir) {
			case NORTH -> 0;
			case EAST -> 2;
			case SOUTH -> 4;
			case WEST -> 6;
			default -> 0;
		};
	}

	public static float rotationToDegrees(int rotation) {
		return rotation * 45.0f;
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		return SHAPE;
	}

	@Override
	public RenderShape getRenderShape(BlockState state) {
		return RenderShape.ENTITYBLOCK_ANIMATED;
	}

	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new CuddlyBlockEntity(pos, state);
	}

	@SuppressWarnings("deprecation")
	@Override
	public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		if (player.isShiftKeyDown()) {
			// Shift right-click: pick up
			if (!level.isClientSide()) {
				BlockEntity be = level.getBlockEntity(pos);
				if (be instanceof CuddlyBlockEntity cuddlyBE) {
					ItemStack stack = cuddlyBE.getItem();
					if (!stack.isEmpty()) {
						if (!player.getInventory().add(stack.copy())) {
							Block.popResource(level, pos, stack.copy());
						}
					}
					cuddlyBE.setItem(ItemStack.EMPTY);
				}
				level.removeBlock(pos, false);
			}
			return InteractionResult.sidedSuccess(level.isClientSide());
		} else {
			// Right-click: rotate 45 degrees clockwise
			if (!level.isClientSide()) {
				int current = state.getValue(ROTATION);
				int next = (current + 1) % 8;
				level.setBlock(pos, state.setValue(ROTATION, next), 3);
			}
			return InteractionResult.sidedSuccess(level.isClientSide());
		}
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
		if (!state.is(newState.getBlock())) {
			BlockEntity be = level.getBlockEntity(pos);
			if (be instanceof CuddlyBlockEntity cuddlyBE && !level.isClientSide()) {
				ItemStack stack = cuddlyBE.getItem();
				if (!stack.isEmpty()) {
					Block.popResource(level, pos, stack);
				}
			}
		}
		super.onRemove(state, level, pos, newState, movedByPiston);
	}
}
