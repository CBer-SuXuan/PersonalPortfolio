package me.suxuan.wardrobe.manager;

import lombok.Getter;
import lombok.Setter;
import me.suxuan.wardrobe.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerInfoManager {

	private static final Map<UUID, PlayerInfoManager> playerInfoManager = new HashMap<>();

	private final UUID uuid;
	@Setter
	@Getter
	private Integer unlock;
	@Setter
	@Getter
	private Integer selected;

	private PlayerInfoManager(UUID uuid) {
		this.uuid = uuid;
		this.unlock = Main.getInstance().getPlayerInfoDatabase().getUnlock(Bukkit.getPlayer(uuid));
		this.selected = Main.getInstance().getPlayerInfoDatabase().getSelected(Bukkit.getPlayer(uuid));
	}

	private void saveToDatabase() {
		Main.getInstance().getPlayerInfoDatabase().setSelected(Bukkit.getPlayer(uuid), selected);
		Main.getInstance().getPlayerInfoDatabase().setUnlock(Bukkit.getPlayer(uuid), unlock);
	}

	public static PlayerInfoManager from(Player player) {
		UUID uuid = player.getUniqueId();
		PlayerInfoManager data = playerInfoManager.get(uuid);
		if (data == null) {
			data = new PlayerInfoManager(uuid);
			playerInfoManager.put(uuid, data);
		}
		return data;
	}

	public static void remove(Player player) {
		playerInfoManager.get(player.getUniqueId()).saveToDatabase();
		playerInfoManager.remove(player.getUniqueId());
	}

}
