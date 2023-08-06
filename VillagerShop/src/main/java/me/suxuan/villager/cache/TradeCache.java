package me.suxuan.villager.cache;

import lombok.Getter;
import me.suxuan.villager.VillagerShop;
import org.bukkit.entity.Player;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.collection.SerializedMap;
import org.mineacademy.fo.settings.YamlConfig;

import java.io.File;
import java.util.List;

@Getter
public final class TradeCache extends YamlConfig {

	private final String tradeName;

	private String displayName;
	private SerializedMap deals;
	private Integer dealsAmount;
	private Boolean delete;

	private TradeCache(String tradeName) {
		this.tradeName = tradeName;
		this.loadConfiguration(NO_DEFAULT, "trades/" + tradeName + ".yml");
		this.save();
	}

	@Override
	protected void onLoad() {
		this.displayName = getString("Display_Name", "&6Trade");
		this.deals = getMap("Deals");
		this.dealsAmount = getInteger("Deals_Amount", 0);
		this.delete = getBoolean("Delete", false);
	}

	@Override
	protected void onSave() {
		this.set("Display_Name", this.displayName);
		this.set("Deals", this.deals);
		this.set("Deals_Amount", this.dealsAmount);
		this.set("Delete", this.delete);
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
		this.save();
	}

	public void changeDeals(SerializedMap deal, int number) {
		SerializedMap map = getMap("Deals");
		map.override(String.valueOf(number), deal);
		this.deals = map;
		this.save();
	}

	public void addDeals(SerializedMap deal) {
		SerializedMap map = getMap("Deals");
		map.override(String.valueOf(getInteger("Deals_Amount")), deal);
		this.deals = map;
		this.save();
	}

	public void addCommand(String command, int number) {
		SerializedMap map = getMap("Deals").getMap(String.valueOf(number));
		List<String> commands = map.getStringList("command");
		commands.add(command);
		map.override("command", commands);

		SerializedMap deal_map = getMap("Deals");
		deal_map.override(String.valueOf(number), map);
		this.deals = deal_map;
		this.save();
	}

	public void deleteCommand(int index, int number) {
		SerializedMap map = getMap("Deals").getMap(String.valueOf(number));
		List<String> commands = map.getStringList("command");
		commands.remove(index);
		map.override("command", commands);

		SerializedMap deal_map = getMap("Deals");
		deal_map.override(String.valueOf(number), map);
		this.deals = deal_map;
		this.save();
	}

	public void deleteDeals(Integer number) {
		SerializedMap map = getMap("Deals");
		if (map.isEmpty() || map.getMap(String.valueOf(number)).isEmpty()) {
			this.delete = true;
			return;
		}
		for (int i = number; i < map.size(); i++) {
			map.override(String.valueOf(i), map.getMap(String.valueOf(i + 1)));
		}
		map.remove(String.valueOf(map.size() - 1));
		this.deals = map;
		this.dealsAmount = getInteger("Deals_Amount") - 1;
		this.delete = true;
		this.save();
	}

	public void setDelete(Boolean delete) {
		this.delete = delete;
		this.save();
	}

	public void addDealsAmount() {
		this.dealsAmount = getInteger("Deals_Amount") + 1;
		this.save();
	}

	public static TradeCache from(String fileName) {
		return new TradeCache(fileName);
	}

	public static void remove(String fileName, Player player) {
		File file = new File(VillagerShop.getInstance().getDataFolder(), "trades/" + fileName + ".yml");
		if (file.delete())
			Common.tell(player, "&6Trade '" + fileName + "' delete successfully!");
	}
}
