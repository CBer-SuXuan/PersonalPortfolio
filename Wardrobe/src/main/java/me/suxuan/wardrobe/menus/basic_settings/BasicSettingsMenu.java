package me.suxuan.wardrobe.menus.basic_settings;

import me.suxuan.wardrobe.Main;
import me.suxuan.wardrobe.menus.MainSettingsMenu;
import me.suxuan.wardrobe.prompt.KitSetMenuPrompt;
import me.suxuan.wardrobe.prompt.WardrobeMenuPrompt;
import me.suxuan.wardrobe.settings.MenuSetting;
import org.bukkit.conversations.Conversable;
import org.bukkit.conversations.Conversation;
import org.bukkit.conversations.ConversationFactory;
import org.bukkit.conversations.ExactMatchConversationCanceller;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.mineacademy.fo.menu.Menu;
import org.mineacademy.fo.menu.button.Button;
import org.mineacademy.fo.menu.model.ItemCreator;
import org.mineacademy.fo.remain.CompMaterial;

import java.util.Arrays;

// This is menu is the menu setting's main menu.
public class BasicSettingsMenu extends Menu {

	private final Button wardrobeSet;
	private final Button kitSet;
	private final Button slotPermission;
	private final Button backButton;

	public BasicSettingsMenu(Player player) {
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
		this.wardrobeSet = new Button() {
			@Override
			public void onClickedInMenu(Player player, Menu menu, ClickType click) {
				if (click == ClickType.RIGHT) {
					MenuSetting.getInstance().setWardrobeTitle("&9My Wardrobe ({current}/{total})");
					MenuSetting.getInstance().reload();
					BasicSettingsMenu.showTo(player);
				} else {
					player.closeInventory();
					Conversation conversation = new ConversationFactory(Main.getInstance())
							.addConversationAbandonedListener(event -> {
								Conversable conversable = event.getContext().getForWhom();
								if (event.gracefulExit()) {
									String name = (String) event.getContext().getSessionData(WardrobeMenuPrompt.Result.TITLE);
									MenuSetting.getInstance().setWardrobeTitle(name);
									MenuSetting.getInstance().reload();
									BasicSettingsMenu.showTo(player);
								} else {
									BasicSettingsMenu.showTo(player);
								}
							})
							.withConversationCanceller(new ExactMatchConversationCanceller("exit"))
							.withTimeout(120)
							.withLocalEcho(false)
							.withFirstPrompt(new WardrobeMenuPrompt())
							.buildConversation(player);
					player.beginConversation(conversation);
				}
			}

			@Override
			public ItemStack getItem() {
				return ItemCreator.of(CompMaterial.CHEST,
						"&7Click to edit &aWardrobe Title",
						"",
						"&aWardrobe Title &7is the title of menu",
						"&7when a player open his wardrobe.",
						"",
						"&fCurrent: " + MenuSetting.getInstance().getWardrobeTitle() + " &7(Right click to reset)").make();
			}
		};
		this.kitSet = new Button() {
			@Override
			public void onClickedInMenu(Player player, Menu menu, ClickType click) {
				if (click == ClickType.RIGHT) {
					MenuSetting.getInstance().setKitSetTitle("&9Set kit");
					MenuSetting.getInstance().reload();
					BasicSettingsMenu.showTo(player);
				} else {
					player.closeInventory();
					Conversation conversation = new ConversationFactory(Main.getInstance())
							.addConversationAbandonedListener(event -> {
								Conversable conversable = event.getContext().getForWhom();
								if (event.gracefulExit()) {
									String name = (String) event.getContext().getSessionData(KitSetMenuPrompt.Result.TITLE);
									MenuSetting.getInstance().setKitSetTitle(name);
									MenuSetting.getInstance().reload();
									BasicSettingsMenu.showTo(player);
								} else {
									BasicSettingsMenu.showTo(player);
								}
							})
							.withConversationCanceller(new ExactMatchConversationCanceller("exit"))
							.withTimeout(120)
							.withLocalEcho(false)
							.withFirstPrompt(new KitSetMenuPrompt())
							.buildConversation(player);
					player.beginConversation(conversation);
				}
			}

			@Override
			public ItemStack getItem() {
				return ItemCreator.of(CompMaterial.ARMOR_STAND,
						"&7Click to edit &9Kit Set Title",
						"",
						"&9Kit Set Title &7is the title of menu",
						"&7when a player open kit edit menu.",
						"",
						"&fCurrent: " + MenuSetting.getInstance().getKitSetTitle() + " &7(Right click to reset)").make();
			}
		};

		this.slotPermission = new Button() {
			final MenuSetting menuSetting = MenuSetting.getInstance();

			@Override
			public void onClickedInMenu(Player player, Menu menu, ClickType click) {
				menuSetting.setSlotPermission(!menuSetting.getSlotPermission());
				MenuSetting.getInstance().reload();
				BasicSettingsMenu.showTo(player);
			}

			@Override
			public ItemStack getItem() {
				return ItemCreator.of(CompMaterial.REDSTONE,
						"&7Click to " + (menuSetting.getSlotPermission() ? "&cdisable" : "&aenable") + " &eSlot Permission",
						"",
						"&7If &eSlot Permission &7set enable,",
						"player will only get the slot by their permissions.",
						"&7Such as if player have permission wardrobe.slot.3,",
						"he can only unlock slot 3.",
						"",
						"&fCurrent: " + (menuSetting.getSlotPermission() ? "&aenable" : "&cdisable")).make();
			}
		};
	}

	@Override
	public ItemStack getItemAt(int slot) {
		if (Arrays.asList(0, 1, 2, 9, 11, 18, 19, 20).contains(slot))
			return ItemCreator.of(CompMaterial.GREEN_STAINED_GLASS_PANE, "&aWardrobe Title").make();
		if (Arrays.asList(3, 4, 5, 12, 14, 21, 22, 23).contains(slot))
			return ItemCreator.of(CompMaterial.BLUE_STAINED_GLASS_PANE, "&9Kit Set Title").make();
		if (Arrays.asList(6, 7, 8, 15, 17, 24, 25, 26).contains(slot))
			return ItemCreator.of(CompMaterial.YELLOW_STAINED_GLASS_PANE, "&eSlot Permission").make();
		if (Arrays.asList(27, 28, 29, 30, 32, 33, 34, 35).contains(slot))
			return ItemCreator.of(CompMaterial.GRAY_STAINED_GLASS_PANE, " ").make();
		if (slot == 10) return wardrobeSet.getItem();
		if (slot == 13) return kitSet.getItem();
		if (slot == 16) return slotPermission.getItem();
		if (slot == 31) return backButton.getItem();
		return NO_ITEM;
	}

	@Override
	protected boolean addInfoButton() {
		return false;
	}

	public static void showTo(Player player) {
		setSound(null);
		new BasicSettingsMenu(player).displayTo(player);
	}
}
