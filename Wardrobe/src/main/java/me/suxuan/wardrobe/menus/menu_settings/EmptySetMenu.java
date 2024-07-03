package me.suxuan.wardrobe.menus.menu_settings;

import me.suxuan.wardrobe.settings.EmptySetting;
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
import java.util.List;

// This menu is for empty set setting.
public class EmptySetMenu extends Menu {

	public EmptySetMenu(Player player) {
		this.setTitle("&cReplace with own items!");
		this.setViewer(player);
		this.setSize(9 * 4);
	}

	@Override
	public ItemStack getItemAt(int slot) {
		File file = FileUtil.getFile("menu/empty_kit.yml");
		if (file.exists()) {
			List<ItemStack> setting = EmptySetting.getInstance().getMenu();
			return slot < setting.size() ? setting.get(slot) : NO_ITEM;
		} else {
			// Get the column
			int column = slot % 9 + 1;
			return switch (column) {
				case 1 -> ItemCreator.of(CompMaterial.RED_STAINED_GLASS_PANE,
						"Qute line",
						"",
						"&7Will be replaced with the kit",
						"&7when player put kit here.").make();
				case 2 -> ItemCreator.of(CompMaterial.ORANGE_STAINED_GLASS_PANE,
						"Qute line",
						"",
						"&7Will be replaced with the kit",
						"&7when player put kit here.").make();
				case 3 -> ItemCreator.of(CompMaterial.YELLOW_STAINED_GLASS_PANE,
						"Qute line",
						"",
						"&7Will be replaced with the kit",
						"&7when player put kit here.").make();
				case 4 -> ItemCreator.of(CompMaterial.GREEN_STAINED_GLASS_PANE,
						"Qute line",
						"",
						"&7Will be replaced with the kit",
						"&7when player put kit here.").make();
				case 5 -> ItemCreator.of(CompMaterial.BLUE_STAINED_GLASS_PANE,
						"Qute line",
						"",
						"&7Will be replaced with the kit",
						"&7when player put kit here.").make();
				case 6 -> ItemCreator.of(CompMaterial.PINK_STAINED_GLASS_PANE,
						"Qute line",
						"",
						"&7Will be replaced with the kit",
						"&7when player put kit here.").make();
				case 7 -> ItemCreator.of(CompMaterial.PURPLE_STAINED_GLASS_PANE,
						"Qute line",
						"",
						"&7Will be replaced with the kit",
						"&7when player put kit here.").make();
				case 8 -> ItemCreator.of(CompMaterial.BROWN_STAINED_GLASS_PANE,
						"Qute line",
						"",
						"&7Will be replaced with the kit",
						"&7when player put kit here.").make();
				case 9 -> ItemCreator.of(CompMaterial.CYAN_STAINED_GLASS_PANE,
						"Qute line",
						"",
						"&7Will be replaced with the kit",
						"&7when player put kit here.").make();
				default -> throw new IllegalStateException("Column calculate error : " + column);
			};
		}
	}

	@Override
	protected boolean addInfoButton() {
		return false;
	}

	@Override
	protected boolean isActionAllowed(MenuClickLocation location, int slot, @Nullable ItemStack clicked,
									  @Nullable ItemStack cursor) {
		return true;
	}

	@Override
	protected void onMenuClose(Player player, Inventory inventory) {
		final StrictMap<Integer, ItemStack> items = new StrictMap<>();

		for (int slot = 0; slot < this.getSize(); slot++) {
			final ItemStack item = inventory.getItem(slot);
			items.put(slot, item);
		}
		EmptySetting.getInstance().setMenu(new ArrayList<>(items.values()));
		SettingsMenu.showTo(getViewer());
	}

	public static void showTo(Player player) {
		setSound(null);
		new EmptySetMenu(player).displayTo(player);
	}
}
