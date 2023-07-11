package me.suxuan.loginreward;

import lombok.Getter;
import org.mineacademy.fo.settings.YamlConfig;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public final class PlayerData extends YamlConfig {

	private static Map<String, PlayerData> playerData = new HashMap<>();

	private final String playerName;

	private List<String> items;

	private PlayerData(String playerName) {
		this.playerName = playerName;

		this.loadConfiguration(NO_DEFAULT, "players/" + playerName + ".yml");  // 加载文件
		this.save();
	}

	@Override
	protected void onLoad() {
		this.items = this.getStringList("Give_Items");
	}

	@Override
	protected void onSave() {
		this.set("Give_Items", this.items);
	}

	public void setItem(String itemInfo) {
		List<String> items = this.getStringList("Give_Items");
		items.add(itemInfo);
		this.items = items;
		this.save();
	}

	public static PlayerData from(String playerName) {
		PlayerData data = playerData.get(playerName);
		if (data == null) {
			data = new PlayerData(playerName);
			playerData.put(playerName, data);
		}
		return data;
	}

	public static void remove(String playerName) {
		playerData.remove(playerName);
	}

}
