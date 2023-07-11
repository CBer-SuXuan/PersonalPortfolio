package me.suxuan.recordblock;

import me.suxuan.recordblock.events.BreakBlockEvent;
import me.suxuan.recordblock.events.PlaceBlockEvent;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public final class RecordBlock extends JavaPlugin {

	private static RecordBlock instance;

	@Override
	public void onEnable() {

		RecordBlock.instance = this;
		Bukkit.getPluginManager().registerEvents(new BreakBlockEvent(), this);
		Bukkit.getPluginManager().registerEvents(new PlaceBlockEvent(), this);

		File file = new File(getDataFolder().toURI());
		if (!file.exists()) file.mkdir();

	}

	public static RecordBlock getInstance() {
		return RecordBlock.instance;
	}

}
