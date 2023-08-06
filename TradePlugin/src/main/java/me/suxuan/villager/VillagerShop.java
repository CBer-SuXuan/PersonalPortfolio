package me.suxuan.villager;

import lombok.Getter;
import me.suxuan.villager.command.MainCommand;
import me.suxuan.villager.events.PlayerEvent;
import org.mineacademy.fo.plugin.SimplePlugin;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public final class VillagerShop extends SimplePlugin {

	@Getter
	private static List<String> editing = new ArrayList<>();

	/**
	 * Automatically perform login ONCE when the plugin starts.
	 */
	@Override
	protected void onPluginStart() {
		File file = new File(getDataFolder(), "trades");
		if (!file.exists()) file.mkdir();
		registerCommand(new MainCommand());
		registerEvents(new PlayerEvent());
	}

	/**
	 * Automatically perform login when the plugin starts and each time it is reloaded.
	 */
	@Override
	protected void onReloadablesStart() {

		// You can check for necessary plugins and disable loading if they are missing
		//Valid.checkBoolean(HookManager.isVaultLoaded(), "You need to install Vault so that we can work with packets, offline player data, prefixes and groups.");

		// Uncomment to load variables
		// Variable.loadVariables();

		//
		// Add your own plugin parts to load automatically here
		// Please see @AutoRegister for parts you do not have to register manually
		//
	}

	@Override
	protected void onPluginPreReload() {

		// Close your database here if you use one
		//YourDatabase.getInstance().close();
	}

	public static VillagerShop getInstance() {
		return (VillagerShop) SimplePlugin.getInstance();
	}
}
