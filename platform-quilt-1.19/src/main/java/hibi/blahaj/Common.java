package hibi.blahaj;

import net.fabricmc.fabric.api.loot.v2.LootTableEvents;
import net.fabricmc.fabric.api.object.builder.v1.trade.TradeOfferHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTables;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.village.TradeOffer;
import net.minecraft.village.VillagerProfession;

public class Common {

	public static final Identifier BLAHAJ_ID;
	public static final Identifier KLAPPAR_HAJ_ID;
	public static final Identifier BLAVINGAD_ID;
	public static final Identifier BREAD_ID;

	public void onInitialize() {
		Item grayShark = Registry.register(Registry.ITEM, KLAPPAR_HAJ_ID, new CuddlyItem(new Item.Settings().maxCount(1).group(ItemGroup.MISC), "item.blahaj.gray_shark.tooltip"));

		Registry.register(Registry.ITEM, BLAHAJ_ID, new CuddlyItem(new Item.Settings().maxCount(1).group(ItemGroup.MISC), "item.blahaj.blue_shark.tooltip"));

		Registry.register(Registry.ITEM, BLAVINGAD_ID, new ItemContainerCuddlyItem(new Item.Settings().maxCount(1).group(ItemGroup.MISC), "item.blahaj.blue_whale.tooltip"));

		Registry.register(Registry.ITEM, BREAD_ID, new CuddlyItem(new Item.Settings().maxCount(1).group(ItemGroup.MISC), null));

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

		TradeOfferHelper.registerVillagerOffers(VillagerProfession.SHEPHERD, 5, factories -> {
			factories.add((entity, random) -> new TradeOffer(
				new ItemStack(Items.EMERALD, 15), new ItemStack(grayShark),
				2, 30, 0.1f));
		});
	}

	static {
		BLAHAJ_ID = new Identifier("blahaj", "blue_shark");
		KLAPPAR_HAJ_ID = new Identifier("blahaj", "gray_shark");
		BLAVINGAD_ID = new Identifier("blahaj", "blue_whale");
		BREAD_ID = new Identifier("blahaj", "bread");
	}
}
