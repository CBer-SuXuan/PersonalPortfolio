package me.suxuan.recordblock.events;

import me.suxuan.recordblock.utils.FileUtil;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import java.io.File;
import java.io.IOException;

public class BreakBlockEvent implements Listener {

	@EventHandler
	public void onBlockBreakEvent(BlockBreakEvent event) throws IOException {

		FileUtil.createFile();
		File file = FileUtil.getFile();
		YamlConfiguration modifyFile = YamlConfiguration.loadConfiguration(file);

		Player player = event.getPlayer();

		int break_new = modifyFile.getInt(player.getName() + ".Block_Break", 0) + 1;
		modifyFile.set(player.getName() + ".Block_Break", break_new);
		modifyFile.save(file);

	}

}
