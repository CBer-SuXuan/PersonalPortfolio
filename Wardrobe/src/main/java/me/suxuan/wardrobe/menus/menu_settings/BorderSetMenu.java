package me.suxuan.wardrobe.menus.menu_settings;

import me.suxuan.wardrobe.settings.BorderSetting;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.mineacademy.fo.FileUtil;
import org.mineacademy.fo.collection.StrictMap;
import org.mineacademy.fo.menu.Menu;
import org.mineacademy.fo.menu.button.Button;
import org.mineacademy.fo.menu.model.ItemCreator;
import org.mineacademy.fo.menu.model.MenuClickLocation;
import org.mineacademy.fo.remain.CompMaterial;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

// This is menu is the menu setting's main menu.
public class BorderSetMenu extends Menu {

	private final Button firstPageBorder;
	private final Button lastPageBorder;
	private final Button middlePageBorder;
	private final Button onePageBorder;
	private final Button backButton;

	public BorderSetMenu(Player player) {
		this.setTitle("&9Border Settings");
		this.setViewer(player);
		this.setSize(9 * 3);
		this.backButton = new Button() {
			@Override
			public void onClickedInMenu(Player player, Menu menu, ClickType click) {
				ButtonSetMenu.showTo(getViewer());
			}

			@Override
			public ItemStack getItem() {
				return ItemCreator.of(CompMaterial.BARRIER,
						"&cBack",
						"",
						"&fBack to the &9Menu Settings&f.").make();
			}
		};
		this.onePageBorder = new Button() {
			@Override
			public void onClickedInMenu(Player player, Menu menu, ClickType click) {
				OnePageBorderSet.showTo(getViewer());
			}

			@Override
			public ItemStack getItem() {
				return ItemCreator.of(CompMaterial.GREEN_DYE,
						"&7Click to edit &aOne page border",
						"",
						"&7Set border of one page.",
						"",
						"&8Before click this button,",
						"&8recommend to prepare the items",
						"&8that you want to drop in.").make();
			}
		};
		this.firstPageBorder = new Button() {
			@Override
			public void onClickedInMenu(Player player, Menu menu, ClickType click) {
				FirstBorderSet.showTo(getViewer());
			}

			@Override
			public ItemStack getItem() {
				return ItemCreator.of(CompMaterial.MAGENTA_DYE,
						"&7Click to edit &dFirst page border",
						"",
						"&7Set border of first page.",
						"",
						"&8Before click this button,",
						"&8recommend to prepare the items",
						"&8that you want to drop in.").make();
			}
		};
		this.middlePageBorder = new Button() {
			@Override
			public void onClickedInMenu(Player player, Menu menu, ClickType click) {
				MiddleBorderSet.showTo(getViewer());
			}

			@Override
			public ItemStack getItem() {
				return ItemCreator.of(CompMaterial.PURPLE_DYE,
						"&7Click to edit &5Middle page border",
						"",
						"&7Set border of middle page.",
						"",
						"&8Before click this button,",
						"&8recommend to prepare the items",
						"&8that you want to drop in the menu.").make();
			}
		};
		this.lastPageBorder = new Button() {
			@Override
			public void onClickedInMenu(Player player, Menu menu, ClickType click) {
				LastBorderSet.showTo(player);
			}

			@Override
			public ItemStack getItem() {
				return ItemCreator.of(CompMaterial.YELLOW_DYE,
						"&7Click to edit &eLast page border",
						"",
						"&7Set border of last page.",
						"",
						"&8Before click this button,",
						"&8recommend to prepare the items",
						"&8that you want to drop in the menu.").make();
			}
		};
	}

	@Override
	public ItemStack getItemAt(int slot) {
		if (slot == 10) return onePageBorder.getItem();
		if (slot == 12) return firstPageBorder.getItem();
		if (slot == 14) return middlePageBorder.getItem();
		if (slot == 16) return lastPageBorder.getItem();
		if (slot == 22) return backButton.getItem();
		return ItemCreator.of(CompMaterial.GRAY_STAINED_GLASS_PANE, "").make();
	}

	@Override
	protected boolean addInfoButton() {
		return false;
	}

	public static void showTo(Player player) {
		setSound(null);
		new BorderSetMenu(player).displayTo(player);
	}

	static class OnePageBorderSet extends Menu {

		public OnePageBorderSet(Player player) {
			this.setTitle("&cReplace with own items!");
			this.setViewer(player);
			this.setSize(9 * 6);
		}

		@Override
		public ItemStack getItemAt(int slot) {
			if (slot < 45)
				return ItemCreator.of(CompMaterial.BLUE_STAINED_GLASS_PANE,
						"You don't need to change me!").make();
			File file = FileUtil.getFile("menu/border.yml");
			if (file.exists()) {
				List<ItemStack> setting = BorderSetting.getInstance().getOne();
				if (setting.isEmpty()) return NO_ITEM;
				return setting.get(slot - 45);
			} else return NO_ITEM;
		}

		@Override
		protected boolean addInfoButton() {
			return false;
		}

		@Override
		protected boolean isActionAllowed(MenuClickLocation location, int slot, @Nullable ItemStack clicked,
										  @Nullable ItemStack cursor) {
			if (location == MenuClickLocation.PLAYER_INVENTORY) return true;
			return slot >= 45;
		}

