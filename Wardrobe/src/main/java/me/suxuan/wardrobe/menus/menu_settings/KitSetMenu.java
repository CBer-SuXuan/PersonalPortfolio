package me.suxuan.wardrobe.menus.menu_settings;

import me.suxuan.wardrobe.settings.KitSetting;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.mineacademy.fo.FileUtil;
import org.mineacademy.fo.collection.StrictMap;
import org.mineacademy.fo.menu.Menu;
import org.mineacademy.fo.menu.model.ItemCreator;
import org.mineacademy.fo.menu.model.MenuClickLocation;
import org.mineacademy.fo.remain.CompMaterial;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class KitSetMenu extends Menu {

	public KitSetMenu(Player player) {
		this.setTitle("&cReplace with own items!");
		this.setViewer(player);
		this.setSize(9 * 6);
	}

	@Override
	public ItemStack getItemAt(int slot) {
		File file = FileUtil.getFile("menu/edit_kit.yml");
		if (file.exists()) {
			List<ItemStack> setting = KitSetting.getInstance().getMenu();
			return slot < setting.size() ? setting.get(slot) : NO_ITEM;
		} else {
			if (slot == 13)
				return ItemCreator.of(CompMaterial.DIAMOND_HELMET, "&6Diamond Helmet",
						"",
						"&7This slot is for server players",
						"&7to drop their &6Helmet &7equipment at here",
						"&8You can't edit this!").make();
			if (slot == 22)
				return ItemCreator.of(CompMaterial.DIAMOND_CHESTPLATE, "&6Diamond Chestplate",
						"",
						"&7This slot is for server players",
						"&7to drop their &6Chestplate &7equipment at here",
						"&8You can't edit this!").make();
			if (slot == 31)
				return ItemCreator.of(CompMaterial.DIAMOND_LEGGINGS, "&6Diamond Leggings",
						"",
						"&7This slot is for server players",
						"&7to drop their &6Leggings &7equipment at here",
						"&8You can't edit this!").make();
			if (slot == 40)
				return ItemCreator.of(CompMaterial.DIAMOND_BOOTS, "&6Diamond Boots",
						"",
						"&7This slot is for server players",
						"&7to drop their &6Boots &7equipment at here",
						"&8You can't edit this!").make();
			if (Arrays.asList(0, 8, 45, 53).contains(slot))
				return ItemCreator.of(CompMaterial.BLUE_STAINED_GLASS_PANE, " ").make();
			if (Arrays.asList(1, 7, 9, 17, 18, 26, 27, 35, 36, 44, 46, 52).contains(slot))
				return ItemCreator.of(CompMaterial.GREEN_STAINED_GLASS_PANE, " ").make();
			if (Arrays.asList(2, 6, 10, 16, 19, 25, 28, 34, 37, 43, 47, 51).contains(slot))
				return ItemCreator.of(CompMaterial.YELLOW_STAINED_GLASS_PANE, " ").make();
			if (Arrays.asList(3, 5, 11, 15, 20, 24, 29, 33, 38, 42, 48, 50).contains(slot))
				return ItemCreator.of(CompMaterial.ORANGE_STAINED_GLASS_PANE, " ").make();
			if (Arrays.asList(4, 12, 14, 21, 23, 30, 32, 39, 41, 49).contains(slot))
				return ItemCreator.of(CompMaterial.RED_STAINED_GLASS_PANE, " ").make();
		}
		return NO_ITEM;
	}

	@Override
	protected boolean addInfoButton() {
		return false;
	}

	@Override
	protected boolean isActionAllowed(MenuClickLocation location, int slot, @Nullable ItemStack clicked,
									  @Nullable ItemStack cursor) {
		return !Arrays.asList(13, 22, 31, 40).contains(slot);
	}

	@Override
	protected void onMenuClose(Player player, Inventory inventory) {
		final StrictMap<Integer, ItemStack> items = new StrictMap<>();

		for (int slot = 0; slot < this.getSize(); slot++) {
			final ItemStack item = inventory.getItem(slot);
			items.put(slot, item);
		}
		KitSetting.getInstance().setMenu(new ArrayList<>(items.values()));
		SettingsMenu.showTo(getViewer());
	}

	public static void showTo(Player player) {
		setSound(null);
		new KitSetMenu(player).displayTo(player);
	}
}
