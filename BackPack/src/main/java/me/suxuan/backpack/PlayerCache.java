package me.suxuan.backpack;

import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.mineacademy.fo.collection.SerializedMap;
import org.mineacademy.fo.settings.YamlConfig;

import java.util.*;

@Getter
public class PlayerCache extends YamlConfig {

	private static final Map<UUID, PlayerCache> cacheMap = new HashMap<>();

	private final String playerName;
	private final UUID uniqueId;

	private SerializedMap items;

	private PlayerCache(String playerName, UUID uniqueId) {
		this.playerName = playerName;
		this.uniqueId = uniqueId;

		this.setHeader("The name of this UUID is '" + playerName + "'.");
		this.loadConfiguration(NO_DEFAULT, "players/" + uniqueId + ".yml");
		this.save();
	}

	@Override
	protected void onLoad() {
		SerializedMap map = new SerializedMap();
		List<ItemStack> stacks = new ArrayList<>();
		for (String name : Settings.COMMAND_LIST) {
			map.put(name, stacks);
		}
		this.items = map;
	}

	@Override
	protected void onSave() {
		this.set("Items", this.items);
	}

	public void changeItems(String name, List<ItemStack> stacks) {
		SerializedMap old = getMap("Items");
		if (!old.containsKey(name))
			System.out.println("No info in the player cache");
		old.override(name, stacks);
		this.items = old;
		this.save();
	}

	public void removeFromMemory() {
		synchronized (cacheMap) {
			cacheMap.remove(this.uniqueId);
		}
	}

	public static PlayerCache from(Player player) {
		synchronized (cacheMap) {
			final UUID uniqueId = player.getUniqueId();
			final String playerName = player.getName();
			PlayerCache cache = cacheMap.get(uniqueId);
			if (cache == null) {
				cache = new PlayerCache(playerName, uniqueId);
				cacheMap.put(uniqueId, cache);
			}
			return cache;
		}
	}

	public static void clearCaches() {
		synchronized (cacheMap) {
			cacheMap.clear();
		}
	}
}
