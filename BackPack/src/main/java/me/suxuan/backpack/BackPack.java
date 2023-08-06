package me.suxuan.backpack;

import lombok.Getter;
import org.mineacademy.fo.plugin.SimplePlugin;

import java.util.ArrayList;
import java.util.List;

public final class BackPack extends SimplePlugin {

	@Getter
	private static List<String> editing = new ArrayList<>();

	@Override
	protected void onPluginStart() {
		registerCommand(new PackCommand());
		registerEvents(new PlayerListener());
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

	public static BackPack getInstance() {
		return (BackPack) SimplePlugin.getInstance();
	}
}
