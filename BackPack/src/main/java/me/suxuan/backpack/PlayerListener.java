package me.suxuan.backpack;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerListener implements Listener {

	@EventHandler
	public void onPlayerJoinEvent(PlayerJoinEvent event) {
		PlayerCache.from(event.getPlayer());
	}

	@EventHandler
	public void onPlayerQuitEvent(PlayerQuitEvent event) {
		PlayerCache.from(event.getPlayer()).removeFromMemory();
	}

	@EventHandler
	public void onPlayerInteractEvent(PlayerInteractEvent event) {
		if (!(event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK)))
			return;
		for (ItemStack item : Settings.ITEM_LIST) {
			if (event.getPlayer().getInventory().getItemInMainHand().equals(item)) {
				int index = Settings.ITEM_LIST.indexOf(item);
				PackMenu.showTo(event.getPlayer(), Settings.SIZE_LIST.get(index), Settings.COMMAND_LIST.get(index)
						, Settings.TITLE_LIST.get(index));
			}
		}
	}

}
