package me.suxuan.villager.menu;

import me.suxuan.villager.VillagerShop;
import me.suxuan.villager.cache.TradeCache;
import me.suxuan.villager.prompt.CommandAddPrompt;
import me.suxuan.villager.prompt.CommandRemovePrompt;
import org.bukkit.Material;
import org.bukkit.conversations.Conversation;
import org.bukkit.conversations.ConversationFactory;
import org.bukkit.conversations.ExactMatchConversationCanceller;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.menu.Menu;
import org.mineacademy.fo.menu.button.Button;
import org.mineacademy.fo.menu.model.ItemCreator;
import org.mineacademy.fo.remain.CompMaterial;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CommandSelectMenu extends Menu {
	// Trade name.
	private final String trade;
	// Surrounding button, only for beautiful.
	private final Button surroundButton;
	// Return button, return to the TradeSetMenu.
	private final Button returnButton;
	// All deals buttons.
	private final List<Button> itemsButtons = new ArrayList<>();

	private int number = -1;

	public CommandSelectMenu(String trade) {
		VillagerShop.getEditing().add(trade);
		this.trade = trade;
		this.setSize(9 * 6);
		this.setTitle("&1Select a deal");
		this.surroundButton = Button.makeDummy(ItemCreator.of(CompMaterial.GRAY_STAINED_GLASS_PANE)
				.name("&7Surrounding Line"));
		if (TradeCache.from(trade).getDeals().isEmpty()) {
			Button button = Button.makeDummy(ItemCreator.of(CompMaterial.RED_CONCRETE)
					.name("&c&lThis trade don't have any deals")
					.lore("&b&lCreate a deal first!"));
			this.itemsButtons.add(button);
		} else {
			for (int i = 0; i < TradeCache.from(trade).getDealsAmount(); i++) {
				TradeCache cache = TradeCache.from(trade);
				List<String> lore = new ArrayList<>();
				for (int j = 0; j < cache.getDeals().getMap(String.valueOf(i)).getStringList("command").size(); j++) {
					lore.add((j + 1) + ". " + cache.getDeals().getMap(String.valueOf(i)).getStringList("command").get(j));
				}
				if (!lore.isEmpty())
					this.itemsButtons.add(Button.makeDummy(ItemCreator.of(CompMaterial.LIME_CONCRETE)
							.name("&6&lDeal " + (i + 1))
							.lore("&d&lLeft &f&lclick to &d&ladd&f&l a command", "&b&lRight &f&lclick to &b&ldelete&f&l a command"
									, "Current Commands:\n")
							.lore(lore)));
				else
					this.itemsButtons.add(Button.makeDummy(ItemCreator.of(CompMaterial.LIME_CONCRETE)
							.name("&6&lDeal " + (i + 1))
							.lore("&d&lLeft &f&lclick to &d&ladd&f&l a command", "&b&lRight &f&lclick to &b&ldelete&f&l a command"
									, "Current no commands.")));
			}
		}
		this.returnButton = Button.makeSimple(ItemCreator.of(CompMaterial.PLAYER_HEAD)
						.skullUrl("http://textures.minecraft.net/texture/86e145e71295bcc0488e9bb7e6d6895b7f969a3b5bb7eb34a52e932bc84df5b")
						.name("&a&lReturn Back"),
				player -> TradeConfigMenu.showTo(player, trade));
	}

	@Override
	public ItemStack getItemAt(int slot) {
		if (slot == 49)
			return returnButton.getItem();
		List<Integer> surround = Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 17, 18, 26, 27, 35, 36, 44, 45, 46, 47, 48, 50, 51, 52, 53);
		if (surround.contains(slot)) return surroundButton.getItem();
		number++;
		if (number >= this.itemsButtons.size()) return NO_ITEM;
		return this.itemsButtons.get(number).getItem();
	}

	@Override
	protected void onMenuClick(Player player, int slot, InventoryAction action, ClickType click, ItemStack cursor, ItemStack clicked, boolean cancelled) {
		if (clicked.getType().equals(Material.LIME_CONCRETE) && click.isLeftClick()) {
			int number = Integer.parseInt(clicked.getItemMeta().getDisplayName().replace("§6§lDeal ", "")) - 1;
			player.closeInventory();
			VillagerShop.getEditing().add(trade);
			Conversation conversation = new ConversationFactory(VillagerShop.getInstance())
					.addConversationAbandonedListener(event -> {
						VillagerShop.getEditing().remove(trade);
						if (event.gracefulExit()) {
							String command = (String) event.getContext().getSessionData(CommandAddPrompt.Commands.COMMAND);
							TradeCache.from(trade).addCommand(command, number);
							CommandSelectMenu.showTo(player, trade);
						} else {
							Common.tell(player, "&cAdd command cancelled!");
							CommandSelectMenu.showTo(player, trade);
						}
					})
					.withConversationCanceller(new ExactMatchConversationCanceller("quit"))
					.withTimeout(300)
					.withLocalEcho(false)
					.withFirstPrompt(new CommandAddPrompt())
					.buildConversation(player);
			player.beginConversation(conversation);
		}
		if (clicked.getType().equals(Material.LIME_CONCRETE) && click.isRightClick()) {
			int number = Integer.parseInt(clicked.getItemMeta().getDisplayName().replace("§6§lDeal ", "")) - 1;
			int all = TradeCache.from(trade).getDeals().getMap(String.valueOf(number)).getStringList("command").size();
			player.closeInventory();
			VillagerShop.getEditing().add(trade);
			TradeCache cache = TradeCache.from(trade);
			List<String> lore = new ArrayList<>();
			for (int j = 0; j < cache.getDeals().getMap(String.valueOf(number)).getStringList("command").size(); j++) {
				lore.add("&6&l" + (j + 1) + "&f. " + cache.getDeals().getMap(String.valueOf(number)).getStringList("command").get(j));
			}
			Conversation conversation = new ConversationFactory(VillagerShop.getInstance())
					.addConversationAbandonedListener(event -> {
						VillagerShop.getEditing().remove(trade);
						if (event.gracefulExit()) {
							int index = (int) event.getContext().getSessionData(CommandRemovePrompt.Delete.COMMAND);
							TradeCache.from(trade).deleteCommand(index - 1, number);
							CommandSelectMenu.showTo(player, trade);
						} else {
							Common.tell(player, "&cDelete command cancelled!");
							CommandSelectMenu.showTo(player, trade);
						}
					})
					.withConversationCanceller(new ExactMatchConversationCanceller("quit"))
					.withTimeout(300)
					.withLocalEcho(false)
					.withFirstPrompt(new CommandRemovePrompt(all, lore))
					.buildConversation(player);
			player.beginConversation(conversation);
		}
	}

	@Override
	protected void onMenuClose(Player player, Inventory inventory) {
		VillagerShop.getEditing().remove(trade);
	}

	public static void showTo(Player player, String trade) {
		setSound(null);
		new CommandSelectMenu(trade).displayTo(player);
	}
}