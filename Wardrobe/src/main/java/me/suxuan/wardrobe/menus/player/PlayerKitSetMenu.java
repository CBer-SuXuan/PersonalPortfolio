package me.suxuan.wardrobe.menus.player;

import me.suxuan.wardrobe.manager.WardrobeManager;
import me.suxuan.wardrobe.settings.KitSetting;
import me.suxuan.wardrobe.settings.MenuSetting;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.FileUtil;
import org.mineacademy.fo.menu.Menu;
import org.mineacademy.fo.menu.model.MenuClickLocation;
import org.mineacademy.fo.remain.CompMaterial;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class PlayerKitSetMenu extends Menu {
	private final Player target;
	private final int page;

	private final int id;

	public PlayerKitSetMenu(Player player, Player target, int id, int page) {
		this.setViewer(player);
		this.setSize(9 * 6);
		this.setTitle(MenuSetting.getInstance().getKitSetTitle());
		this.id = id;
		this.page = page;
		this.target = target;
	}

	@Override
	public ItemStack getItemAt(int slot) {
		WardrobeManager wardrobeManager = WardrobeManager.from(target);
		List<ItemStack> items = KitSetting.getInstance().getMenu();
		if (!Arrays.asList(13, 22, 31, 40).contains(slot))
			return items.get(slot);
		if (slot == 13)
			return wardrobeManager.getEquip(WardrobeManager.Equip.HELMET, id);
		if (slot == 22)
			return wardrobeManager.getEquip(WardrobeManager.Equip.CHESTPLATE, id);
		if (slot == 31)
			return wardrobeManager.getEquip(WardrobeManager.Equip.LEGGINGS, id);
		if (slot == 40)
			return wardrobeManager.getEquip(WardrobeManager.Equip.BOOTS, id);
		return NO_ITEM;
	}

	@Override
	protected boolean addInfoButton() {
		return false;
	}

	@Override
	protected boolean isActionAllowed(MenuClickLocation location, int slot, @Nullable ItemStack clicked, @Nullable ItemStack cursor, InventoryAction action) {

//		System.out.println("Location: " + location + ", Slot: " + slot + ", Clicked: " + clicked + ", Cursor: " + cursor + ", Action: " + action);

		if (action == InventoryAction.MOVE_TO_OTHER_INVENTORY ||
				(location == MenuClickLocation.MENU && action.toString().contains("HOTBAR"))) {
			this.animateTitle("&4Drag item to the slot!");
			return false;
		}

		if (location == MenuClickLocation.PLAYER_INVENTORY || Arrays.asList(13, 22, 31, 40).contains(slot)) {

			ItemStack placedItem;
			if (action == InventoryAction.SWAP_WITH_CURSOR)
				placedItem = cursor;
			else
				placedItem = clicked != null && !CompMaterial.isAir(clicked) ? clicked : cursor;

			if (placedItem != null && !CompMaterial.isAir(placedItem)) {
				if ((action == InventoryAction.SWAP_WITH_CURSOR || action == InventoryAction.PLACE_ALL || action == InventoryAction.PLACE_ONE || action == InventoryAction.PLACE_SOME) && location == MenuClickLocation.MENU) {
					if (placedItem.getAmount() > 1 && action != InventoryAction.PLACE_ONE) {
						this.animateTitle("&4Amount must be 1!");
						return false;
					}
					if (clicked != null && clicked.getAmount() >= 1) {
						this.animateTitle("&4Amount must be 1!");
						return false;
					}
					List<String> allows = Arrays.asList("");
					boolean allowDrop = false;
					switch (slot) {
						case 13 -> allows = Arrays.asList("helmet", "head");
						case 22 -> allows = Arrays.asList("chestplate");
						case 31 -> allows = Arrays.asList("leggings");
						case 40 -> allows = Arrays.asList("boots");
					}
					for (String allow : allows)
						if (placedItem.getType().toString().toLowerCase().contains(allow)) allowDrop = true;
					return allowToDrop(allowDrop);
				}
			}
			return true;
		}

		return false;
	}

	@Override
	protected void onMenuClose(Player player, Inventory inventory) {
		ItemStack head = inventory.getItem(13);
		ItemStack chestplate = inventory.getItem(22);
		ItemStack leggings = inventory.getItem(31);
		ItemStack boots = inventory.getItem(40);
		if (head != null) head.setAmount(1);
		if (chestplate != null) chestplate.setAmount(1);
		if (leggings != null) leggings.setAmount(1);
		if (boots != null) boots.setAmount(1);
		WardrobeManager.from(target).setEquipAll(head, chestplate, leggings, boots, id);
		PlayerWardrobeMenu.showTo(player, target, page);
	}

	public static void showTo(Player player, Player target, int id, int page) {
		setSound(null);
		File file = FileUtil.getFile("menu/edit_kit.yml");
		// Check if file no exist, that mean not edit kit menu.
		if (!file.exists()) {
			if (player.isOp()) {
				Common.tellNoPrefix(player, "&f[&#df9820W&#bf9a40a&#9f9d60r&#80a080d&#60a29fr&#40a5bfo&#20a7dfb&#00aaffe&f] &cYou need to edit the edit kit menu first!");
				Common.tellNoPrefix(player, "&f[&#df9820W&#bf9a40a&#9f9d60r&#80a080d&#60a29fr&#40a5bfo&#20a7dfb&#00aaffe&f] &cType &f/wardrobe settings");
				Common.tellNoPrefix(player, "&f[&#df9820W&#bf9a40a&#9f9d60r&#80a080d&#60a29fr&#40a5bfo&#20a7dfb&#00aaffe&f] &cChoose &9Menu Settings");
				Common.tellNoPrefix(player, "&f[&#df9820W&#bf9a40a&#9f9d60r&#80a080d&#60a29fr&#40a5bfo&#20a7dfb&#00aaffe&f] &cand then choose &eEdit kit menu");
			} else {
				Common.tellNoPrefix(player, "&f[&#df9820W&#bf9a40a&#9f9d60r&#80a080d&#60a29fr&#40a5bfo&#20a7dfb&#00aaffe&f] &cContact owner to set this menu!");
			}
			return;
		} else {
			new PlayerKitSetMenu(player, target, id, page).displayTo(player);
		}
	}

	private boolean allowToDrop(boolean allowDrop) {
		if (!allowDrop) {
			this.animateTitle("&4You cannot put in here!");
			return false;
		} else {
			return true;
		}
	}
}
