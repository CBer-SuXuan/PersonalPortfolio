package me.suxuan.quests.listener;

import me.suxuan.quests.cache.PlayerCache;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListener implements Listener {

	@EventHandler
	public void onPlayerJoinEvent(PlayerJoinEvent event) {
		PlayerCache.from(event.getPlayer());
	}

	@EventHandler
	public void onPlayerQuitEvent(PlayerQuitEvent event) {
		PlayerCache.from(event.getPlayer()).removeFromMemory();
	}

}
