package hibi.blahaj;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.nbt.StringTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.animal.allay.Allay;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.event.entity.player.AnvilRepairEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = BlahajMod.MODID)
public class ForgeEvents {
	@SubscribeEvent
	public static void preventTakePlush(PlayerInteractEvent.EntityInteract e) {
		if(e.getTarget() instanceof Allay && e.getItemStack().getItem() instanceof CuddlyItem) {
			e.setCancellationResult(InteractionResult.PASS);
			e.setCanceled(true);
		}
	}
	@SubscribeEvent
	public static void setName(AnvilUpdateEvent e) {
		if(e.getLeft().getItem() instanceof CuddlyItem) {
			e.getLeft().addTagElement(CuddlyItem.OWNER_KEY, StringTag.valueOf(e.getPlayer().getName().getString()));
		}
	}
	@SubscribeEvent
	public static void modifyLoot(LootTableLoadEvent e) {
		// FIXME replace with GLM
		LootTable table = e.getTable();
		ResourceLocation id = table.getLootTableId();
		if(id.equals(BuiltInLootTables.STRONGHOLD_CROSSING)
				|| id.equals(BuiltInLootTables.STRONGHOLD_CORRIDOR)) {
			LootPool.Builder pb = LootPool.lootPool()
					.add(LootItem.lootTableItem(BlahajMod.BLUE_SHARK.get())
							.setWeight(5))
					.add(LootItem.lootTableItem(Items.AIR)
							.setWeight(100));
			table.addPool(pb.build());
		}
		else if(id.equals(BuiltInLootTables.VILLAGE_PLAINS_HOUSE)) {
			LootPool.Builder pb = LootPool.lootPool()
					.add(LootItem.lootTableItem(BlahajMod.BLUE_SHARK.get()))
					.add(LootItem.lootTableItem(Items.AIR)
							.setWeight(43));
			table.addPool(pb.build());
		}
		else if(id.equals(BuiltInLootTables.VILLAGE_TAIGA_HOUSE)
				|| id.equals(BuiltInLootTables.VILLAGE_SNOWY_HOUSE)) {
			LootPool.Builder pb = LootPool.lootPool()
					.add(LootItem.lootTableItem(BlahajMod.BLUE_SHARK.get())
							.setWeight(5))
					.add(LootItem.lootTableItem(Items.AIR)
							.setWeight(54));
			table.addPool(pb.build());
		}
	}
}
