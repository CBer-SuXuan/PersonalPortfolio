package me.suxuan.rollboss;

import org.bukkit.entity.Player;
import org.mineacademy.fo.remain.CompBarColor;
import org.mineacademy.fo.remain.CompBarStyle;
import org.mineacademy.fo.remain.Remain;

public class ShowBossBar implements Runnable {

	private int index = 0;

	@Override
	public void run() {

		if (index >= Settings.TEXTS.size())
			index = 0;
		for (Player player : Remain.getOnlinePlayers()) {
			if (player.getWorld().getName().equals(Settings.WORLD))
				Remain.sendBossbarPercent(player, checkReplace(Settings.TEXTS.get(index).split(";")[0], player), 100,
						CompBarColor.fromKey(Settings.TEXTS.get(index).split(";")[1]),
						CompBarStyle.fromKey(Settings.TEXTS.get(index).split(";")[2]));
		}
		index++;

	}

	private String checkReplace(String text, Player player) {
		String text_change = text;
		if (text.contains("%NAME%"))
			text_change = text_change.replace("%NAME%", player.getName());
		if (text.contains("%TPS%"))
			text_change = text_change.replace("%TPS%", String.valueOf(Remain.getTPS()));
		if (text.contains("%X%"))
			text_change = text_change.replace("%X%", String.valueOf(player.getLocation().getX()));
		if (text.contains("%Y%"))
			text_change = text_change.replace("%Y%", String.valueOf(player.getLocation().getY()));
		if (text.contains("%Z%"))
			text_change = text_change.replace("%Z%", String.valueOf(player.getLocation().getZ()));
		return text_change;
	}

}
