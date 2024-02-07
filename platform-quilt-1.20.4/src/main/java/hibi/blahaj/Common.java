package hibi.blahaj;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.loot.v2.LootTableEvents;
import net.fabricmc.fabric.api.object.builder.v1.trade.TradeOfferHelper;

import java.util.List;
import java.util.function.Consumer;

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
import net.minecraft.village.TradeOffers;
import net.minecraft.village.VillagerProfession;

public class Common {

	public static final Identifier BLAHAJ_ID;
	public static final Identifier KLAPPAR_HAJ_ID;
	public static final Identifier BLAVINGAD_ID;
	public static final Identifier BREAD_ID;

	public void onInitializeQuilt(Object _mod) {
		// NOTE: Cast `_mod` to `ModContainer` before using it.
		this.onInitialize();
	}

	public void onInitialize() {
		Item grayShark = new CuddlyItem(new Item.Settings().maxCount(1), "item.blahaj.gray_shark.tooltip");
		Registry.register(Registries.ITEM, KLAPPAR_HAJ_ID, grayShark);

		Item blueShark = new CuddlyItem(new Item.Settings().maxCount(1), "item.blahaj.blue_shark.tooltip");
		Registry.register(Registries.ITEM, BLAHAJ_ID, blueShark);

		Item blueWhale = new ItemContainerCuddlyItem(new Item.Settings().maxCount(1), "item.blahaj.blue_whale.tooltip");
		Registry.register(Registries.ITEM, BLAVINGAD_ID, blueWhale);

		Item breadPillow = new CuddlyItem(new Item.Settings().maxCount(1), null);
		Registry.register(Registries.ITEM, BREAD_ID, breadPillow);

		// Register items to item group
		ItemGroupEvents.modifyEntriesEvent(ItemGroups.TOOLS_AND_UTILITIES).register((content) -> {
			content.addItem(blueShark);
			content.addItem(grayShark);
			content.addItem(blueWhale);
			content.addItem(breadPillow);
		});

		LootTableEvents.MODIFY.register((resourceManager, lootManager, id, tableBuilder, source) -> {
			if(!source.isBuiltin()) return;
			if(id.equals(LootTables.field_800) // Stronghold crossing
				|| id.equals(LootTables.field_842)) { // Stronghold corridor
				LootPool.Builder pb = LootPool.builder()
					.with(ItemEntry.builder(grayShark)
						.weight(5))
					.with(ItemEntry.builder(Items.field_8162) // Air
						.weight(100));
				tableBuilder.pool(pb);
			}
			else if(id.equals(LootTables.VILLAGE_PLAINS_CHEST)) {
				LootPool.Builder pb = LootPool.builder()
					.with(ItemEntry.builder(grayShark))
					.with(ItemEntry.builder(Items.field_8162)
						.weight(43));
				tableBuilder.pool(pb);
			}
			else if(id.equals(LootTables.VILLAGE_TAIGA_HOUSE_CHEST)
				|| id.equals(LootTables.VILLAGE_SNOWY_HOUSE_CHEST)) {
				LootPool.Builder pb = LootPool.builder()
					.with(ItemEntry.builder(grayShark)
						.weight(5))
					.with(ItemEntry.builder(Items.field_8162)
						.weight(54));
				tableBuilder.pool(pb);
			}
		});

		Consumer<List<TradeOffers.Factory>> lambda = factories -> {
			factories.add((entity, random) -> new TradeOffer(
				new ItemStack(Items.field_8687, 15), new ItemStack(grayShark), // Emerald
				2, 30, 0.1f));
		};

		TradeOfferHelper.registerVillagerOffers(VillagerProfession.field_17063, 5, lambda); // Shepherd
	}

	static {
		BLAHAJ_ID = new Identifier("blahaj", "blue_shark");
		KLAPPAR_HAJ_ID = new Identifier("blahaj", "gray_shark");
		BLAVINGAD_ID = new Identifier("blahaj", "blue_whale");
		BREAD_ID = new Identifier("blahaj", "bread");
	}
}
