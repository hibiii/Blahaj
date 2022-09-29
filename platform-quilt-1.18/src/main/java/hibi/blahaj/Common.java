package hibi.blahaj;

import org.quiltmc.loader.api.ModContainer;
import net.fabricmc.fabric.api.loot.v2.LootTableEvents;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.Items;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTables;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class Common {

	public static final Identifier BLAHAJ_ID;
	public static final Identifier BREAD_ID;

	public void onInitialize(ModContainer mod) {
		Item blueShark = Registry.register(Registry.ITEM, BLAHAJ_ID, new CuddlyItem(new Item.Settings().maxCount(1).group(ItemGroup.MISC), "item.blahaj.blue_shark.tooltip"));
		Registry.register(Registry.ITEM, BREAD_ID, new CuddlyItem(new Item.Settings().maxCount(1).group(ItemGroup.MISC), null));

		// FIXME replace with QSL
		LootTableEvents.MODIFY.register((resourceManager, lootManager, id, tableBuilder, source) -> {
			if(!source.isBuiltin()) return;
			if(id.equals(LootTables.STRONGHOLD_CROSSING_CHEST)
				|| id.equals(LootTables.STRONGHOLD_CORRIDOR_CHEST)) {
				LootPool.Builder pb = LootPool.builder()
					.with(ItemEntry.builder(blueShark)
						.weight(5))
					.with(ItemEntry.builder(Items.AIR)
						.weight(100));
					// Sum of weights of SH Corridor ≈ 100
					// SOW of SH Crossing ≈ 60
				tableBuilder.pool(pb);
			}
			else if(id.equals(LootTables.VILLAGE_PLAINS_CHEST)) {
				LootPool.Builder pb = LootPool.builder()
					.with(ItemEntry.builder(blueShark))
					.with(ItemEntry.builder(Items.AIR)
						.weight(43));
				tableBuilder.pool(pb);
			}
			else if(id.equals(LootTables.VILLAGE_TAIGA_HOUSE_CHEST)
				|| id.equals(LootTables.VILLAGE_SNOWY_HOUSE_CHEST)) {
				LootPool.Builder pb = LootPool.builder()
					.with(ItemEntry.builder(blueShark)
						.weight(5))
					.with(ItemEntry.builder(Items.AIR)
						.weight(54));
				tableBuilder.pool(pb);
			}
		});
	}

	static {
		BLAHAJ_ID = new Identifier("blahaj", "blue_shark");
		BREAD_ID = new Identifier("blahaj", "bread");
	}
}
