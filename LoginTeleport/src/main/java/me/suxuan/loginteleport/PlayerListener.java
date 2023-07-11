package me.suxuan.loginteleport;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerListener implements Listener {

	@EventHandler
	public void onPlayerJoinEvent(PlayerJoinEvent event) {
		Settings settings = new Settings();
		if (!settings.getIsEmpty()) {
			Location loc = new Location(Bukkit.getWorld(settings.getWorld()), settings.getX(), settings.getY(), settings.getZ()
					, settings.getYaw().floatValue(), settings.getPitch().floatValue());
			event.getPlayer().teleport(loc);
		}
	}

}
