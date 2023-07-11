package me.suxuan.recordblock.utils;

import me.suxuan.recordblock.RecordBlock;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

public class FileUtil {

	public static void createFile() {
		File file = getFile();

		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				RecordBlock.getInstance().getLogger().log(Level.WARNING, "创建文件" + file.getName() + "失败!");
			}
		}
	}

	public static void initialPlayerData(Player player) throws IOException {
		File file = getFile();

		YamlConfiguration modifyFile = YamlConfiguration.loadConfiguration(file);

		Map<String, Integer> map = new HashMap<>();
		map.put("Block_Break", 0);
		map.put("Block_Place", 0);

		modifyFile.set(player.getName(), map);
		modifyFile.save(file);
	}

	public static File getFile() {
		String fileName = TimeUtil.handleTime() + ".yml";
		return new File(RecordBlock.getInstance().getDataFolder(), fileName);
	}

}
