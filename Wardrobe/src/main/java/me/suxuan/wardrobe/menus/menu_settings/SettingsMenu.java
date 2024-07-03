package me.suxuan.wardrobe.menus.menu_settings;

import me.suxuan.wardrobe.menus.MainSettingsMenu;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.mineacademy.fo.menu.Menu;
import org.mineacademy.fo.menu.button.Button;
import org.mineacademy.fo.menu.model.ItemCreator;
import org.mineacademy.fo.remain.CompMaterial;

import java.util.Arrays;

// This is menu is the menu setting's main menu.
public class SettingsMenu extends Menu {

	private final Button emptyKitMenu;
	private final Button buttonSetMenu;
	private final Button kitEditMenu;
	private final Button backButton;

	public SettingsMenu(Player player) {
		this.setTitle("&9Menu Settings");
		this.setViewer(player);
		this.setSize(9 * 4);
		this.backButton = new Button() {
			@Override
			public void onClickedInMenu(Player player, Menu menu, ClickType click) {
				MainSettingsMenu.showTo(getViewer());
			}

			@Override
			public ItemStack getItem() {
				return ItemCreator.of(CompMaterial.BARRIER,
						"&cBack",
						"",
						"&fBack to the &bWardrobe Settings &fmenu.").make();
			}
		};
		this.emptyKitMenu = new Button() {
			@Override
			public void onClickedInMenu(Player player, Menu menu, ClickType click) {
				EmptySetMenu.showTo(getViewer());
			}

			@Override
			public ItemStack getItem() {
				return ItemCreator.of(CompMaterial.ARMOR_STAND,
						"&7Click to edit &aEmpty kit menu",
						"",
						"&aEmpty kit menu &7is like a &7template.",
						"&7If one slot in wardrobe doesn't unlock,",
						"&7it will replace the corresponding empty position.",
						"",
						"&8Before click this button,",
						"&8recommend to prepare the items",
						"&8that you want to drop in.").make();
			}
		};
		this.buttonSetMenu = new Button() {
			@Override
			public void onClickedInMenu(Player player, Menu menu, ClickType click) {
				ButtonSetMenu.showTo(getViewer());
			}

			@Override
			public ItemStack getItem() {
				return ItemCreator.of(CompMaterial.STONE_BUTTON,
						"&7Click to edit &9Buttons",
						"",
						"&7You might use some &9Buttons &7in the wardrobe.",
						"&7Drop the item that you make in the specified slot,",
						"&7and it will add to the wardrobe automatically.",
						"",
						"&fLeft click &6Not Select Button &fwill make player select the kit.",
						"&fRight click &6Not Select Button &fwill open kit edit menu for player.",
						"",
						"&8Before click this button,",
						"&8recommend to prepare the items",
						"&8that you want to drop in the menu.").make();
			}
		};
		this.kitEditMenu = new Button() {
			@Override
			public void onClickedInMenu(Player player, Menu menu, ClickType click) {
				KitSetMenu.showTo(player);
			}

			@Override
			public ItemStack getItem() {
				return ItemCreator.of(CompMaterial.DIAMOND_CHESTPLATE,
						"&7Click to edit &eEdit kit menu",
						"",
						"&7In the &eKit edit menu&7, player can edit their kits.",
						"&7You need to put some item to make it beautiful.",
						"&7You can not edit diamond equipment in this menu!",
						"",
						"&8Before click this button,",
						"&8recommend to prepare the items",
						"&8that you want to drop in the menu.").make();
			}
		};
	}

	@Override
	public ItemStack getItemAt(int slot) {
		if (Arrays.asList(0, 1, 2, 9, 11, 18, 19, 20).contains(slot))
			return ItemCreator.of(CompMaterial.GREEN_STAINED_GLASS_PANE, "&aEmpty kit menu").make();
		if (Arrays.asList(3, 4, 5, 12, 14, 21, 22, 23).contains(slot))
			return ItemCreator.of(CompMaterial.BLUE_STAINED_GLASS_PANE, "&9Buttons").make();
		if (Arrays.asList(6, 7, 8, 15, 17, 24, 25, 26).contains(slot))
			return ItemCreator.of(CompMaterial.YELLOW_STAINED_GLASS_PANE, "&eEdit kit menu").make();
		if (Arrays.asList(27, 28, 29, 30, 32, 33, 34, 35).contains(slot))
			return ItemCreator.of(CompMaterial.GRAY_STAINED_GLASS_PANE, " ").make();
		if (slot == 10) return emptyKitMenu.getItem();
		if (slot == 13) return buttonSetMenu.getItem();
		if (slot == 16) return kitEditMenu.getItem();
		if (slot == 31) return backButton.getItem();
		return NO_ITEM;
	}

	@Override
	protected boolean addInfoButton() {
		return false;
	}

	public static void showTo(Player player) {
		setSound(null);
		new SettingsMenu(player).displayTo(player);
	}
}
