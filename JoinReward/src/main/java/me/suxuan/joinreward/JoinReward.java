package me.suxuan.joinreward;

import org.mineacademy.fo.plugin.SimplePlugin;

public final class JoinReward extends SimplePlugin {

	@Override
	protected void onPluginStart() {
		registerCommand(new MainCommand());
		registerEvents(new PlayerListener());
	}

	@Override
	protected void onReloadablesStart() {

	}

	@Override
	protected void onPluginPreReload() {

	}

	public static JoinReward getInstance() {
		return (JoinReward) SimplePlugin.getInstance();
	}
}
