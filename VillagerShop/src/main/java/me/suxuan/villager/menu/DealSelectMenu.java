package me.suxuan.villager.menu;

import me.suxuan.villager.VillagerShop;
import me.suxuan.villager.cache.TradeCache;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.mineacademy.fo.menu.Menu;
import org.mineacademy.fo.menu.button.Button;
import org.mineacademy.fo.menu.model.ItemCreator;
import org.mineacademy.fo.remain.CompMaterial;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DealSelectMenu extends Menu {
	// Trade name.
	private final String trade;
	// Surrounding button, only for beautiful.
	private final Button surroundButton;
	// Return button, return to the TradeSetMenu.
	private final Button returnButton;
	// All deals buttons.
	private final List<Button> itemsButtons = new ArrayList<>();

	private int number = -1;

	public DealSelectMenu(String trade) {
		VillagerShop.getEditing().add(trade);
		this.trade = trade;
		this.setSize(9 * 6);
		this.setTitle("&1Select a deal");
		this.surroundButton = Button.makeDummy(ItemCreator.of(CompMaterial.GRAY_STAINED_GLASS_PANE)
				.name("&7Surrounding Line"));
		if (TradeCache.from(trade).getDeals().isEmpty()) {
			Button button = Button.makeDummy(ItemCreator.of(CompMaterial.RED_CONCRETE)
					.name("&c&lThis trade don't have any deals")
					.lore("&b&lClick to add a deal!"));
			this.itemsButtons.add(button);
		} else {
			for (int i = 0; i < TradeCache.from(trade).getDealsAmount(); i++) {
				this.itemsButtons.add(Button.makeDummy(ItemCreator.of(CompMaterial.LIME_CONCRETE)
						.name("&6&lDeal " + (i + 1))
						.lore("&d&lClick to manage this deal")));
			}
			this.itemsButtons.add(Button.makeDummy(ItemCreator.of(CompMaterial.RED_CONCRETE)
					.name("&6&lWant to make a new deal?")
					.lore("&b&lClick to add a deal!")));
		}
		this.returnButton = Button.makeSimple(ItemCreator.of(CompMaterial.PLAYER_HEAD)
						.skullUrl("http://textures.minecraft.net/texture/86e145e71295bcc0488e9bb7e6d6895b7f969a3b5bb7eb34a52e932bc84df5b")
						.name("&a&lReturn Back"),
				player -> TradeConfigMenu.showTo(player, trade));
	}

	@Override
	public ItemStack getItemAt(int slot) {
		if (slot == 49)
			return returnButton.getItem();
		List<Integer> surround = Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 17, 18, 26, 27, 35, 36, 44, 45, 46, 47, 48, 50, 51, 52, 53);
		if (surround.contains(slot)) return surroundButton.getItem();
		number++;
		if (number >= this.itemsButtons.size()) return NO_ITEM;
		return this.itemsButtons.get(number).getItem();
	}

	@Override
	protected void onMenuClick(Player player, int slot, InventoryAction action, ClickType click, ItemStack cursor, ItemStack clicked, boolean cancelled) {
		TradeCache cache = TradeCache.from(trade);
		if (clicked.getType().equals(Material.RED_CONCRETE)) {
			ItemSetMenu.showTo(player, trade, cache.getDealsAmount(), true);
		}
		if (clicked.getType().equals(Material.LIME_CONCRETE)) {
			int number = Integer.parseInt(clicked.getItemMeta().getDisplayName().replace("§6§lDeal ", "")) - 1;
			ItemSetMenu.showTo(player, trade, number, false);
		}
	}

	@Override
	protected void onMenuClose(Player player, Inventory inventory) {
		VillagerShop.getEditing().remove(trade);
	}

	public static void showTo(Player player, String trade) {
		setSound(null);
		new DealSelectMenu(trade).displayTo(player);
	}
}