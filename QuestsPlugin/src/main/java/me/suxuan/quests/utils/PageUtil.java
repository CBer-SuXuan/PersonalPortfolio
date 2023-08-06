package me.suxuan.quests.utils;

import me.suxuan.quests.QuestsPlugin;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class PageUtil {

	public static boolean needAddPages(String type) {
		File[] types = new File(QuestsPlugin.getInstance().getDataFolder(), type).listFiles((dir, name) -> !name.equals("template.yml"));
		if (types == null) return false;
		for (File file : types) {
			YamlConfiguration yaml = YamlConfiguration.loadConfiguration(file);
			String position = yaml.getString("Position");
			if (position == null) return false;
			if (Integer.parseInt(position.split(";")[0]) >= 2)
				return true;
		}
		return false;
	}

	public static int getMaxPage(String type) {
		int max = 1;
		File[] types = new File(QuestsPlugin.getInstance().getDataFolder(), type).listFiles((dir, name) -> !name.equals("template.yml"));
		if (types == null) return -1;
		for (File file : types) {
			YamlConfiguration yaml = YamlConfiguration.loadConfiguration(file);
			String position = yaml.getString("Position");
			if (position == null) return -1;
			if (Integer.parseInt(position.split(";")[0]) > max)
				max = Integer.parseInt(position.split(";")[0]);
		}
		return max;
	}

}
