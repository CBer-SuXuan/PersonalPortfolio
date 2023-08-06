package me.suxuan.rollboss;

import org.mineacademy.fo.Common;
import org.mineacademy.fo.plugin.SimplePlugin;

public final class RollBossBar extends SimplePlugin {

	@Override
	protected void onPluginStart() {
		registerCommand(new ReloadCommand());
	}

	@Override
	protected void onReloadablesStart() {
		Common.runTimer(20 * Settings.INTERVAL, new ShowBossBar());
	}

	public static RollBossBar getInstance() {
		return (RollBossBar) SimplePlugin.getInstance();
	}
}
