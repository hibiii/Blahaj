package hibi.blahaj;

import org.quiltmc.loader.api.ModContainer;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.loot.v2.LootTableEvents;
import org.quiltmc.qsl.villager.api.TradeOfferHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTables;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.village.TradeOffer;
import net.minecraft.village.VillagerProfession;

public class Common {

	public static final Identifier BLAHAJ_ID;
	public static final Identifier KLAPPAR_HAJ_ID;
	public static final Identifier BREAD_ID;

	public void onInitialize(ModContainer mod) {
		Item grayShark = new CuddlyItem(new Item.Settings().maxCount(1), "item.blahaj.gray_shark.tooltip");
		Registry.register(Registries.ITEM, KLAPPAR_HAJ_ID, grayShark);

		Item blueShark = new CuddlyItem(new Item.Settings().maxCount(1), "item.blahaj.blue_shark.tooltip");
		Registry.register(Registries.ITEM, BLAHAJ_ID, blueShark);

		Item breadPillow = new CuddlyItem(new Item.Settings().maxCount(1), null);
		Registry.register(Registries.ITEM, BREAD_ID, breadPillow);

		// Register items to item group
		ItemGroupEvents.modifyEntriesEvent(ItemGroups.TOOLS).register((content) -> {
			content.addItem(blueShark);
			content.addItem(grayShark);
			content.addItem(breadPillow);
		});

		// FIXME replace with QSL
		LootTableEvents.MODIFY.register((resourceManager, lootManager, id, tableBuilder, source) -> {
			if(!source.isBuiltin()) return;
			if(id.equals(LootTables.STRONGHOLD_CROSSING_CHEST)
				|| id.equals(LootTables.STRONGHOLD_CORRIDOR_CHEST)) {
				LootPool.Builder pb = LootPool.builder()
					.with(ItemEntry.builder(grayShark)
						.weight(5))
					.with(ItemEntry.builder(Items.AIR)
						.weight(100));
				tableBuilder.pool(pb);
			}
			else if(id.equals(LootTables.VILLAGE_PLAINS_CHEST)) {
				LootPool.Builder pb = LootPool.builder()
					.with(ItemEntry.builder(grayShark))
					.with(ItemEntry.builder(Items.AIR)
						.weight(43));
				tableBuilder.pool(pb);
			}
			else if(id.equals(LootTables.VILLAGE_TAIGA_HOUSE_CHEST)
				|| id.equals(LootTables.VILLAGE_SNOWY_HOUSE_CHEST)) {
				LootPool.Builder pb = LootPool.builder()
					.with(ItemEntry.builder(grayShark)
						.weight(5))
					.with(ItemEntry.builder(Items.AIR)
						.weight(54));
				tableBuilder.pool(pb);
			}
		});

		// FIXME replace with QSL
		TradeOfferHelper.registerVillagerOffers(VillagerProfession.SHEPHERD, 5, factories -> {
			factories.add((entity, random) -> new TradeOffer(
				new ItemStack(Items.EMERALD, 15), new ItemStack(grayShark),
				2, 30, 0.1f));
		});
	}

	static {
		BLAHAJ_ID = new Identifier("blahaj", "blue_shark");
		KLAPPAR_HAJ_ID = new Identifier("blahaj", "gray_shark");
		BREAD_ID = new Identifier("blahaj", "bread");
	}
}
