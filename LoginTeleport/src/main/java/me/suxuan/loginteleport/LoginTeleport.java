package me.suxuan.loginteleport;

import org.mineacademy.fo.plugin.SimplePlugin;

public final class LoginTeleport extends SimplePlugin {

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

	public static LoginTeleport getInstance() {
		return (LoginTeleport) SimplePlugin.getInstance();
	}
}
