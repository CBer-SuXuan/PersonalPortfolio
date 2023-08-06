package me.suxuan.backpack;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.mineacademy.fo.collection.StrictMap;
import org.mineacademy.fo.menu.Menu;
import org.mineacademy.fo.menu.model.MenuClickLocation;

import java.util.ArrayList;
import java.util.List;

public class PackMenu extends Menu {

	private final String name;

	protected PackMenu(int tier, String name, String title) {
		this.setSize(tier * 9);
		this.setTitle(title);
		this.name = name;
	}

	@Override
	public ItemStack getItemAt(int slot) {
		PlayerCache cache = PlayerCache.from(getViewer());
		List<ItemStack> items = cache.getItems().getList(name, ItemStack.class);
		return slot < items.size() ? items.get(slot) : NO_ITEM;
	}

	@Override
	protected void onMenuClose(Player player, Inventory inventory) {
		final StrictMap<Integer, ItemStack> items = new StrictMap<>();
		for (int slot = 0; slot < this.getSize(); slot++) {

			final ItemStack item = inventory.getItem(slot);
			items.put(slot, item);
		}
		this.onMenuClose(items);
	}

	@Override
	protected boolean isActionAllowed(MenuClickLocation location, int slot, @Nullable ItemStack clicked, @Nullable ItemStack cursor, InventoryAction action) {
		return true;
	}

	private void onMenuClose(StrictMap<Integer, ItemStack> items) {
		PlayerCache cache = PlayerCache.from(this.getViewer());
		cache.changeItems(name, new ArrayList<>(items.values()));
	}

	@Override
	protected boolean addReturnButton() {
		return false;
	}

	@Override
	protected boolean addInfoButton() {
		return false;
	}


	public static void showTo(Player player, int rows, String name, String title) {
		new PackMenu(rows, name, title).displayTo(player);
	}
}
