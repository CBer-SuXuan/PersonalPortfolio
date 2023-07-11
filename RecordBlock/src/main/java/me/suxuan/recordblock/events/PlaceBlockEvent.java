package me.suxuan.recordblock.events;

import me.suxuan.recordblock.utils.FileUtil;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

import java.io.File;
import java.io.IOException;

public class PlaceBlockEvent implements Listener {

	@EventHandler
	public void onBlockPlaceEvent(BlockPlaceEvent event) throws IOException {

		FileUtil.createFile();
		File file = FileUtil.getFile();
		YamlConfiguration modifyFile = YamlConfiguration.loadConfiguration(file);

		Player player = event.getPlayer();

		int place_new = modifyFile.getInt(player.getName() + ".Block_Place", 0) + 1;
		modifyFile.set(player.getName() + ".Block_Place", place_new);
		modifyFile.save(file);

	}

}
