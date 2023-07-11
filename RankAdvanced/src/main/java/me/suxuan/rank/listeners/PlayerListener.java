package me.suxuan.rank.listeners;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import me.suxuan.rank.Rank;
import me.suxuan.rank.settings.LocalData;
import me.suxuan.rank.settings.MySQLData;
import me.suxuan.rank.settings.SQLiteData;
import me.suxuan.rank.settings.Settings;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.annotation.AutoRegister;

import java.sql.SQLException;

/**
 * @author CBer_SuXuan
 * @className PlayerListener
 * @date 2023/5/12 20:42
 * @description
 */
@AutoRegister
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public final class PlayerListener implements Listener {

	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		switch (Settings.USE_DATABASE) {
			case "Local" -> {
				LocalData data = LocalData.from(event.getPlayer());
				break;
			}
			case "SQLite" -> {
				SQLiteData data = SQLiteData.from(event.getPlayer());
				break;
			}
			case "MySQL" -> {
				try {
					Player player = event.getPlayer();
					MySQLData data = Rank.getInstance().getMySQLDatabase().findPlayerDataByUUID(player.getUniqueId());
					if (data == null) {
						data = new MySQLData(player.getName(), player.getUniqueId(), "None", "Mortal", 0, 0, 0);
						Rank.getInstance().getMySQLDatabase().createPlayerData(data);
					}
				} catch (SQLException e) {
					Common.log("There are some errors of MySQL(listener)! " + e.getMessage());
				}
				break;
			}
		}

	}

	@EventHandler
	public void onQuit(PlayerQuitEvent event) {
		final Player player = event.getPlayer();
		switch (Settings.USE_DATABASE) {
			case "Local" -> LocalData.remove(player);
			case "SQLite" -> SQLiteData.remove(player);
		}
	}

}
