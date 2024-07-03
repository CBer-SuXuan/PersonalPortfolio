package me.suxuan.wardrobe.menus.menu_settings;

import me.suxuan.wardrobe.settings.ButtonsSetting;
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
import java.util.Arrays;
import java.util.List;

// This menu is used to set buttons.
public class ButtonSetMenu extends Menu {

	private final Button borderSetButton;
	private boolean borderSet = false;

	public ButtonSetMenu(Player player) {
		this.setTitle("&cDrop in right place!");
		this.setViewer(player);
		this.setSize(9 * 3);
		this.borderSetButton = new Button() {
			@Override
			public void onClickedInMenu(Player player, Menu menu, ClickType click) {
				borderSet = true;
				BorderSetMenu.showTo(player);
			}

			@Override
			public ItemStack getItem() {
				return ItemCreator.of(CompMaterial.REDSTONE,
						"&5Border Setting",
						"",
						"&7Click to set border buttons"
				).make();
			}
		};
	}

	@Override
	public ItemStack getItemAt(int slot) {
		List<Integer> boarder = Arrays.asList(0, 8, 9, 17, 18, 26);
		if (boarder.contains(slot)) return ItemCreator.of(CompMaterial.GRAY_STAINED_GLASS_PANE, " ").make();
		if (slot == 1)
			return ItemCreator.of(CompMaterial.PLAYER_HEAD, "&5Border Setting").skullUrl("https://textures.minecraft.net/texture/1cb8be16d40c25ace64e09f6086d408ebc3d545cfb2990c5b6c25dabcedeacc").make();
		if (slot == 2)
			return ItemCreator.of(CompMaterial.PLAYER_HEAD, "&bLast Page &7Button").skullUrl("https://textures.minecraft.net/texture/1cb8be16d40c25ace64e09f6086d408ebc3d545cfb2990c5b6c25dabcedeacc").make();
		if (slot == 3)
			return ItemCreator.of(CompMaterial.PLAYER_HEAD, "&aNext Page &7Button").skullUrl("https://textures.minecraft.net/texture/1cb8be16d40c25ace64e09f6086d408ebc3d545cfb2990c5b6c25dabcedeacc").make();
		if (slot == 4)
			return ItemCreator.of(CompMaterial.PLAYER_HEAD, "&9Lock &7Button").skullUrl("https://textures.minecraft.net/texture/1cb8be16d40c25ace64e09f6086d408ebc3d545cfb2990c5b6c25dabcedeacc").make();
		if (slot == 5)
			return ItemCreator.of(CompMaterial.PLAYER_HEAD, "&6Not Select &7Button").skullUrl("https://textures.minecraft.net/texture/1cb8be16d40c25ace64e09f6086d408ebc3d545cfb2990c5b6c25dabcedeacc").make();
		if (slot == 6)
			return ItemCreator.of(CompMaterial.PLAYER_HEAD, "&dSelected &7Button").skullUrl("https://textures.minecraft.net/texture/1cb8be16d40c25ace64e09f6086d408ebc3d545cfb2990c5b6c25dabcedeacc").make();
		if (slot == 7)
			return ItemCreator.of(CompMaterial.PLAYER_HEAD, "&3Empty Kit &7Button",
					"",
					"Will replace empty slot when open wardrobe.",
					"Put nothing here will disable this feature.").skullUrl("https://textures.minecraft.net/texture/1cb8be16d40c25ace64e09f6086d408ebc3d545cfb2990c5b6c25dabcedeacc").make();
		if (slot == 10)
			return borderSetButton.getItem();
		if (slot == 19)
			return ItemCreator.of(CompMaterial.PLAYER_HEAD, "&5Border Setting").skullUrl("https://textures.minecraft.net/texture/45c588b9ec0a08a37e01a809ed0903cc34c3e3f176dc92230417da93b948f148").make();
		if (slot == 20)
			return ItemCreator.of(CompMaterial.PLAYER_HEAD, "&bLast Page &7Button").skullUrl("https://textures.minecraft.net/texture/45c588b9ec0a08a37e01a809ed0903cc34c3e3f176dc92230417da93b948f148").make();
		if (slot == 21)
			return ItemCreator.of(CompMaterial.PLAYER_HEAD, "&aNext Page &7Button").skullUrl("https://textures.minecraft.net/texture/45c588b9ec0a08a37e01a809ed0903cc34c3e3f176dc92230417da93b948f148").make();
		if (slot == 22)
			return ItemCreator.of(CompMaterial.PLAYER_HEAD, "&9Lock &7Button").skullUrl("https://textures.minecraft.net/texture/45c588b9ec0a08a37e01a809ed0903cc34c3e3f176dc92230417da93b948f148").make();
		if (slot == 23)
			return ItemCreator.of(CompMaterial.PLAYER_HEAD, "&6Not Select &7Button").skullUrl("https://textures.minecraft.net/texture/45c588b9ec0a08a37e01a809ed0903cc34c3e3f176dc92230417da93b948f148").make();
		if (slot == 24)
			return ItemCreator.of(CompMaterial.PLAYER_HEAD, "&dSelected &7Button").skullUrl("https://textures.minecraft.net/texture/45c588b9ec0a08a37e01a809ed0903cc34c3e3f176dc92230417da93b948f148").make();
		if (slot == 25)
			return ItemCreator.of(CompMaterial.PLAYER_HEAD, "&3Empty Kit &7Button",
					"",
					"Will replace empty slot when open wardrobe.",
					"Put nothing here will disable this feature.").skullUrl("https://textures.minecraft.net/texture/45c588b9ec0a08a37e01a809ed0903cc34c3e3f176dc92230417da93b948f148").make();

		File file = FileUtil.getFile("menu/buttons.yml");
		if (file.exists()) {
			List<ItemStack> setting = ButtonsSetting.getInstance().getButtons();
			return setting.get(slot - 11);
		} else {
			return NO_ITEM;
		}
	}

	@Override
	protected boolean addInfoButton() {
		return false;
	}

	@Override
	protected boolean isActionAllowed(MenuClickLocation location, int slot, @Nullable ItemStack clicked,
									  @Nullable ItemStack cursor) {
		if (location == MenuClickLocation.PLAYER_INVENTORY) return true;
		return slot >= 11 && slot <= 16;
	}

	@Override
	protected void onMenuClose(Player player, Inventory inventory) {
		final StrictMap<Integer, ItemStack> items = new StrictMap<>();

		for (int slot = 11; slot <= 16; slot++) {
			final ItemStack item = inventory.getItem(slot);
			items.put(slot, item);
		}
		ButtonsSetting.getInstance().setButtons(new ArrayList<>(items.values()));
		if (!borderSet) SettingsMenu.showTo(getViewer());
	}

	public static void showTo(Player player) {
		setSound(null);
		new ButtonSetMenu(player).displayTo(player);
	}
}
