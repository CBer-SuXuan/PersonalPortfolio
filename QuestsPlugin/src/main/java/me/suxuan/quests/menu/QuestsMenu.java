package me.suxuan.quests.menu;

import me.arcaniax.hdb.api.HeadDatabaseAPI;
import me.suxuan.quests.QuestsPlugin;
import me.suxuan.quests.cache.ConfigSettings;
import me.suxuan.quests.cache.PlayerCache;
import me.suxuan.quests.cache.QuestsCache;
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

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

// This is old class. Not use anymore.
public class QuestsMenu extends Menu {

	private final List<Button> questsButton = new ArrayList<>();
	private Button surroundButton = Button.makeEmpty();
	File[] quests = new File(QuestsPlugin.getInstance().getDataFolder(), "quests").listFiles();

	QuestsMenu(Player player) {

		this.setTitle(ConfigSettings.MAIN_MENU_TITLE);
		this.setSize(9 * ConfigSettings.MAIN_MENU_SIZE);

		if (quests == null) return;

		SerializedMap surround_button = ConfigSettings.MAIN_MENU_SURROUND;
		if (surround_button.getBoolean("Use"))
			this.surroundButton = Button.makeDummy(ItemCreator.of(CompMaterial.fromString(surround_button.getString("Icon")))
					.name(surround_button.getString("Name"))
					.lore(surround_button.getStringList("Details")));
		else this.surroundButton = Button.makeEmpty();

		HeadDatabaseAPI api = new HeadDatabaseAPI();

		for (int i = 0; i < QuestsCache.cacheMap.size(); i++) {

			if (quests[i].getName().equals("template.yml")) continue;
			QuestsCache cache = QuestsCache.from(quests[i].getName().replace(".yml", ""));
			if (PlayerCache.from(player).getDone_quests().contains(quests[i].getName().replace(".yml", ""))) {
				this.questsButton.add(Button.makeDummy(ItemCreator
						.of(checkIfAllNumber(cache.getFinishedQuest().getString("Icon")) ?
								api.getItemHead(cache.getFinishedQuest().getString("Icon")) :
								CompMaterial.fromString(cache.getFinishedQuest().getString("Icon")).toItem())
						.name(cache.getFinishedQuest().getString("Name"))
						.lore(cache.getFinishedQuest().getStringList("Details"))));
			} else if (PlayerCache.from(player).getDoing_quest().equals(quests[i].getName().replace(".yml", "")))
				this.questsButton.add(Button.makeDummy(ItemCreator
						.of(checkIfAllNumber(cache.getAcceptedQuest().getString("Icon")) ?
								api.getItemHead(cache.getAcceptedQuest().getString("Icon")) :
								CompMaterial.fromString(cache.getAcceptedQuest().getString("Icon")).toItem())
						.name(cache.getAcceptedQuest().getString("Name"))
						.lore(Variables.replace(cache.getAcceptedQuest().getStringList("Details"), player, getReplaceMap(cache, player)))));
			else
				this.questsButton.add(Button.makeDummy(ItemCreator
						.of(checkIfAllNumber(cache.getUnacceptedQuest().getString("Icon")) ?
								api.getItemHead(cache.getUnacceptedQuest().getString("Icon")) :
								CompMaterial.fromString(cache.getUnacceptedQuest().getString("Icon")).toItem())
						.name(cache.getUnacceptedQuest().getString("Name"))
						.lore(cache.getUnacceptedQuest().getStringList("Details"))));
		}

	}

	@Override
	public ItemStack getItemAt(int slot) {

		int row = ConfigSettings.MAIN_MENU_SIZE;
		for (int i = 0; i < QuestsCache.cacheMap.size(); i++) {
			if (quests[i].getName().equals("template.yml")) continue;
			QuestsCache cache = QuestsCache.from(quests[i].getName().replace(".yml", ""));
			if (slot == (Integer.parseInt(cache.getPosition().split(";")[0]) - 1) * 9
					+ Integer.parseInt(cache.getPosition().split(";")[1]) - 1)
				return this.questsButton.get(i).getItem();
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
			QuestsMenu.showTo(player);
			return;
		}
		if (getFileFromAcceptedQuest(slot) != null && click.isRightClick()) {
			CancelMenu.showTo(player, getFileFromAcceptedQuest(slot));
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
		for (int i = 0; i < QuestsCache.cacheMap.size(); i++) {
			if (quests[i].getName().equals("template.yml")) continue;
			QuestsCache cache = QuestsCache.from(quests[i].getName().replace(".yml", ""));
			if (cache.getUnacceptedQuest().getString("Name").replaceAll("&{1}[a-z0-9]{1}", "").equals(display_name))
				return cache.getQuest_file_name();
		}
		return null;
	}

	private String getFileFromAcceptedQuest(int slot) {
		ItemMeta meta = getItemAt(slot).getItemMeta();
		if (meta == null) return null;
		String display_name = meta.getDisplayName().replaceAll("§{1}[a-z0-9]{1}", "");
		for (int i = 0; i < QuestsCache.cacheMap.size(); i++) {
			if (quests[i].getName().equals("template.yml")) continue;
			QuestsCache cache = QuestsCache.from(quests[i].getName().replace(".yml", ""));
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

	private Boolean checkIfAllNumber(String check) {
		if (check == null) return false;
		for (char c : check.toCharArray()) {
			if (!Character.isDigit(c)) return false;
		}
		return true;
	}

	public static void showTo(Player player) {
		setSound(null);
		new QuestsMenu(player).displayTo(player);
	}
}
