package hibi.blahaj;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.allay.Allay;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.village.VillagerTradesEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.minecraft.world.InteractionResult;

@Mod("blahaj")
public class Blahaj {

	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, "blahaj");

	public static final RegistryObject<Item> GRAY_SHARK = ITEMS.register("gray_shark",
		() -> new CuddlyItem(new Item.Properties().stacksTo(1), "item.blahaj.gray_shark.tooltip"));

	public static final RegistryObject<Item> BLUE_SHARK = ITEMS.register("blue_shark",
		() -> new CuddlyItem(new Item.Properties().stacksTo(1), "item.blahaj.blue_shark.tooltip"));

	public static final RegistryObject<Item> BLUE_WHALE = ITEMS.register("blue_whale",
		() -> new ItemContainerCuddlyItem(new Item.Properties().stacksTo(1), "item.blahaj.blue_whale.tooltip"));

	public static final RegistryObject<Item> BREAD = ITEMS.register("bread",
		() -> new CuddlyItem(new Item.Properties().stacksTo(1), null));

	public static final RegistryObject<Item> BROWN_BEAR = ITEMS.register("brown_bear",
		() -> new CuddlyItem(new Item.Properties().stacksTo(1), "item.blahaj.brown_bear.tooltip"));

	public Blahaj() {
		IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
		ITEMS.register(modBus);
		modBus.addListener(this::buildCreativeTab);

		MinecraftForge.EVENT_BUS.addListener(Blahaj::onLootTableLoad);
		MinecraftForge.EVENT_BUS.addListener(Blahaj::onVillagerTrades);
		MinecraftForge.EVENT_BUS.addListener(Blahaj::onEntityInteract);
	}

	private void buildCreativeTab(BuildCreativeModeTabContentsEvent event) {
		if (event.getTabKey() == CreativeModeTabs.TOOLS_AND_UTILITIES) {
			event.accept(BLUE_SHARK.get());
			event.accept(GRAY_SHARK.get());
			event.accept(BLUE_WHALE.get());
			event.accept(BREAD.get());
			event.accept(BROWN_BEAR.get());
		}
	}

	private static void onLootTableLoad(LootTableLoadEvent event) {
		ResourceLocation id = event.getName();
		if (id.equals(BuiltInLootTables.STRONGHOLD_CROSSING)
			|| id.equals(BuiltInLootTables.STRONGHOLD_CORRIDOR)) {
			event.getTable().addPool(LootPool.lootPool()
				.add(LootItem.lootTableItem(GRAY_SHARK.get())
					.setWeight(5))
				.add(LootItem.lootTableItem(Items.AIR)
					.setWeight(100))
				.build());
		}
		else if (id.equals(BuiltInLootTables.VILLAGE_PLAINS_HOUSE)) {
			event.getTable().addPool(LootPool.lootPool()
				.add(LootItem.lootTableItem(GRAY_SHARK.get()))
				.add(LootItem.lootTableItem(Items.AIR)
					.setWeight(43))
				.build());
		}
		else if (id.equals(BuiltInLootTables.VILLAGE_TAIGA_HOUSE)
			|| id.equals(BuiltInLootTables.VILLAGE_SNOWY_HOUSE)) {
			event.getTable().addPool(LootPool.lootPool()
				.add(LootItem.lootTableItem(GRAY_SHARK.get())
					.setWeight(5))
				.add(LootItem.lootTableItem(Items.AIR)
					.setWeight(54))
				.build());
		}
	}

	private static void onVillagerTrades(VillagerTradesEvent event) {
		if (event.getType() == VillagerProfession.SHEPHERD) {
			event.getTrades().get(5).add((trader, rand) -> new MerchantOffer(
				new ItemStack(Items.EMERALD, 15), new ItemStack(GRAY_SHARK.get()),
				2, 30, 0.1f));
		}
	}

	private static void onEntityInteract(PlayerInteractEvent.EntityInteract event) {
		if (event.getTarget() instanceof Allay
			&& event.getEntity().getItemInHand(event.getHand()).getItem() instanceof CuddlyItem) {
			event.setCancellationResult(InteractionResult.PASS);
			event.setCanceled(true);
		}
	}
}