		@Override
		protected void onMenuClose(Player player, Inventory inventory) {
			final StrictMap<Integer, ItemStack> items = new StrictMap<>();

			for (int slot = 45; slot <= 53; slot++) {
				final ItemStack item = inventory.getItem(slot);
				items.put(slot, item);
			}
			BorderSetting.getInstance().setOne(new ArrayList<>(items.values()));
			BorderSetMenu.showTo(getViewer());
		}

		public static void showTo(Player player) {
			setSound(null);
			new OnePageBorderSet(player).displayTo(player);
		}
	}

	static class FirstBorderSet extends Menu {

		public FirstBorderSet(Player player) {
			this.setTitle("&cReplace with own items!");
			this.setViewer(player);
			this.setSize(9 * 6);
		}

		@Override
		public ItemStack getItemAt(int slot) {
			if (slot < 45 || slot == 53)
				return ItemCreator.of(CompMaterial.BLUE_STAINED_GLASS_PANE,
						"You don't need to change me!").make();
			File file = FileUtil.getFile("menu/border.yml");
			if (file.exists()) {
				List<ItemStack> setting = BorderSetting.getInstance().getFirst();
				if (setting.isEmpty()) return NO_ITEM;
				return setting.get(slot - 45);
			} else return NO_ITEM;
		}

		@Override
		protected boolean addInfoButton() {
			return false;
		}

		@Override
		protected boolean isActionAllowed(MenuClickLocation location, int slot, @Nullable ItemStack clicked,
										  @Nullable ItemStack cursor) {
			if (location == MenuClickLocation.PLAYER_INVENTORY) return true;
			return slot >= 45 && slot <= 52;
		}

		@Override
		protected void onMenuClose(Player player, Inventory inventory) {
			final StrictMap<Integer, ItemStack> items = new StrictMap<>();

			for (int slot = 45; slot <= 52; slot++) {
				final ItemStack item = inventory.getItem(slot);
				items.put(slot, item);
			}
			BorderSetting.getInstance().setFirst(new ArrayList<>(items.values()));
			BorderSetMenu.showTo(getViewer());
		}

		public static void showTo(Player player) {
			setSound(null);
			new FirstBorderSet(player).displayTo(player);
		}
	}

	static class MiddleBorderSet extends Menu {

		public MiddleBorderSet(Player player) {
			this.setTitle("&cReplace with own items!");
			this.setViewer(player);
			this.setSize(9 * 6);
		}

		@Override
		public ItemStack getItemAt(int slot) {
			if (slot < 46 || slot == 53)
				return ItemCreator.of(CompMaterial.BLUE_STAINED_GLASS_PANE,
						"You don't need to change me!").make();
			File file = FileUtil.getFile("menu/border.yml");
			if (file.exists()) {
				List<ItemStack> setting = BorderSetting.getInstance().getMiddle();
				if (setting.isEmpty()) return NO_ITEM;
				return setting.get(slot - 46);
			} else return NO_ITEM;
		}

		@Override
		protected boolean addInfoButton() {
			return false;
		}

		@Override
		protected boolean isActionAllowed(MenuClickLocation location, int slot, @Nullable ItemStack clicked,
										  @Nullable ItemStack cursor) {
			if (location == MenuClickLocation.PLAYER_INVENTORY) return true;
			return (slot >= 46 && slot <= 52);
		}

		@Override
		protected void onMenuClose(Player player, Inventory inventory) {
			final StrictMap<Integer, ItemStack> items = new StrictMap<>();

			for (int slot = 46; slot <= 52; slot++) {
				final ItemStack item = inventory.getItem(slot);
				items.put(slot, item);
			}
			BorderSetting.getInstance().setMiddle(new ArrayList<>(items.values()));
			BorderSetMenu.showTo(getViewer());
		}

		public static void showTo(Player player) {
			setSound(null);
			new MiddleBorderSet(player).displayTo(player);
		}
	}

	static class LastBorderSet extends Menu {

		public LastBorderSet(Player player) {
			this.setTitle("&cReplace with own items!");
			this.setViewer(player);
			this.setSize(9 * 6);
		}

		@Override
		public ItemStack getItemAt(int slot) {
			if (slot < 46)
				return ItemCreator.of(CompMaterial.BLUE_STAINED_GLASS_PANE,
						"You don't need to change me!").make();
			File file = FileUtil.getFile("menu/border.yml");
			if (file.exists()) {
				List<ItemStack> setting = BorderSetting.getInstance().getLast();
				if (setting.isEmpty()) return NO_ITEM;
				return setting.get(slot - 46);
			} else return NO_ITEM;
		}

		@Override
		protected boolean addInfoButton() {
			return false;
		}

		@Override
		protected boolean isActionAllowed(MenuClickLocation location, int slot, @Nullable ItemStack clicked,
										  @Nullable ItemStack cursor) {
			if (location == MenuClickLocation.PLAYER_INVENTORY) return true;
			return slot >= 46;
		}

		@Override
		protected void onMenuClose(Player player, Inventory inventory) {
			final StrictMap<Integer, ItemStack> items = new StrictMap<>();

			for (int slot = 46; slot <= 53; slot++) {
				final ItemStack item = inventory.getItem(slot);
				items.put(slot, item);
			}
			BorderSetting.getInstance().setLast(new ArrayList<>(items.values()));
			BorderSetMenu.showTo(getViewer());
		}

		public static void showTo(Player player) {
			setSound(null);
			new LastBorderSet(player).displayTo(player);
		}
	}
}
