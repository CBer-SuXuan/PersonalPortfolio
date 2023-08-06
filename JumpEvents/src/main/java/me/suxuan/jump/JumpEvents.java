package me.suxuan.jump;

import org.mineacademy.fo.plugin.SimplePlugin;

public final class JumpEvents extends SimplePlugin {

	@Override
	protected void onPluginStart() {
		registerCommand(new ReloadCommand());
		registerEvents(new PlayerEvent());
	}

	@Override
	protected void onReloadablesStart() {
	}

	public static JumpEvents getInstance() {
		return (JumpEvents) SimplePlugin.getInstance();
	}
}
