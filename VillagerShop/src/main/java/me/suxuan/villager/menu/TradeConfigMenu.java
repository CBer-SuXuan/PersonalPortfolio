package me.suxuan.villager.menu;

import me.suxuan.villager.VillagerShop;
import me.suxuan.villager.cache.TradeCache;
import me.suxuan.villager.prompt.NamePrompt;
import org.bukkit.conversations.Conversation;
import org.bukkit.conversations.ConversationFactory;
import org.bukkit.conversations.ExactMatchConversationCanceller;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.menu.Menu;
import org.mineacademy.fo.menu.button.Button;
import org.mineacademy.fo.menu.button.annotation.Position;
import org.mineacademy.fo.menu.model.ItemCreator;
import org.mineacademy.fo.remain.CompMaterial;

import java.util.Arrays;
import java.util.List;

public class TradeConfigMenu extends Menu {

	private final String trade;

	@Position(10)
	private final Button nameButton;
	@Position(13)
	private final Button itemButton;
	@Position(16)
	private final Button commandButton;

	public TradeConfigMenu(String trade_name) {
		VillagerShop.getEditing().add(trade_name);
		this.trade = trade_name;
		this.setSize(9 * 3);
		this.setTitle("&1Trade &6" + trade_name + " &1Config");
		this.nameButton = Button.makeSimple(ItemCreator.of(CompMaterial.PLAYER_HEAD, "&9&lChange trade display name")
						.skullUrl("http://textures.minecraft.net/texture/9d9cc58ad25a1ab16d36bb5d6d493c8f5898c2bf302b64e325921c41c35867")
						.lore("Current display name: " + TradeCache.from(trade_name).getDisplayName()),
				player -> {
					player.closeInventory();
					VillagerShop.getEditing().add(trade_name);
					Conversation conversation = new ConversationFactory(VillagerShop.getInstance())
							.addConversationAbandonedListener(event -> {
								VillagerShop.getEditing().remove(trade_name);
								if (event.gracefulExit()) {
									String name = (String) event.getContext().getSessionData(NamePrompt.Name.NAME);
									TradeCache.from(trade_name).setDisplayName(name);
									TradeConfigMenu.showTo(player, trade_name);
								} else {
									Common.tell(player, "&cChange trade name cancelled!");
									TradeConfigMenu.showTo(player, trade_name);
								}
							})
							.withConversationCanceller(new ExactMatchConversationCanceller("quit"))
							.withTimeout(300)
							.withLocalEcho(false)
							.withFirstPrompt(new NamePrompt())
							.buildConversation(player);
					player.beginConversation(conversation);
				});
		this.itemButton = Button.makeSimple(ItemCreator.of(CompMaterial.PLAYER_HEAD, "&6&lChange trade items")
						.skullUrl("http://textures.minecraft.net/texture/14e550979ebf9950bbf5715ed779626a3cec76ac910b4bddb5d7581facafef67"),
				player -> DealSelectMenu.showTo(player, trade_name));
		this.commandButton = Button.makeSimple(ItemCreator.of(CompMaterial.PLAYER_HEAD, "&e&lChange trade commands")
						.skullUrl("http://textures.minecraft.net/texture/6d0f4061bfb767a7f922a6ca7176f7a9b20709bd0512696beb15ea6fa98ca55c"),
				player -> CommandSelectMenu.showTo(player, trade_name));
	}

	@Override
	protected void onMenuClose(Player player, Inventory inventory) {
		VillagerShop.getEditing().remove(trade);
	}

	@Override
	public ItemStack getItemAt(int slot) {
		List<Integer> light_blue = Arrays.asList(0, 1, 2, 9, 11, 18, 19, 20);
		List<Integer> orange = Arrays.asList(3, 4, 5, 12, 14, 21, 22, 23);
		List<Integer> yellow = Arrays.asList(6, 7, 8, 15, 17, 24, 25, 26);
		if (light_blue.contains(slot))
			return ItemCreator.of(CompMaterial.LIGHT_BLUE_STAINED_GLASS_PANE).name("&bDisplay")
					.clearLore().make();
		if (orange.contains(slot)) return ItemCreator.of(CompMaterial.ORANGE_STAINED_GLASS_PANE).name("&bItems")
				.clearLore().make();
		if (yellow.contains(slot)) return ItemCreator.of(CompMaterial.YELLOW_STAINED_GLASS_PANE).name("&bCommands")
				.clearLore().make();
		return NO_ITEM;
	}

	public static void showTo(Player player, String trade) {
		setSound(null);
		new TradeConfigMenu(trade).displayTo(player);
	}

}
