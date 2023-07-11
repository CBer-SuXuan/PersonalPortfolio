package me.suxuan.joinreward;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.menu.model.ItemCreator;
import org.mineacademy.fo.remain.CompMaterial;

import java.util.List;

public class PlayerListener implements Listener {

	@EventHandler
	public void onPlayerJoinEvent(PlayerJoinEvent event) {
		if (event.getPlayer().hasPlayedBefore()) return;
		List<String> items = Settings.GIVE_ITEMS;
		for (int i = 0; i < items.size(); i++) {
			String[] item_split = items.get(i).split(";");
			CompMaterial mat = CompMaterial.fromString(item_split[0]);
			if (mat == null) {
				Common.log("[&6&lLogin&9&lReward&f] &c&lconfig.yml中第 " + (i + 1) + " 行物品名 "
						+ item_split[0] + " 有误，请核实后重载插件！");
				return;
			}
			try {
				int number = Integer.parseInt(item_split[1]);
			} catch (NumberFormatException error) {
				Common.log("[&6&lLogin&9&lReward&f] &c&lconfig.yml中第 " + (i + 1) + " 行数字 "
						+ item_split[1] + " 有误，请核实后重载插件！");
				return;
			}
		}
		for (String item : items) {
			ItemCreator.of(CompMaterial.fromString(item.split(";")[0]))
					.amount(Integer.parseInt(item.split(";")[1]))
					.give(event.getPlayer());
		}
	}

}
