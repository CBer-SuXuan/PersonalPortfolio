package me.suxuan.wardrobe.menus;

import me.suxuan.wardrobe.menus.basic_settings.BasicSettingsMenu;
import me.suxuan.wardrobe.menus.menu_settings.SettingsMenu;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.mineacademy.fo.menu.Menu;
import org.mineacademy.fo.menu.button.Button;
import org.mineacademy.fo.menu.button.annotation.Position;
import org.mineacademy.fo.menu.model.ItemCreator;
import org.mineacademy.fo.remain.CompMaterial;

import java.util.Arrays;

// The main menu of this plugin.
public class MainSettingsMenu extends Menu {

	@Position(13)
	private final Button menuSettingsButton;
	@Position(10)
	private final Button basicSettingButton;

	@Position(16)
	private final Button usefulToolButton;

	public MainSettingsMenu(Player player) {
		this.setTitle("&bWardrobe Settings");
		this.setViewer(player);
		this.setSize(9 * 3);
		this.menuSettingsButton = new Button() {
			@Override
			public void onClickedInMenu(Player player, Menu menu, ClickType click) {
				SettingsMenu.showTo(getViewer());
			}

			@Override
			public ItemStack getItem() {
				return ItemCreator.of(CompMaterial.CHEST,
						"&9Menu Settings",
						"",
						"&6Click to go to &9Menu Settings",
						"&7Where you can custom all menus and buttons.").make();
			}
		};
		this.basicSettingButton = new Button() {
			@Override
			public void onClickedInMenu(Player player, Menu menu, ClickType click) {
				BasicSettingsMenu.showTo(getViewer());
			}

			@Override
			public ItemStack getItem() {
				return ItemCreator.of(CompMaterial.COMMAND_BLOCK,
						"&aBasic Settings",
						"",
						"&6Click to go to &aBasic Settings",
						"&7Where you can set title of every menus.").make();
			}
		};
		this.usefulToolButton = Button.makeDummy(ItemCreator.of(CompMaterial.BARRIER, "&6Coming soon"));
	}

	@Override
	public ItemStack getItemAt(int slot) {
		if (Arrays.asList(0, 1, 2, 9, 11, 18, 19, 20).contains(slot))
			return ItemCreator.of(CompMaterial.GREEN_STAINED_GLASS_PANE, "&aBasic Settings").make();
		if (Arrays.asList(3, 4, 5, 12, 14, 21, 22, 23).contains(slot))
			return ItemCreator.of(CompMaterial.BLUE_STAINED_GLASS_PANE, "&9Menu Settings").make();
		if (Arrays.asList(6, 7, 8, 15, 17, 24, 25, 26).contains(slot))
			return ItemCreator.of(CompMaterial.YELLOW_STAINED_GLASS_PANE, "&eUseful Tools").make();
		return NO_ITEM;
	}

	@Override
	protected boolean addInfoButton() {
		return false;
	}

	public static void showTo(Player player) {
		setSound(null);
		new MainSettingsMenu(player).displayTo(player);
	}
}
