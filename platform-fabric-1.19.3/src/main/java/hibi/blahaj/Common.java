package hibi.blahaj;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.loot.v2.LootTableEvents;
import net.fabricmc.fabric.api.object.builder.v1.trade.TradeOfferHelper;
import net.minecraft.item.*;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTables;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.village.TradeOffer;
import net.minecraft.village.VillagerProfession;

public class Common implements ModInitializer {

	public static final Identifier BLAHAJ_ID;
	public static final Identifier KLAPPAR_HAJ_ID;
	public static final Identifier BREAD_ID;

	public Item KLAPPAR_HAJ;
	public Item BLAHAJ;
	public Item BREAD;

	public void onInitialize() {
		KLAPPAR_HAJ = new CuddlyItem(new Item.Settings().maxCount(1), "item.blahaj.gray_shark.tooltip");
		BLAHAJ = new CuddlyItem(new Item.Settings().maxCount(1), "item.blahaj.blue_shark.tooltip");
		BREAD = new CuddlyItem(new Item.Settings().maxCount(1), null);

		Registry.register(Registries.ITEM, KLAPPAR_HAJ_ID, KLAPPAR_HAJ);
		Registry.register(Registries.ITEM, BLAHAJ_ID, BLAHAJ);
		Registry.register(Registries.ITEM, BREAD_ID, BREAD);

		ItemGroupEvents.modifyEntriesEvent(ItemGroups.TOOLS).register(entries -> {
			entries.add(KLAPPAR_HAJ);
			entries.add(BLAHAJ);
			entries.add(BREAD);
		});

		LootTableEvents.MODIFY.register((resourceManager, lootManager, id, tableBuilder, source) -> {
			if(id.equals(LootTables.STRONGHOLD_CROSSING_CHEST)
				|| id.equals(LootTables.STRONGHOLD_CORRIDOR_CHEST)) {
				LootPool.Builder pb = LootPool.builder()
					.with(ItemEntry.builder(KLAPPAR_HAJ)
						.weight(5))
					.with(ItemEntry.builder(Items.AIR)
						.weight(100));
				tableBuilder.pool(pb);
			}
			else if(id.equals(LootTables.VILLAGE_PLAINS_CHEST)) {
				LootPool.Builder pb = LootPool.builder()
					.with(ItemEntry.builder(KLAPPAR_HAJ))
					.with(ItemEntry.builder(Items.AIR)
						.weight(43));
				tableBuilder.pool(pb);
			}
			else if(id.equals(LootTables.VILLAGE_TAIGA_HOUSE_CHEST)
				|| id.equals(LootTables.VILLAGE_SNOWY_HOUSE_CHEST)) {
				LootPool.Builder pb = LootPool.builder()
					.with(ItemEntry.builder(KLAPPAR_HAJ)
						.weight(5))
					.with(ItemEntry.builder(Items.AIR)
						.weight(54));
				tableBuilder.pool(pb);
			}
		});

		TradeOfferHelper.registerVillagerOffers(VillagerProfession.SHEPHERD, 5, factories -> {
			factories.add((entity, random) -> new TradeOffer(
				new ItemStack(Items.EMERALD, 15), new ItemStack(KLAPPAR_HAJ),
				2, 30, 0.1f));
		});
	}

	static {
		BLAHAJ_ID = new Identifier("blahaj", "blue_shark");
		KLAPPAR_HAJ_ID = new Identifier("blahaj", "gray_shark");
		BREAD_ID = new Identifier("blahaj", "bread");
	}
}
