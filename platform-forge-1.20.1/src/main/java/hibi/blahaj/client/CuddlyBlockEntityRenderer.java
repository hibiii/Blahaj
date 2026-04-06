package hibi.blahaj.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;

import hibi.blahaj.CuddlyBlock;
import hibi.blahaj.CuddlyBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.VoxelShape;

public class CuddlyBlockEntityRenderer implements BlockEntityRenderer<CuddlyBlockEntity> {

	public CuddlyBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
	}

	@Override
	public void render(CuddlyBlockEntity blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
		ItemStack item = blockEntity.getItem();
		if (item.isEmpty()) {
			return;
		}

		poseStack.pushPose();

		// Check the block below to see if it's a non-full block (slab, bed, carpet, etc.)
		// If so, lower blahaj to rest on its surface instead of floating
		double yOffset = 0.22;
		Level level = blockEntity.getLevel();
		if (level != null) {
			BlockPos below = blockEntity.getBlockPos().below();
			BlockState belowState = level.getBlockState(below);
			VoxelShape belowShape = belowState.getCollisionShape(level, below);
			if (!belowShape.isEmpty()) {
				double belowMaxY = belowShape.max(Direction.Axis.Y);
				if (belowMaxY < 1.0) {
					// Lower by the gap between the block-below's top and a full block
					yOffset -= (1.0 - belowMaxY);
				}
			}
		}

		poseStack.translate(0.5, yOffset, 0.5);

		// Rotate to match block rotation (8 directions, 45-degree steps)
		int rotation = blockEntity.getBlockState().getValue(CuddlyBlock.ROTATION);
		// The FIXED display has a built-in -90 Y rotation, so offset by +90
		float yRot = CuddlyBlock.rotationToDegrees(rotation) + 90f;
		poseStack.mulPose(Axis.YP.rotationDegrees(yRot));

		// Compensate for the FIXED display's built-in Z offset (-3/16 = -0.1875)
		poseStack.translate(0.0, 0.0, 0.1875);

		poseStack.scale(0.8f, 0.8f, 0.8f);

		Minecraft.getInstance().getItemRenderer().renderStatic(
			item, ItemDisplayContext.FIXED, packedLight, packedOverlay, poseStack, buffer, blockEntity.getLevel(), 0);

		poseStack.popPose();
	}
}
