package hibi.blahaj;

import net.minecraft.client.model.HumanoidModel;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = BlahajMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientStuff {
	static HumanoidModel.ArmPose CUDDLE_BLAHAJ;
	@SubscribeEvent
	public static void clientSetup(FMLClientSetupEvent e) {
		e.enqueueWork(() -> {
			CUDDLE_BLAHAJ = HumanoidModel.ArmPose.create("BLAHAJ__CUDDLE_BLAHAJ", true, (model, entity, arm) -> {
				model.rightArm.xRot = -0.95F;
				model.rightArm.yRot = (float) (-Math.PI / 8);
				model.leftArm.xRot = -0.90F;
				model.leftArm.yRot = (float) (Math.PI / 8);
			});
		});
	}
}
