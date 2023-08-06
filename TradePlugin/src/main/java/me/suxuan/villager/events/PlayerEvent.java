package me.suxuan.villager.events;

import me.suxuan.villager.VillagerShop;
import me.suxuan.villager.cache.TradeCache;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MerchantInventory;
import org.bukkit.scheduler.BukkitRunnable;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.collection.SerializedMap;

import java.io.File;
import java.util.List;
import java.util.Objects;

public class PlayerEvent implements Listener {

	@EventHandler
	public void onPlayerTrade(InventoryClickEvent event) {
		// Check if this inventory is trade
		if (event.getInventory().getType() != InventoryType.MERCHANT) return;
		// Check if player action is take supply item
		if (!(event.getAction().equals(InventoryAction.PICKUP_ALL) || event.getAction().equals(InventoryAction.MOVE_TO_OTHER_INVENTORY)
				|| event.getAction().equals(InventoryAction.PICKUP_HALF)))
			return;
		// Check if player success make a deal
		if (event.getInventory().getItem(2) == null) return;
		// Cancel if player click demands
		if (event.getSlot() == 0 || event.getSlot() == 1) return;
		// If player click their inventory
		if (event.getClickedInventory() == null || event.getSlot() != 2 || event.getClickedInventory().getType().equals(InventoryType.PLAYER))
			return;

		ItemStack supply = event.getInventory().getItem(2);
		ItemStack demand1 = event.getInventory().getItem(0);
		ItemStack demand2 = event.getInventory().getItem(1);

		MerchantInventory merchant = (MerchantInventory) event.getInventory();
		if (merchant.getSelectedRecipe() == null) return;

		// Get file name by supply
		String file = "";
		File[] trades_files = Objects.requireNonNull(new File(VillagerShop.getInstance().getDataFolder(), "trades").listFiles());
		for (File trade : trades_files) {
			if (event.getView().getPlayer().getScoreboardTags().contains(trade.getName().replace(".yml", "")))
				file = trade.getName().replace(".yml", "");
		}
		if (file.equals("")) return;

		SerializedMap deals = TradeCache.from(file).getDeals();
		for (int i = 0; i < deals.size(); i++) {

			// Check which deal done successful
			if (!deals.getMap(String.valueOf(i)).getItemStack("2").equals(supply))
				continue;

			// Check if demand is same
			if (demand2 == null && demand1 != null)
				if (checkIfNotSame(demand1, deals, i)) continue;
			if (demand1 == null && demand2 != null)
				if (checkIfNotSame(demand2, deals, i)) continue;
			if (demand1 != null && demand2 != null) {
				if (!(deals.getMap(String.valueOf(i)).getItemStack("0").getType().equals(demand1.getType())
						&& deals.getMap(String.valueOf(i)).getItemStack("1").getType().equals(demand2.getType())))
					if (!(deals.getMap(String.valueOf(i)).getItemStack("1").getType().equals(demand1.getType())
							&& deals.getMap(String.valueOf(i)).getItemStack("0").getType().equals(demand2.getType())))
						continue;
			}

			List<String> commands = deals.getMap(String.valueOf(i)).getStringList("command");

			if (!commands.isEmpty()) {
				for (String command : commands)
					Common.dispatchCommand(event.getView().getPlayer(), command);
				Common.runLater(1, new BukkitRunnable() {
					@Override
					public void run() {
						// If player hold this item
						if (event.getView().getPlayer().getItemOnCursor().getType().equals(Material.AIR))
							event.getView().getPlayer().getInventory().removeItem(supply);
						else
							event.getView().getPlayer().setItemOnCursor(null);
					}
				});
			}
			break;
		}
	}

	private boolean checkIfNotSame(ItemStack demand, SerializedMap deals, int i) {
		ItemStack stack = demand.clone();
		stack.setAmount(deals.getMap(String.valueOf(i)).getItemStack("0").getAmount());
		// Type are not same
		return !deals.getMap(String.valueOf(i)).getItemStack("0").equals(stack);
	}

	@EventHandler
	public void onInventoryCloseEvent(InventoryCloseEvent event) {
		if (event.getInventory().getType() != InventoryType.MERCHANT) return;
		File[] trades_files = Objects.requireNonNull(new File(VillagerShop.getInstance().getDataFolder(), "trades").listFiles());
		for (File trade : trades_files) {
			if (event.getView().getPlayer().getScoreboardTags().contains(trade.getName().replace(".yml", ""))) {
				event.getView().getPlayer().removeScoreboardTag(trade.getName().replace(".yml", ""));
				return;
			}
		}
	}

}
