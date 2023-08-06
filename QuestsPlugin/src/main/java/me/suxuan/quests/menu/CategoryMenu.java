package me.suxuan.quests.menu;

import me.arcaniax.hdb.api.HeadDatabaseAPI;
import me.suxuan.quests.QuestsPlugin;
import me.suxuan.quests.cache.ConfigSettings;
import me.suxuan.quests.cache.PlayerCache;
import me.suxuan.quests.cache.QuestsCache;
import me.suxuan.quests.utils.PageUtil;
import me.suxuan.quests.utils.QuestsUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.mineacademy.fo.collection.SerializedMap;
import org.mineacademy.fo.menu.Menu;
import org.mineacademy.fo.menu.button.Button;
import org.mineacademy.fo.menu.model.ItemCreator;
import org.mineacademy.fo.model.Variables;
import org.mineacademy.fo.remain.CompMaterial;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class CategoryMenu extends Menu {

	private final List<Button> questsButton = new ArrayList<>();
	private Button surroundButton = Button.makeEmpty();
	private boolean morePages = false;
	private int pageNumber = 1;
	private final List<Button> positionButton = new ArrayList<>();
	private Button backButton;
	private final List<String> categoryButton;

	CategoryMenu(Menu parent, Player player, List<String> category, int page) {

		super(parent);
		this.pageNumber = page;
		this.morePages = PageUtil.needAddPages("quests");

		if (morePages) this.setTitle(ConfigSettings.SUB_MENU_TITLE.replace("{page}", String.valueOf(page)));
		else this.setTitle(ConfigSettings.SUB_MENU_TITLE);

		this.setSize(9 * ConfigSettings.SUB_MENU_SIZE);

		backButton = Button.makeSimple(ItemCreator.of(CompMaterial.fromString(ConfigSettings.SUB_MENU_BACK.getString("Icon")))
				.name(ConfigSettings.SUB_MENU_BACK.getString("Name"))
				.lore(ConfigSettings.SUB_MENU_BACK.getStringList("Details")), player1 -> CategoriesMenu.showTo(player1, 1));

		// ----------------------------------Make surrounding button----------------------------------------------
		SerializedMap surround_button = ConfigSettings.SUB_MENU_SURROUND;
		if (surround_button.getBoolean("Use"))
			this.surroundButton = Button.makeDummy(ItemCreator.of(CompMaterial.fromString(surround_button.getString("Icon")))
					.name(surround_button.getString("Name"))
					.lore(surround_button.getStringList("Details"))
					.glow(surround_button.getBoolean("Glowing")));
		else this.surroundButton = Button.makeEmpty();
		// -------------------------------------------------------------------------------------------------------
		this.categoryButton = category;
		if (QuestsPlugin.getInstance().isUseHead()) {
			HeadDatabaseAPI api = new HeadDatabaseAPI();
			if (morePages) {
				positionButton.add(makePositionButtons("next", true));
				positionButton.add(makePositionButtons("last", true));
				positionButton.add(makePositionButtons("no_next", true));
				positionButton.add(makePositionButtons("no_last", true));
			}

			backButton = Button.makeSimple(ItemCreator
					.of(checkIfAllNumber(ConfigSettings.SUB_MENU_BACK.getString("Icon")) ?
							api.getItemHead(ConfigSettings.SUB_MENU_BACK.getString("Icon")) :
							CompMaterial.fromString(ConfigSettings.SUB_MENU_BACK.getString("Icon")).toItem())
					.name(ConfigSettings.SUB_MENU_BACK.getString("Name"))
					.lore(ConfigSettings.SUB_MENU_BACK.getStringList("Details"))
					.glow(ConfigSettings.SUB_MENU_BACK.getBoolean("Glowing")), player1 -> CategoriesMenu.showTo(player1, 1));

			// ---------------------------------------Make quests button-------------------------------------------------
			for (String s : this.categoryButton) {
				QuestsCache cache = QuestsCache.from(s.replace(".yml", ""));
				if (PlayerCache.from(player).getDone_quests().contains(s.replace(".yml", ""))) {
					if (Integer.parseInt(cache.getPosition().split(";")[0]) == page) {
						Boolean glowing = cache.getFinishedQuest().getBoolean("Glowing");
						this.questsButton.add(Button.makeDummy(ItemCreator
								.of(checkIfAllNumber(cache.getFinishedQuest().getString("Icon")) ?
										api.getItemHead(cache.getFinishedQuest().getString("Icon")) :
										CompMaterial.fromString(cache.getFinishedQuest().getString("Icon")).toItem())
								.name(cache.getFinishedQuest().getString("Name"))
								.lore(cache.getFinishedQuest().getStringList("Details"))
								.glow(glowing)));
					}
				} else if (PlayerCache.from(player).getDoing_quest().equals(s.replace(".yml", ""))) {
					if (Integer.parseInt(cache.getPosition().split(";")[0]) == page) {
						Boolean glowing = cache.getAcceptedQuest().getBoolean("Glowing");
						this.questsButton.add(Button.makeDummy(ItemCreator
								.of(checkIfAllNumber(cache.getAcceptedQuest().getString("Icon")) ?
										api.getItemHead(cache.getAcceptedQuest().getString("Icon")) :
										CompMaterial.fromString(cache.getAcceptedQuest().getString("Icon")).toItem())
								.name(cache.getAcceptedQuest().getString("Name"))
								.lore(Variables.replace(cache.getAcceptedQuest().getStringList("Details"), player, getReplaceMap(cache, player)))
								.glow(glowing)));
					}
				} else {
					if (Integer.parseInt(cache.getPosition().split(";")[0]) == page) {
						Boolean glowing = cache.getUnacceptedQuest().getBoolean("Glowing");
						this.questsButton.add(Button.makeDummy(ItemCreator
								.of(checkIfAllNumber(cache.getUnacceptedQuest().getString("Icon")) ?
										api.getItemHead(cache.getUnacceptedQuest().getString("Icon")) :
										CompMaterial.fromString(cache.getUnacceptedQuest().getString("Icon")).toItem())
								.name(cache.getUnacceptedQuest().getString("Name"))
								.lore(cache.getUnacceptedQuest().getStringList("Details"))
								.glow(glowing)));
					}
				}
			}
			// -------------------------------------------------------------------------------------------------------
		} else {
			if (morePages) {
				positionButton.add(makePositionButtons("next", false));
				positionButton.add(makePositionButtons("last", false));
				positionButton.add(makePositionButtons("no_next", false));
				positionButton.add(makePositionButtons("no_last", false));
			}
			backButton = Button.makeSimple(ItemCreator
					.of(CompMaterial.fromString(ConfigSettings.SUB_MENU_BACK.getString("Icon")))
					.name(ConfigSettings.SUB_MENU_BACK.getString("Name"))
					.lore(ConfigSettings.SUB_MENU_BACK.getStringList("Details"))
					.glow(ConfigSettings.SUB_MENU_BACK.getBoolean("Glowing")), player1 -> CategoriesMenu.showTo(player1, 1));
			for (String s : this.categoryButton) {
				QuestsCache cache = QuestsCache.from(s.replace(".yml", ""));
				if (PlayerCache.from(player).getStringList("Done_quests").contains(s.replace(".yml", ""))) {
					if (Integer.parseInt(cache.getPosition().split(";")[0]) == page) {
						Boolean glowing = cache.getFinishedQuest().getBoolean("Glowing");
						this.questsButton.add(Button.makeDummy(ItemCreator
								.of(CompMaterial.fromString(cache.getFinishedQuest().getString("Icon")))
								.name(cache.getFinishedQuest().getString("Name"))
								.lore(cache.getFinishedQuest().getStringList("Details"))
								.glow(glowing)));
					}
				} else if (PlayerCache.from(player).getString("Doing.quest").equals(s.replace(".yml", ""))) {
					if (Integer.parseInt(cache.getPosition().split(";")[0]) == page) {
						Boolean glowing = cache.getAcceptedQuest().getBoolean("Glowing");
						this.questsButton.add(Button.makeDummy(ItemCreator
								.of(CompMaterial.fromString(cache.getAcceptedQuest().getString("Icon")))
								.name(cache.getAcceptedQuest().getString("Name"))
								.lore(Variables.replace(cache.getAcceptedQuest().getStringList("Details"), player, getReplaceMap(cache, player)))
								.glow(glowing)));
					}
				} else {
					if (Integer.parseInt(cache.getPosition().split(";")[0]) == page) {
						Boolean glowing = cache.getUnacceptedQuest().getBoolean("Glowing");
						this.questsButton.add(Button.makeDummy(ItemCreator
								.of(CompMaterial.fromString(cache.getUnacceptedQuest().getString("Icon")))
								.name(cache.getUnacceptedQuest().getString("Name"))
								.lore(cache.getUnacceptedQuest().getStringList("Details"))
								.glow(glowing)));
					}
				}
			}
		}

	}

	@Override
	public ItemStack getItemAt(int slot) {

		int row = ConfigSettings.SUB_MENU_SIZE;
		for (int i = 0; i < this.categoryButton.size(); i++) {
			QuestsCache cache = QuestsCache.from(this.categoryButton.get(i).replace(".yml", ""));
			if (slot == (Integer.parseInt(cache.getPosition().split(";")[1]) - 1) * 9
					+ Integer.parseInt(cache.getPosition().split(";")[2]) - 1)
				return this.questsButton.get(i).getItem();
		}
		if (slot == (Integer.parseInt(ConfigSettings.SUB_MENU_BACK.getString("Position").split(";")[0]) - 1) * 9
				+ Integer.parseInt(ConfigSettings.SUB_MENU_BACK.getString("Position").split(";")[1]) - 1)
			return backButton.getItem();
		if (morePages) {
			if (pageNumber == 1) {
				if (slot == (Integer.parseInt(ConfigSettings.SUB_MENU_DIRECTION_BUTTON.getMap("next").getString("Position").split(";")[0]) - 1) * 9
						+ Integer.parseInt(ConfigSettings.SUB_MENU_DIRECTION_BUTTON.getMap("next").getString("Position").split(";")[1]) - 1)
					return this.positionButton.get(0).getItem();
				if (slot == (Integer.parseInt(ConfigSettings.SUB_MENU_DIRECTION_BUTTON.getMap("no_last").getString("Position").split(";")[0]) - 1) * 9
						+ Integer.parseInt(ConfigSettings.SUB_MENU_DIRECTION_BUTTON.getMap("no_last").getString("Position").split(";")[1]) - 1)
					return this.positionButton.get(3).getItem();
			} else if (pageNumber == PageUtil.getMaxPage("categories")) {
				if (slot == (Integer.parseInt(ConfigSettings.SUB_MENU_DIRECTION_BUTTON.getMap("no_next").getString("Position").split(";")[0]) - 1) * 9
						+ Integer.parseInt(ConfigSettings.SUB_MENU_DIRECTION_BUTTON.getMap("no_next").getString("Position").split(";")[1]) - 1)
					return this.positionButton.get(2).getItem();
				if (slot == (Integer.parseInt(ConfigSettings.SUB_MENU_DIRECTION_BUTTON.getMap("last").getString("Position").split(";")[0]) - 1) * 9
						+ Integer.parseInt(ConfigSettings.SUB_MENU_DIRECTION_BUTTON.getMap("last").getString("Position").split(";")[1]) - 1)
					return this.positionButton.get(1).getItem();
			} else {
				if (slot == (Integer.parseInt(ConfigSettings.SUB_MENU_DIRECTION_BUTTON.getMap("next").getString("Position").split(";")[0]) - 1) * 9
						+ Integer.parseInt(ConfigSettings.SUB_MENU_DIRECTION_BUTTON.getMap("next").getString("Position").split(";")[1]) - 1)
					return this.positionButton.get(0).getItem();
				if (slot == (Integer.parseInt(ConfigSettings.SUB_MENU_DIRECTION_BUTTON.getMap("last").getString("Position").split(";")[0]) - 1) * 9
						+ Integer.parseInt(ConfigSettings.SUB_MENU_DIRECTION_BUTTON.getMap("last").getString("Position").split(";")[1]) - 1)
					return this.positionButton.get(1).getItem();
			}
		}
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
		if (getFileFromUnacceptedQuest(slot) != null && click.isLeftClick()) {
			if (!PlayerCache.from(player).getDoing_quest().equals("")) return;
			String file_name = getFileFromUnacceptedQuest(slot);
			if (!QuestsUtil.startRequest(file_name, player)) return;
			player.closeInventory();
			CategoryMenu.showTo(getParent(), player, this.categoryButton, 1);
			return;
		}
		if (getFileFromAcceptedQuest(slot) != null && click.isRightClick()) {
			CategoryCancelMenu.showTo(this, player, getFileFromAcceptedQuest(slot), this.categoryButton);
		}
		for (int i = 0; i < positionButton.size(); i++) {
			if (clicked.equals(positionButton.get(i).getItem())) {
				switch (i) {
					case 0 -> {
						CategoryMenu.showTo(getParent(), player, this.categoryButton, pageNumber + 1);
						return;
					}
					case 1 -> {
						CategoryMenu.showTo(getParent(), player, this.categoryButton, pageNumber - 1);
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

	private String getFileFromUnacceptedQuest(int slot) {
		ItemMeta meta = getItemAt(slot).getItemMeta();
		if (meta == null) return null;
		String display_name = meta.getDisplayName().replaceAll("§{1}[a-z0-9]{1}", "");
		for (String s : this.categoryButton) {
			QuestsCache cache = QuestsCache.from(s.replace(".yml", ""));
			if (cache.getUnacceptedQuest().getString("Name").replaceAll("&{1}[a-z0-9]{1}", "").equals(display_name))
				return cache.getQuest_file_name();
		}
		return null;
	}

	private String getFileFromAcceptedQuest(int slot) {
		ItemMeta meta = getItemAt(slot).getItemMeta();
		if (meta == null) return null;
		String display_name = meta.getDisplayName().replaceAll("§{1}[a-z0-9]{1}", "");
		for (String s : this.categoryButton) {
			QuestsCache cache = QuestsCache.from(s.replace(".yml", ""));
			if (cache.getAcceptedQuest().getString("Name").replaceAll("&{1}[a-z0-9]{1}", "").equals(display_name))
				return cache.getQuest_file_name();
		}
		return null;
	}

	private Map<String, Object> getReplaceMap(QuestsCache cache, Player player) {
		SerializedMap placeholder_map = new SerializedMap();
		for (Map.Entry<String, Object> map : cache.getPlaceholder()) {
			PlayerCache playerCache = PlayerCache.from(player);
			String type = cache.getPlaceholder().getString(map.getKey());
			if (playerCache.getDoing_types().contains("Break")) {
				int done;
				int need = 0;
				for (String s : playerCache.getBreak_done())
					if (s.split(";")[0].equals(type)) {
						done = Integer.parseInt(s.split(";")[1]);
						for (String s2 : playerCache.getBreak_quests())
							if (s2.split(";")[0].equals(type))
								need = Integer.parseInt(s2.split(";")[1]);
						placeholder_map.override(map.getKey(), Math.min(need, done));
					}
			}
			if (playerCache.getDoing_types().contains("Kill")) {
				int done;
				int need = 0;
				for (String s : playerCache.getKill_done())
					if (s.split(";")[0].equals(type)) {
						done = Integer.parseInt(s.split(";")[1]);
						for (String s2 : playerCache.getKill_quests())
							if (s2.split(";")[0].equals(type))
								need = Integer.parseInt(s2.split(";")[1]);
						placeholder_map.override(map.getKey(), Math.min(need, done));
					}
			}
			if (playerCache.getDoing_types().contains("Catch")) {
				int done;
				int need = 0;
				for (String s : playerCache.getCatch_done())
					if (s.split(";")[0].equals(type)) {
						done = Integer.parseInt(s.split(";")[1]);
						for (String s2 : playerCache.getCatch_quests())
							if (s2.split(";")[0].equals(type))
								need = Integer.parseInt(s2.split(";")[1]);
						placeholder_map.override(map.getKey(), Math.min(need, done));
					}
			}
		}
		return placeholder_map.asMap();
	}

	@Override
	protected boolean addReturnButton() {
		return false;
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
			ItemStack head = api.getItemHead(ConfigSettings.SUB_MENU_DIRECTION_BUTTON.getMap(type).getString("Icon"));
			temp = Button.makeDummy(ItemCreator.of(checkIfAllNumber(ConfigSettings.SUB_MENU_DIRECTION_BUTTON.getMap(type).getString("Icon")) ?
							CompMaterial.fromItem(head) : CompMaterial.fromString(ConfigSettings.SUB_MENU_DIRECTION_BUTTON.getMap(type).getString("Icon")))
					.name(ConfigSettings.SUB_MENU_DIRECTION_BUTTON.getMap(type).getString("Name"))
					.lore(ConfigSettings.SUB_MENU_DIRECTION_BUTTON.getMap(type).getStringList("Details"))
					.glow(ConfigSettings.SUB_MENU_DIRECTION_BUTTON.getMap(type).getBoolean("Glowing")));
		} else
			temp = Button.makeDummy(ItemCreator.of(CompMaterial.fromString(ConfigSettings.SUB_MENU_DIRECTION_BUTTON.getMap(type).getString("Icon")))
					.name(ConfigSettings.SUB_MENU_DIRECTION_BUTTON.getMap(type).getString("Name"))
					.lore(ConfigSettings.SUB_MENU_DIRECTION_BUTTON.getMap(type).getStringList("Details"))
					.glow(ConfigSettings.SUB_MENU_DIRECTION_BUTTON.getMap(type).getBoolean("Glowing")));
		return temp;
	}

	public static void showTo(Menu parent, Player player, List<String> category, int page) {
		setSound(null);
		new CategoryMenu(parent, player, category, page).displayTo(player);
	}
}
