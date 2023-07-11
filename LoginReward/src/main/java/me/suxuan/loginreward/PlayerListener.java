package me.suxuan.loginreward;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.mineacademy.fo.menu.model.ItemCreator;
import org.mineacademy.fo.remain.CompMaterial;

import java.io.File;
import java.util.List;

public class PlayerListener implements Listener {

	@EventHandler
	public void onPlayerJoinEvent(PlayerJoinEvent event) {
		File file = new File(LoginReward.getInstance().getDataFolder().getAbsolutePath() +
				"\\players\\" + event.getPlayer().getName() + ".yml");
		if (file.exists()) {
			List<String> list = PlayerData.from(event.getPlayer().getName()).getItems();
			for (String item : list) {
				String[] item_split = item.split(";");
				ItemCreator.of(CompMaterial.fromString(item_split[0]))
						.amount(Integer.parseInt(item_split[1]))
						.give(event.getPlayer());
			}
			file.delete();
		}
		PlayerData.remove(event.getPlayer().getName());
	}

	@EventHandler
	public void onPlayerLeaveEvent(PlayerQuitEvent event) {
		PlayerData.remove(event.getPlayer().getName());
	}

}
