package me.suxuan.wardrobe.events;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.suxuan.wardrobe.Main;
import me.suxuan.wardrobe.manager.PlayerInfoManager;
import me.suxuan.wardrobe.manager.WardrobeManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.mineacademy.fo.annotation.AutoRegister;

@AutoRegister
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class PlayerListener implements Listener {

	@Getter
	private static final PlayerListener instance = new PlayerListener();

	// Initial player if there are no records in database.
	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		Main.getInstance().getPlayerInfoDatabase().initialPlayerData(event.getPlayer());
		PlayerInfoManager.from(event.getPlayer());
		WardrobeManager.from(event.getPlayer());
	}

	@EventHandler
	public void onQuit(PlayerQuitEvent event) {
		PlayerInfoManager.remove(event.getPlayer());
		WardrobeManager.remove(event.getPlayer());
	}


}
