package me.suxuan.quests.menu;

import me.arcaniax.hdb.api.HeadDatabaseAPI;
import me.suxuan.quests.QuestsPlugin;
import me.suxuan.quests.cache.CategoriesCache;
import me.suxuan.quests.cache.ConfigSettings;
import me.suxuan.quests.utils.PageUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.mineacademy.fo.collection.SerializedMap;
import org.mineacademy.fo.menu.Menu;
import org.mineacademy.fo.menu.button.Button;
import org.mineacademy.fo.menu.model.ItemCreator;
import org.mineacademy.fo.remain.CompMaterial;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CategoriesMenu extends Menu {

	// Every categories button
	private final List<Button> categoriesButton = new ArrayList<>();
	// Surrounding button
	private Button surroundButton = Button.makeEmpty();
	private boolean morePages = false;
	private int pageNumber = 1;
	private final List<Button> positionButton = new ArrayList<>();
	// Get all registered category files
	File[] categoryFiles = new File(QuestsPlugin.getInstance().getDataFolder(), "categories")
			.listFiles((dir, name) -> !name.equals("template.yml"));

	CategoriesMenu(Player player, int page) {
		this.pageNumber = page;
		this.morePages = PageUtil.needAddPages("categories");  // if need more pages?

		// title
		if (morePages) this.setTitle(ConfigSettings.MAIN_MENU_TITLE.replace("{page}", String.valueOf(page)));
		else this.setTitle(ConfigSettings.MAIN_MENU_TITLE);

		this.setSize(9 * ConfigSettings.MAIN_MENU_SIZE);  // size
		if (categoryFiles.length == 0) return;  // Check if no file in categories.

		// ------------------------------------------------------------------------------------------------
		SerializedMap surroundButton = ConfigSettings.MAIN_MENU_SURROUND;
		// Make surrounding button
		if (surroundButton.getBoolean("Use"))
			this.surroundButton = Button.makeDummy(ItemCreator.of(CompMaterial.fromString(surroundButton.getString("Icon")))
					.name(surroundButton.getString("Name"))
					.lore(surroundButton.getStringList("Details"))
					.glow(surroundButton.getBoolean("Glowing")));
		else this.surroundButton = Button.makeEmpty();
		// ------------------------------------------------------------------------------------------------

		// ------------------------------------------------------------------------------------------------
		// Make all category button after checking if using head database.
		if (QuestsPlugin.getInstance().isUseHead()) {
			HeadDatabaseAPI api = new HeadDatabaseAPI();
			if (morePages) {
				positionButton.add(makePositionButtons("next", true));
				positionButton.add(makePositionButtons("last", true));
				positionButton.add(makePositionButtons("no_next", true));
				positionButton.add(makePositionButtons("no_last", true));
			}
			for (int i = 0; i < CategoriesCache.cacheMap.size(); i++) {
				CategoriesCache cache = CategoriesCache.from(categoryFiles[i].getName().replace(".yml", ""));
				// Make all direction button after check if need pages.
				if (morePages) {
					if (Integer.parseInt(cache.getPosition().split(";")[0]) == page) {
						this.categoriesButton.add(Button.makeDummy(ItemCreator
								.of(checkIfAllNumber(cache.getIcon()) ?
										api.getItemHead(cache.getIcon()) :
										CompMaterial.fromString(cache.getIcon()).toItem())
								.name(cache.getName())
								.lore(cache.getDetails())
								.glow(cache.getGlowing())));
					}
				} else {
					this.categoriesButton.add(Button.makeDummy(ItemCreator
							.of(checkIfAllNumber(cache.getIcon()) ?
									api.getItemHead(cache.getIcon()) :
									CompMaterial.fromString(cache.getIcon()).toItem())
							.name(cache.getName())
							.lore(cache.getDetails())
							.glow(cache.getGlowing())));
				}

			}
		} else {
			// Make all direction button after check if need pages.
			if (morePages) {
				positionButton.add(makePositionButtons("next", false));
				positionButton.add(makePositionButtons("last", false));
				positionButton.add(makePositionButtons("no_next", false));
				positionButton.add(makePositionButtons("no_last", false));
			}
			for (int i = 0; i < CategoriesCache.cacheMap.size(); i++) {
				CategoriesCache cache = CategoriesCache.from(categoryFiles[i].getName().replace(".yml", ""));
				if (morePages) {
					if (Integer.parseInt(cache.getPosition().split(";")[0]) == page) {
						this.categoriesButton.add(Button.makeDummy(ItemCreator
								.of(CompMaterial.fromString(cache.getIcon()))
								.name(cache.getName())
								.lore(cache.getDetails())
								.glow(cache.getGlowing())));
					}
				} else {
					this.categoriesButton.add(Button.makeDummy(ItemCreator
							.of(CompMaterial.fromString(cache.getIcon()))
							.name(cache.getName())
							.lore(cache.getDetails())
							.glow(cache.getGlowing())));
				}

			}
		}
		// ------------------------------------------------------------------------------------------------

	}

	@Override
	public ItemStack getItemAt(int slot) {

		int row = ConfigSettings.MAIN_MENU_SIZE;
		// Set category button
		for (int i = 0; i < categoriesButton.size(); i++) {
			CategoriesCache cache = CategoriesCache.from(categoryFiles[i].getName().replace(".yml", ""));
			if (slot == (Integer.parseInt(cache.getPosition().split(";")[1]) - 1) * 9
					+ Integer.parseInt(cache.getPosition().split(";")[2]) - 1)
				return this.categoriesButton.get(i).getItem();
		}
		// Set page change button
		if (morePages) {
			if (pageNumber == 1) {
				if (slot == (Integer.parseInt(ConfigSettings.MAIN_DIRECTION_BUTTON.getMap("next").getString("Position").split(";")[0]) - 1) * 9
						+ Integer.parseInt(ConfigSettings.MAIN_DIRECTION_BUTTON.getMap("next").getString("Position").split(";")[1]) - 1)
					return this.positionButton.get(0).getItem();
				if (slot == (Integer.parseInt(ConfigSettings.MAIN_DIRECTION_BUTTON.getMap("no_last").getString("Position").split(";")[0]) - 1) * 9
						+ Integer.parseInt(ConfigSettings.MAIN_DIRECTION_BUTTON.getMap("no_last").getString("Position").split(";")[1]) - 1)
					return this.positionButton.get(3).getItem();
			} else if (pageNumber == PageUtil.getMaxPage("categories")) {
				if (slot == (Integer.parseInt(ConfigSettings.MAIN_DIRECTION_BUTTON.getMap("no_next").getString("Position").split(";")[0]) - 1) * 9
						+ Integer.parseInt(ConfigSettings.MAIN_DIRECTION_BUTTON.getMap("no_next").getString("Position").split(";")[1]) - 1)
					return this.positionButton.get(2).getItem();
				if (slot == (Integer.parseInt(ConfigSettings.MAIN_DIRECTION_BUTTON.getMap("last").getString("Position").split(";")[0]) - 1) * 9
						+ Integer.parseInt(ConfigSettings.MAIN_DIRECTION_BUTTON.getMap("last").getString("Position").split(";")[1]) - 1)
					return this.positionButton.get(1).getItem();
			} else {
				if (slot == (Integer.parseInt(ConfigSettings.MAIN_DIRECTION_BUTTON.getMap("next").getString("Position").split(";")[0]) - 1) * 9
						+ Integer.parseInt(ConfigSettings.MAIN_DIRECTION_BUTTON.getMap("next").getString("Position").split(";")[1]) - 1)
					return this.positionButton.get(0).getItem();
				if (slot == (Integer.parseInt(ConfigSettings.MAIN_DIRECTION_BUTTON.getMap("last").getString("Position").split(";")[0]) - 1) * 9
						+ Integer.parseInt(ConfigSettings.MAIN_DIRECTION_BUTTON.getMap("last").getString("Position").split(";")[1]) - 1)
					return this.positionButton.get(1).getItem();
			}
		}
		// Set surrounding button
		if (row >= 3) {
			List<Integer> surround_slot = new ArrayList<>(Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 17));
			for (int i = 1; i <= row - 2; i++) {
				surround_slot.add(9 * (i + 1));
				surround_slot.add(9 * (i + 1) + 8);
			}
			int temp = 9 * (row - 1);
			for (int i = 1; i <= 7; i++)
				surround_slot.add(temp + i);
			if (surround_slot.contains(slot))
				return this.surroundButton.getItem();
		}
		return NO_ITEM;

	}

	@Override
	protected void onMenuClick(Player player, int slot, InventoryAction action, ClickType click, ItemStack cursor, ItemStack clicked, boolean cancelled) {
		String file = getFileFromCategory(slot);
		if (file != null && click.isLeftClick()) {
			CategoryMenu.showTo(this, player, CategoriesCache.from(file).getQuests(), 1);
			return;
		}
		for (int i = 0; i < positionButton.size(); i++) {
			if (clicked.equals(positionButton.get(i).getItem())) {
				switch (i) {
					case 0 -> {
						CategoriesMenu.showTo(player, pageNumber + 1);
						return;
					}
					case 1 -> {
						CategoriesMenu.showTo(player, pageNumber - 1);
						return;
					}
				}
			}
		}
	}

	@Override
	protected boolean addInfoButton() {
		return false;
	}

	private String getFileFromCategory(int slot) {
		ItemMeta meta = getItemAt(slot).getItemMeta();
		if (meta == null) return null;
		String display_name = meta.getDisplayName().replaceAll("§{1}[a-z0-9]{1}", "");
		for (int i = 0; i < CategoriesCache.cacheMap.size(); i++) {
			CategoriesCache cache = CategoriesCache.from(categoryFiles[i].getName().replace(".yml", ""));
			if (cache.getName().replaceAll("&{1}[a-z0-9]{1}", "").equals(display_name))
				return cache.getCategoryFile();
		}
		return null;
	}

	private Boolean checkIfAllNumber(String check) {
		if (check == null) return false;
		for (char c : check.toCharArray()) {
			if (!Character.isDigit(c)) return false;
		}
		return true;
	}

	private Button makePositionButtons(String type, boolean useHead) {
		Button temp;
		if (useHead) {
			HeadDatabaseAPI api = new HeadDatabaseAPI();
			ItemStack head = api.getItemHead(ConfigSettings.MAIN_DIRECTION_BUTTON.getMap(type).getString("Icon"));
			temp = Button.makeDummy(ItemCreator.of(checkIfAllNumber(ConfigSettings.MAIN_DIRECTION_BUTTON.getMap(type).getString("Icon")) ?
							CompMaterial.fromItem(head) : CompMaterial.fromString(ConfigSettings.MAIN_DIRECTION_BUTTON.getMap(type).getString("Icon")))
					.name(ConfigSettings.MAIN_DIRECTION_BUTTON.getMap(type).getString("Name"))
					.lore(ConfigSettings.MAIN_DIRECTION_BUTTON.getMap(type).getStringList("Details"))
					.glow(ConfigSettings.MAIN_DIRECTION_BUTTON.getMap(type).getBoolean("Glowing")));
		} else
			temp = Button.makeDummy(ItemCreator.of(CompMaterial.fromString(ConfigSettings.MAIN_DIRECTION_BUTTON.getMap(type).getString("Icon")))
					.name(ConfigSettings.MAIN_DIRECTION_BUTTON.getMap(type).getString("Name"))
					.lore(ConfigSettings.MAIN_DIRECTION_BUTTON.getMap(type).getStringList("Details"))
					.glow(ConfigSettings.MAIN_DIRECTION_BUTTON.getMap(type).getBoolean("Glowing")));
		return temp;
	}

	public static void showTo(Player player, int page) {
		setSound(null);
		new CategoriesMenu(player, page).displayTo(player);
	}

}
