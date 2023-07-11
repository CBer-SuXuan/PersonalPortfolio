package me.suxuan.loginreward;

import org.mineacademy.fo.plugin.SimplePlugin;

public final class LoginReward extends SimplePlugin {

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

	public static LoginReward getInstance() {
		return (LoginReward) SimplePlugin.getInstance();
	}
}
