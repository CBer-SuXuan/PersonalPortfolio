package me.suxuan.heartcraft;

import org.mineacademy.fo.plugin.SimplePlugin;

public final class HeartCraft extends SimplePlugin {

	@Override
	protected void onPluginStart() {
		registerCommand(new HeartCommand());
		registerEvents(new PlayerListener());
	}

	@Override
	protected void onReloadablesStart() {

	}

	@Override
	protected void onPluginPreReload() {

	}

	public static HeartCraft getInstance() {
		return (HeartCraft) SimplePlugin.getInstance();
	}
}
