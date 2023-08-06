package me.suxuan.quests.menu;

import me.suxuan.quests.cache.ConfigSettings;
import me.suxuan.quests.utils.QuestsUtil;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.mineacademy.fo.collection.SerializedMap;
import org.mineacademy.fo.menu.Menu;
import org.mineacademy.fo.menu.button.Button;
import org.mineacademy.fo.menu.model.ItemCreator;
import org.mineacademy.fo.remain.CompMaterial;

import java.util.List;

public class CategoryCancelMenu extends Menu {

	private final Button yesButton;
	private final Button noButton;
	private List<String> category;

	public CategoryCancelMenu(Menu parent, String quest_name, List<String> category) {

		super(parent);
		this.setSize(ConfigSettings.CANCEL_MENU_SIZE * 9);
		this.setTitle(ConfigSettings.CANCEL_MENU_TITLE.replace("{quest_name}", quest_name));

		SerializedMap yes_map = ConfigSettings.CANCEL_MENU_YES;
		SerializedMap no_map = ConfigSettings.CANCEL_MENU_NO;
		this.category = category;

		this.yesButton = Button.makeSimple(ItemCreator.of(CompMaterial.fromString(yes_map.getString("Icon")))
				.name(yes_map.getString("Name")).lore(yes_map.getStringList("Details")), player -> {

			player.closeInventory();
			QuestsUtil.cancelQuest(quest_name, player);
			CategoryMenu.showTo(getParent(), player, this.category, 1);

		});

		this.noButton = Button.makeSimple(ItemCreator.of(CompMaterial.fromString(no_map.getString("Icon")))
				.name(no_map.getString("Name")).lore(no_map.getStringList("Details")), player ->
				CategoryMenu.showTo(getParent(), player, this.category, 1));

	}

	@Override
	public ItemStack getItemAt(int slot) {

		SerializedMap yes_map = ConfigSettings.CANCEL_MENU_YES;
		SerializedMap no_map = ConfigSettings.CANCEL_MENU_NO;
		if (slot == (Integer.parseInt(yes_map.getString("Position").split(";")[0]) - 1) * 9
				+ Integer.parseInt(yes_map.getString("Position").split(";")[1]) - 1)
			return this.yesButton.getItem();
		if (slot == (Integer.parseInt(no_map.getString("Position").split(";")[0]) - 1) * 9
				+ Integer.parseInt(no_map.getString("Position").split(";")[1]) - 1)
			return this.noButton.getItem();
		return NO_ITEM;

	}

	@Override
	protected boolean addReturnButton() {
		return false;
	}

	@Override
	protected boolean addInfoButton() {
		return false;
	}

	public static void showTo(Menu parent, Player player, String quest, List<String> category) {
		setSound(null);
		new CategoryCancelMenu(parent, quest, category).displayTo(player);
	}

}
