package me.suxuan.wardrobe.menus.player;

import me.suxuan.wardrobe.manager.PlayerInfoManager;
import me.suxuan.wardrobe.manager.WardrobeManager;
import me.suxuan.wardrobe.settings.BorderSetting;
import me.suxuan.wardrobe.settings.ButtonsSetting;
import me.suxuan.wardrobe.settings.EmptySetting;
import me.suxuan.wardrobe.settings.MenuSetting;
import me.suxuan.wardrobe.utils.Cooldown;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.Node;
import net.luckperms.api.query.QueryOptions;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.FileUtil;
import org.mineacademy.fo.menu.Menu;
import org.mineacademy.fo.menu.button.Button;
import org.mineacademy.fo.remain.CompMaterial;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PlayerWardrobeMenu extends Menu {

	private final int page;
	private final Player target;
	private final List<Button> onePageBorderButton = new ArrayList<>();
	private final List<Button> firstPageBorderButton = new ArrayList<>();
	private final List<Button> middlePageBorderButton = new ArrayList<>();
	private final List<Button> lastPageBorderButton = new ArrayList<>();
	private final Button lastPageButton;
	private final Button nextPageButton;
	private final Button lockButton;
	private final Button notSelectButton;
	private final Button selectedButton;
	private final Button emptyKitButton;

	public PlayerWardrobeMenu(Player player, Player target, int page) {

		this.setViewer(player);
		this.setSize(9 * 6);
		this.page = page;
		this.target = target;

		int total_page;
		if (MenuSetting.getInstance().getSlotPermission()) {
			List<Integer> slots = WardrobeManager.from(target).getPermissionSlotList();

			Integer max;
			if (slots.isEmpty()) max = 0;
			else max = Collections.max(slots);

			if (max % 9 == 0 && max != 0) total_page = max / 9;
			else total_page = max / 9 + 1;
		} else {
			int unlock = PlayerInfoManager.from(target).getUnlock();
			if (unlock % 9 == 0) total_page = unlock / 9;
			else total_page = unlock / 9 + 1;
		}

		this.setTitle(MenuSetting.getInstance().getWardrobeTitle().replace("{current}", String.valueOf(page)).replace("{total}", String.valueOf(total_page)));

		List<ItemStack> buttons = ButtonsSetting.getInstance().getButtons();

		for (ItemStack itemStack : BorderSetting.getInstance().getOne())
			this.onePageBorderButton.add(Button.makeDummy(itemStack));
		for (ItemStack itemStack : BorderSetting.getInstance().getFirst())
			this.firstPageBorderButton.add(Button.makeDummy(itemStack));
		for (ItemStack itemStack : BorderSetting.getInstance().getMiddle())
			this.middlePageBorderButton.add(Button.makeDummy(itemStack));
		for (ItemStack itemStack : BorderSetting.getInstance().getLast())
			this.lastPageBorderButton.add(Button.makeDummy(itemStack));

		this.lastPageButton = Button.makeDummy(buttons.get(0));
		this.nextPageButton = Button.makeDummy(buttons.get(1));
		this.lockButton = Button.makeDummy(buttons.get(2));
		this.notSelectButton = Button.makeDummy(buttons.get(3));
		this.selectedButton = Button.makeDummy(buttons.get(4));
		this.emptyKitButton = Button.makeDummy(buttons.get(5) == null ? CompMaterial.AIR.toItem() : buttons.get(5));
	}

	@Override
	public ItemStack getItemAt(int slot) {
		List<ItemStack> buttons = ButtonsSetting.getInstance().getButtons();

		int total_page;
		if (MenuSetting.getInstance().getSlotPermission()) {
			updatePermissionSlot(LuckPermsProvider.get().getUserManager().getUser(target.getUniqueId()));
			List<Integer> slots = WardrobeManager.from(target).getPermissionSlotList();

			Integer max;
			if (slots.isEmpty()) max = 0;
			else max = Collections.max(slots);

			if (max % 9 == 0 && max != 0) total_page = max / 9;
			else total_page = max / 9 + 1;

			int line = slot / 9 + 1;
			int id = (slot % 9 + 1) + 9 * (page - 1);
			if (slots.contains(id) && slot < 36) {
				WardrobeManager.from(target).updatePermissionSlot(slots);
				return getEquip(line, buttons, id);
			}

			if (slot >= 36 && slot <= 44) {
				if (slots.contains((slot - 35) + 9 * (page - 1))) {
					if (PlayerInfoManager.from(target).getSelected() == id)
						return selectedButton.getItem();
					return notSelectButton.getItem();
				} else return lockButton.getItem();
			}
		} else {
			int unlock = PlayerInfoManager.from(target).getUnlock();
			if (unlock % 9 == 0 && unlock != 0) total_page = unlock / 9;
			else total_page = unlock / 9 + 1;

			int line = slot / 9 + 1;
			int id = (slot % 9 + 1) + 9 * (page - 1);
			if (id <= unlock && slot < 36) {
				WardrobeManager.from(target).updateSlot(unlock);
				return getEquip(line, buttons, id);
			}

			if (slot >= 36 && slot <= 44) {
				if ((slot - 35) + 9 * (page - 1) <= unlock) {
					if (PlayerInfoManager.from(target).getSelected() == id)
						return selectedButton.getItem();
					return notSelectButton.getItem();
				} else return lockButton.getItem();
			}
		}

		if (slot >= 45 && slot <= 53)
			if (total_page == 1)
				return onePageBorderButton.get(slot - 45).getItem();
			else {
				if (page == 1)
					if (slot == 53)
						return nextPageButton.getItem();
					else
						return firstPageBorderButton.get(slot - 45).getItem();
				else if (page == total_page)
					if (slot == 45)
						return lastPageButton.getItem();
					else
						return lastPageBorderButton.get(slot - 46).getItem();
				else {
					if (slot == 45)
						return lastPageButton.getItem();
					else if (slot == 53)
						return nextPageButton.getItem();
					else
						return middlePageBorderButton.get(slot - 46).getItem();
				}
			}

		List<ItemStack> items = EmptySetting.getInstance().getMenu();
		if (slot >= 0 && slot <= 35)
			return items.get(slot);
		return NO_ITEM;
	}

	@Override
	protected boolean addInfoButton() {
		return false;
	}

	@Override
	protected void onButtonClick(Player player, int slot, InventoryAction action, ClickType click, Button button) {
		if (player != target) {
			animateTitle("&4Can not edit!");
			return;
		}
		if (Cooldown.checkCooldown(player)) {
			if (button == notSelectButton) {
				if (click == ClickType.LEFT) {  // Left click to select the slot
					// Update old armors
					PlayerInventory inventory = target.getInventory();
					WardrobeManager.from(target).setEquipAll(inventory.getHelmet(), inventory.getChestplate(),
							inventory.getLeggings(), inventory.getBoots(), PlayerInfoManager.from(target).getSelected());

					// Set new armors
					List<ItemStack> stacks = WardrobeManager.from(target).getEquipAll((slot - 35) + 9 * (page - 1));
					target.getInventory().setHelmet(stacks.get(0));
					target.getInventory().setChestplate(stacks.get(1));
					target.getInventory().setLeggings(stacks.get(2));
					target.getInventory().setBoots(stacks.get(3));
					PlayerInfoManager.from(target).setSelected((slot - 35) + 9 * (page - 1));
					PlayerWardrobeMenu.showTo(player, target, page);
				} else if (click == ClickType.RIGHT) {  // Right click to edit the slot
					PlayerKitSetMenu.showTo(player, target, (slot - 35) + 9 * (page - 1), page);
				}
			}

			if (slot == 45 && button == lastPageButton) PlayerWardrobeMenu.showTo(player, target, this.page - 1);
			if (slot == 53 && button == nextPageButton) PlayerWardrobeMenu.showTo(player, target, this.page + 1);

			Cooldown.setCooldown(player, 0.5);
		}
	}

	public static void showTo(Player player, Player target, int page) {
		setSound(null);
		// Check if file no exist, that mean not edit empty kit menu.
		File file = FileUtil.getFile("menu/empty_kit.yml");
		if (!file.exists()) {
			if (player.isOp()) {
				Common.tellNoPrefix(player, "&f[&#df9820W&#bf9a40a&#9f9d60r&#80a080d&#60a29fr&#40a5bfo&#20a7dfb&#00aaffe&f] &cYou need to edit the empty set menu first!");
				Common.tellNoPrefix(player, "&f[&#df9820W&#bf9a40a&#9f9d60r&#80a080d&#60a29fr&#40a5bfo&#20a7dfb&#00aaffe&f] &cType &f/wardrobe settings");
				Common.tellNoPrefix(player, "&f[&#df9820W&#bf9a40a&#9f9d60r&#80a080d&#60a29fr&#40a5bfo&#20a7dfb&#00aaffe&f] &9Menu Settings &c-> &aEmpty kit menu");
			} else {
				Common.tellNoPrefix(player, "&f[&#df9820W&#bf9a40a&#9f9d60r&#80a080d&#60a29fr&#40a5bfo&#20a7dfb&#00aaffe&f] &cContact owner to initialize the plugin");
			}
			return;
		}
		// Check if file no exist, that mean not edit buttons.
		File file2 = FileUtil.getFile("menu/buttons.yml");
		if (!file2.exists()) {
			if (player.isOp()) {
				Common.tellNoPrefix(player, "&f[&#df9820W&#bf9a40a&#9f9d60r&#80a080d&#60a29fr&#40a5bfo&#20a7dfb&#00aaffe&f] &cYou need to set buttons!");
				Common.tellNoPrefix(player, "&f[&#df9820W&#bf9a40a&#9f9d60r&#80a080d&#60a29fr&#40a5bfo&#20a7dfb&#00aaffe&f] &cType &f/wardrobe settings");
				Common.tellNoPrefix(player, "&f[&#df9820W&#bf9a40a&#9f9d60r&#80a080d&#60a29fr&#40a5bfo&#20a7dfb&#00aaffe&f] &9Menu Settings &c-> &9Buttons");
			} else {
				Common.tellNoPrefix(player, "&f[&#df9820W&#bf9a40a&#9f9d60r&#80a080d&#60a29fr&#40a5bfo&#20a7dfb&#00aaffe&f] &cContact owner to initialize the plugin");
			}
			return;
		} else {
			if (player.isOp()) {
				boolean warning = false;
				for (int i = 0; i <= 4; i++)
					if (ButtonsSetting.getInstance().getButtons().get(i) == null) {
						warning = true;
						break;
					}
				if (warning)
					Common.tellNoPrefix(player, "&f[&#df9820W&#bf9a40a&#9f9d60r&#80a080d&#60a29fr&#40a5bfo&#20a7dfb&#00aaffe&f] &cYou need to set every buttons!");
				else new PlayerWardrobeMenu(player, target, page).displayTo(player);
			}
		}
		// Check if file no exist, that mean not edit border.
		File file3 = FileUtil.getFile("menu/border.yml");
		if (!file3.exists()) {
			if (player.isOp()) {
				Common.tellNoPrefix(player, "&f[&#df9820W&#bf9a40a&#9f9d60r&#80a080d&#60a29fr&#40a5bfo&#20a7dfb&#00aaffe&f] &cYou need to edit the borders!");
				Common.tellNoPrefix(player, "&f[&#df9820W&#bf9a40a&#9f9d60r&#80a080d&#60a29fr&#40a5bfo&#20a7dfb&#00aaffe&f] &cType &f/wardrobe settings");
				Common.tellNoPrefix(player, "&f[&#df9820W&#bf9a40a&#9f9d60r&#80a080d&#60a29fr&#40a5bfo&#20a7dfb&#00aaffe&f] &9Menu Settings &c-> &9Buttons &c-> &5Border Setting");
			} else {
				Common.tellNoPrefix(player, "&f[&#df9820W&#bf9a40a&#9f9d60r&#80a080d&#60a29fr&#40a5bfo&#20a7dfb&#00aaffe&f] &cContact owner to initialize the plugin");
			}
			return;
		}
		new PlayerWardrobeMenu(player, target, page).displayTo(player);
	}

	private void updatePermissionSlot(User user) {
		List<Integer> permissions = new ArrayList<>();
		for (Node node : user.resolveInheritedNodes(QueryOptions.nonContextual())) {
			String key = node.getKey();
			if (key.startsWith("wardrobe.slot."))
				permissions.add(Integer.valueOf(key.replace("wardrobe.slot.", "")));
		}
		WardrobeManager.from(Bukkit.getPlayer(user.getUniqueId())).updatePermissionSlot(permissions);
	}

	private ItemStack getEquip(int line, List<ItemStack> buttons, int id) {
		WardrobeManager.Equip equip = null;
		switch (line) {
			case 1 -> equip = WardrobeManager.Equip.HELMET;
			case 2 -> equip = WardrobeManager.Equip.CHESTPLATE;
			case 3 -> equip = WardrobeManager.Equip.LEGGINGS;
			case 4 -> equip = WardrobeManager.Equip.BOOTS;
		}
		return WardrobeManager.from(target).getEquip(equip, id) == null ? (buttons.get(5) == null ? CompMaterial.AIR.toItem() : buttons.get(5)) : WardrobeManager.from(target).getEquip(equip, id);
	}
}
